package model.rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import model.items.*;
import model.GameContext;
import model.Region;

public class Room {
    
    private String id;
    private String name;
    private String description;
    private boolean isMarked = false;
    private String chalkMarking;
    private Map<String, Room> exits;
    private List<Item> items;
    //private Region region;

    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }

    //public Region getRegion() {
    //    return this.region;
    //}
    public String getId() { return this.id; }
    public String getName() { return this.name; }

    public void addItem(Item item) { this.items.add(item); }

    public Item removeItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public String getItemString() {
        if (this.items.isEmpty()) {
            return "No items here.";
        } else {
            StringBuilder sb = new StringBuilder("Items: ");

            for(Item item : this.items) {
                sb.append(item.getName()).append(", ");
            }

            return sb.substring(0, sb.length() - 2);
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setExit(String direction, Room neighbor) {
        this.exits.put(direction, neighbor);
    }

    public void setLockedExit(String direction) {
        this.lockedExits.put(direction, true);
    } 

    public boolean isExitLocked(String direction) {
        return this.lockedExits.getOrDefault(direction, false);
    }

    public void unlockExit(String direction) {
        this.lockedExits.put(direction, false);
    }

    public Room getExit(String direction) {
        return (Room)this.exits.get(direction);
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();

        for(String direction : this.exits.keySet()) {
            sb.append(direction).append(" ");
        }

        return sb.toString().trim();
    }

    public String getLongDescription() {
        String description = this.description; 
        String defaultMessage = "You are " + description + ".\nExits: " + this.getExitString() + "\n" + this.getItemString();
        if (this.isMarked) {
            return defaultMessage += "\nThis room is marked with chalk";
        }
        return defaultMessage;
    }

    public void markWithChalk() {
        this.isMarked = true;
    }

    public boolean hasChalkMark() {
        return this.isMarked;
    }

    // polymorphic onEnter method
    public void onEnter(GameContext game) {
        game.showMessage(getLongDescription());
    }

    protected void randomizeExits() {
        if (exits.size() < 2) {
            return;
        }

        List<String> directions = new ArrayList<>(exits.keySet());
        List<Room> rooms = new ArrayList<>(exits.values());

        Collections.shuffle(rooms);

        exits.clear();
        for (int i = 0; i < directions.size(); i++) {
            exits.put(directions.get(i), rooms.get(i));
        }
    }
}