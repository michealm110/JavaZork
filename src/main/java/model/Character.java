package model;

import java.util.ArrayList;
import java.util.List;
import model.rooms.*;
import model.items.*;
import model.GameContext;


public class Character {
    private String name;
    private Room currentRoom;
    private List<Item> inventory = new ArrayList<>();

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
    }

    public String getName() {
        return this.name;
    }

    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void addItem(Item item) {
        this.inventory.add(item);
    }

    public Item removeItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                inventory.remove(item);
                return item;
            }
        }
        return null;
    }

    public Item getItemFromInventory(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void listItems(GameContext game) {
        if (inventory.isEmpty()) {
            game.showMessage("Your inventory is currently empty.");
        } else {
            int i = 1;
            for (Item item : inventory) {
                game.showMessage(i + ". " + item.getName());
                i++;
            }
        }
    }
    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
}