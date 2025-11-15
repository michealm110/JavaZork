package model;

import model.items.*;
import model.rooms.Exit;

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

    public Room load(String fileName) {
        try {
            //first, load as JSON from the expected path.
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
                Room room = new Room(rJson.getString("id"), rJson.getString("name"), rJson.getString("description"));
                
                if (rJson.has("items")) {
                    JSONArray items = rJson.getJSONArray("items");
                    for (int j = 0; j < items.length(); j++) {
                            JSONObject iJson = items.getJSONObject(j);
                        //TODO: clean this up
                        boolean isPort = iJson.optBoolean("portable") || Boolean.parseBoolean(iJson.optString("portable"));
                        room.addItem(new Item(iJson.getString("id"), iJson.getString("name"), iJson.getString("desc"), isPort, iJson.getEnum(ItemType.class, "type")));
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
                            String key = lock.getString("key");
                            String msg = lock.getString("msg");
                            exit =  Exit.createLockedExit(rooms.get(exits.getString(dir)), key, msg);
                        } else {
                            exit =  Exit.createUnlockedExit(room); 
                        }
                        Direction direction = Direction.fromString(dir);
                        room.setExit(direction, exit);
                    }
                }
            }
            
            // TODO: createdNPCs
            if (worldJson.has("npcs")) {
                JSONArray npcArray = worldJson.getJSONArray("npcs");
                for (int i = 0; i < npcArray.length(); i++) {
                    JSONObject n = npcArray.getJSONObject(i);
                    boolean hostile = n.optBoolean("hostile") || Boolean.parseBoolean(n.optString("hostile"));
                    npcs.add(new NPC(n.getString("id"), n.getString("name"), n.getString("desc"), n.getString("room"), hostile));
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
