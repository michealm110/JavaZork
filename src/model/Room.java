package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private List<Item> items = new ArrayList();
    private String description;
    private Map<String, Room> exits;

    public Room(String description) {
        this.description = description;
        this.exits = new HashMap();
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
        return "You are " + var10000 + ".\nExits: " + this.getExitString() + "\n" + this.getItemString();
    }
}