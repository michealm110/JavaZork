package model.rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import model.Item;
import model.GameContext;

public class Room {
    private List<Item> items = new ArrayList<>();
    private String description;
    private Map<String, Room> exits;
    private boolean isMarked = false;
    private String chalkMarking;

    public Room(String description) {
        this.description = description;
        this.exits = new HashMap<>();
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

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
        String var10000 = this.description; 
        String defaultMessage = "You are " + var10000 + ".\nExits: " + this.getExitString() + "\n" + this.getItemString();
        if (isMarked) {
            return defaultMessage += "\nThis room is marked with chalk that reads: " + chalkMarking;
        }
        return defaultMessage;
    }

    public void mark(String chalkMarking) {
        this.chalkMarking = chalkMarking;
        isMarked = true;
    }

    public boolean isMarked() {
        return isMarked;
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