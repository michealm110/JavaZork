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
                        room.setExit(dir, exit);
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

class WorldBuilderOld {
    public static Character createPlayerAndWorld() {

        Room takeawayFront = new Room(
                "takeaway_front",
                "takeaway_front",
                "outside the front door of Kingly Kebab, neon light spilling onto the wet pavement."
       //         Region.TAKEAWAY
        );

        Room staffAlley = new Room(
                "staff_alley",
                "staff_alley",
                "in the narrow staff alley behind the takeaway. Bins, crates, and the smell of old chips."
                //Region.TAKEAWAY
        );

        Room stockRoom = new Room(
                "stock_room",
                "stock_room",
                "in the cramped stock room: boxes of sauces, spare menus, and random junk piled everywhere."
                //Region.TAKEAWAY
        );

        Room mainStreetWest = new Room(
                "main_street_west",
                "main_street_west",
                "on Main Street West. Shuttered shops and parked cars line the road."
                //Region.STREETS
        );

        Room mainStreetEast = new Room(
                "main_street_east",
                "main_street_east",
                "on Main Street East, closer to the estates. An off-licence hums with low music nearby."
                //Region.STREETS
        );

        Room parkEntrance = new Room(
                "park_entrance",
                "park_entrance",
                "at the entrance to a dimly lit park. Trees arch overhead and a path winds into the darkness."
                //Region.PARK
        );

        Room darkPark = new Room(
                "dark_park",
                "dark_park",
                "deep in the park. It’s so dark that without extra light you feel like you could get lost."
                //Region.PARK
        );

        Room estateEntrance = new Room(
                "estate_entrance",
                "estate_entrance",
                "at the entrance to Crooked Lane estate. Identical houses sprawl in every direction."
                //Region.ESTATE
        );

        Room twistyJunction = new Room(
                "twisty_junction",
                "twisty_junction",
                "at a junction of narrow estate roads. Every corner looks weirdly similar."
                //Region.ESTATE
        );

        Room northStreet = new Room(
                "north_street",
                "north_street",
                "on a silent residential street. The same type of semi-detached houses repeat endlessly."
                //Region.ESTATE
        );

        Room deepStreet = new Room(
                "deep_street",
                "deep_street",
                "on a deeper stretch of road in the estate. It feels like you are further from civilisation."
                //Region.ESTATE
        );

        Room dizzyStreet = new SpinnerRoom(
                "dizzy_street",
                "dizzy_street",
                "at a strange corner where the houses seem to shift when you’re not looking."
                //Region.ESTATE
        );

        Room shadowAlley = new Room(
                "shadow_alley",
                "shadow_alley",
                "in a narrow alley between houses. It’s quiet, and you see your shadow in each window."
                //Region.ESTATE
        );

        Room southStreet = new Room(
                "south_street",
                "south_street",
                "on a street ending at a tall metal gate to the south. A small sign reads ‘Private walkway’."
                //Region.ESTATE
        );

        Room gardenPath = new Room(
                "garden_path",
                "garden_path",
                "on a narrow garden path that winds behind the houses. A single house light glows ahead."
                //Region.ESTATE
        );

        Room customerHouse = new EndRoom(
                "customer_house",
                "customer_house",
                "outside No. 14 Crooked Lane, your customer’s house. Warm light spills from behind the curtains."
                //Region.ESTATE
        );

        Item key = new KeyItem(
                "golden_key",
                "Golden Key",
                "A large golden key with ‘CL’ scratched into it. It probably opens something in the estate."
        );

        Item chalk = new ChalkItem(
                "white_chalk",
                "Chalk",
                "A stub of white chalk. Perfect for marking walls so you don’t get lost."
        );

        stockRoom.addItem(key);   
        darkPark.addItem(chalk);
       

        takeawayFront.setExit("south", Exit.createUnlockedExit(staffAlley));
        staffAlley.setExit("north", Exit.createUnlockedExit(takeawayFront));

        staffAlley.setExit("east", Exit.createUnlockedExit(stockRoom));
        stockRoom.setExit("west", Exit.createUnlockedExit(staffAlley));

        takeawayFront.setExit("east",Exit.createUnlockedExit( mainStreetWest));
        mainStreetWest.setExit("west", Exit.createUnlockedExit(takeawayFront));

        mainStreetWest.setExit("east", Exit.createUnlockedExit(mainStreetEast));
        mainStreetEast.setExit("west", Exit.createUnlockedExit(mainStreetWest));

        mainStreetEast.setExit("north", Exit.createUnlockedExit(parkEntrance));
        parkEntrance.setExit("south", Exit.createUnlockedExit(mainStreetEast));

        parkEntrance.setExit("north", Exit.createUnlockedExit(darkPark));
        darkPark.setExit("south", Exit.createUnlockedExit(parkEntrance));

        mainStreetEast.setExit("south", Exit.createUnlockedExit(estateEntrance));
        estateEntrance.setExit("north", Exit.createUnlockedExit(mainStreetEast));

        estateEntrance.setExit("east", Exit.createUnlockedExit(twistyJunction));
        twistyJunction.setExit("west", Exit.createUnlockedExit(estateEntrance));

        twistyJunction.setExit("north", Exit.createUnlockedExit(northStreet));
        northStreet.setExit("south", Exit.createUnlockedExit(twistyJunction));

        northStreet.setExit("east", Exit.createUnlockedExit(deepStreet));
        deepStreet.setExit("west", Exit.createUnlockedExit(northStreet));

        deepStreet.setExit("south", Exit.createUnlockedExit(dizzyStreet));
        dizzyStreet.setExit("north", Exit.createUnlockedExit(deepStreet));

        dizzyStreet.setExit("south", Exit.createUnlockedExit(shadowAlley));
        shadowAlley.setExit("north", Exit.createUnlockedExit(dizzyStreet));

        twistyJunction.setExit("south", Exit.createUnlockedExit(southStreet));
        southStreet.setExit("north", Exit.createUnlockedExit(twistyJunction));

        southStreet.setExit("south", Exit.createLockedExit(gardenPath, "golden_key",
                "The gate is locked. You need a key to open it.")); 

        gardenPath.setExit("north", Exit.createUnlockedExit(southStreet));
        gardenPath.setExit("east", Exit.createUnlockedExit(customerHouse));
        customerHouse.setExit("west", Exit.createUnlockedExit(gardenPath)); 

        Character player = new Character("Player", takeawayFront);
        return player;
    }
}