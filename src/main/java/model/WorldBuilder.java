package model;

import model.items.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.rooms.*;

public class WorldBuilder {
    private Map<String, Room> rooms = new HashMap<>();
    private List<NPC> npcs = new ArrayList<>();
    private String startingRoomId;

    private enum RoomType {
        NORMAL,
        SPINNER,
        END
    }

    public Room load(String fileName) {
        try {
            //first, load as JSON from the expected patho
            
            Path path = Paths.get(fileName);
            if (!Files.exists(path)) path = Paths.get("zork", fileName);
            if (!Files.exists(path)) throw new RuntimeException("File not found: " + fileName);
            InputStream is = new FileInputStream(path.toFile());
            JSONObject worldJson = new JSONObject(new JSONTokener(is));
            
            this.startingRoomId = worldJson.getString("startingRoom");
            JSONArray roomsArray = worldJson.getJSONArray("rooms");

            // Create Rooms & Items
            // rooms all have to be created before exits can be set or we get null pointerss
            for (int i = 0; i < roomsArray.length(); i++) {
                JSONObject rJson = roomsArray.getJSONObject(i);

                RoomType roomType = RoomType.NORMAL;
                if (rJson.has("type")) {
                    roomType = rJson.getEnum(RoomType.class, "type");
                }
                Room room;
                if (roomType == RoomType.SPINNER) {
                    room = new SpinnerRoom(rJson.getString("id"), rJson.getString("name"), rJson.getString("description"));
                } else if (roomType == RoomType.END) {
                    room = new EndRoom(rJson.getString("id"), rJson.getString("name"), rJson.getString("description"));
                } else {
                    room = new Room(rJson.getString("id"), rJson.getString("name"), rJson.getString("description"));
                }
                
                if (rJson.has("items")) {
                        JSONArray items = rJson.getJSONArray("items");
                        for (int j = 0; j < items.length(); j++) {
                        JSONObject iJson = items.getJSONObject(j);
                        
                        String id = iJson.getString("id");
                        String name = iJson.getString("name");
                        String desc = iJson.getString("desc");
                        boolean isPortable = iJson.optBoolean("portable", true);

                        
                        // Get type, default to GENERIC if missing
                        ItemType type = ItemType.GENERIC;
                        if(iJson.has("type")) {
                                type = iJson.getEnum(ItemType.class, "type");
                        }

                        Item item;
                        
                        // FACTORY LOGIC
                        switch (type) {
                                case KEY:
                                item = new KeyItem(id, name, desc);
                                break;
                                case CHALK:
                                item = new ChalkItem(id, name, desc);
                                break;
                                case WEAPON:
                                int dmg = iJson.optInt("damage", 5); // Default damage
                                item = new WeaponItem(id, name, desc, dmg);
                                break;
                                default:
                                item = new GenericItem(id, name, desc, isPortable);
                                break;
                        }
                        
                        room.addItem(item);
                        }
                }
                rooms.put(room.getId(), room);
            }

            // now  crreate Exits
            for (int i = 0; i < roomsArray.length(); i++) {
                JSONObject rJson = roomsArray.getJSONObject(i);
                Room room = rooms.get(rJson.getString("id"));
                
                if (rJson.has("exits")) {
                    JSONObject exits = rJson.getJSONObject("exits");
                    for (String dir : exits.keySet()) {
                        Exit exit; 
                        // Check for locks
                        if (rJson.has("lockedExits") && rJson.getJSONObject("lockedExits").has(dir)) {
                            JSONObject lock = rJson.getJSONObject("lockedExits").getJSONObject(dir);
                            String targetRoomId = exits.getString(dir);
                            Room targetRoom = rooms.get(targetRoomId);
                            exit =  Exit.createUnlockedExit(targetRoom); 
                            String key = lock.getString("key");
                            String msg = lock.getString("msg");
                            String msg_unlock = lock.getString("msg_unlock");
                            String msg_unlock_fail = lock.getString("msg_unlock_fail");
                            exit =  Exit.createLockedExit(targetRoom, key, msg, msg_unlock, msg_unlock_fail);
                        } else {
                            String targetRoomId = exits.getString(dir);
                            Room targetRoom = rooms.get(targetRoomId);
                            exit =  Exit.createUnlockedExit(targetRoom); 
                        }
                        Direction direction = Direction.fromString(dir);
                        room.setExit(direction, exit);
                    }
                }
            }
            
            // created npcs:
            if (worldJson.has("npcs")) {
                JSONArray npcArray = worldJson.getJSONArray("npcs");
                for (int i = 0; i < npcArray.length(); i++) {
                    JSONObject n = npcArray.getJSONObject(i);
                    boolean hostile = n.optBoolean("hostile");
                    
                    String roomId = n.getString("room");
                    boolean wandering = n.optBoolean("wandering", false);
                    NPC npc;

                    if (wandering) {
                        npc = new WanderingNPC(
                            n.getString("id"), 
                            n.getString("name"), 
                            n.getString("desc"), 
                            n.getString("conversation_piece"),
                            roomId, 
                            hostile
                        );
                    } else {
                         npc = new NPC(
                            n.getString("id"), 
                            n.getString("name"), 
                            n.getString("desc"), 
                            n.getString("conversation_piece"),
                            roomId, 
                            hostile
                        );
                    }
                    
                    // Add to global list and to room (just a reference not a duplicate)
                    npcs.add(npc);
                    
                    Room room = rooms.get(roomId);
                    if (room != null) {
                        room.addNPC(npc);
                    }
                }
            }


            is.close();
            
        } catch (Exception e) {
           // If the JSON is broken, the game can't start, so just crash.
            e.printStackTrace();
            System.exit(1);
        }
        
        return rooms.get(startingRoomId);
    }

    public Map<String, Room> getRooms() { return rooms; }
    public List<NPC> getNpcs() { return npcs; }
}
