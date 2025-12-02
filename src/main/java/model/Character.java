package model;

import java.io.Serializable;
import java.util.List;
import model.rooms.*;
import model.items.*;


public class Character implements Serializable{
    private final String name;
    private Room currentRoom;
    private ItemContainer<Item> inventory;

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ItemContainer<>();
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

    public void addItem(Item item) throws InventoryFullException{
        this.inventory.add(item);
    }

    public Item removeItem(String itemName) {
        return this.inventory.remove(itemName);
    }

    public Item getItemFromInventory(String itemName) {
        return this.inventory.find(itemName);
    }

    public List<Item> getItems() {
        return this.inventory.getAll();
    }

    public void listItems(GameContext game) {
        if (inventory.isEmpty()) {
            game.showMessage("Your inventory is currently empty.");
        } else {
            int i = 1;
            for (Item item : inventory.getAll()) {
                game.showMessage(i + ". " + item.getName());
                i++;
            }
        }
    }
    public boolean hasItem(String itemName) {
        for (Item item : inventory.getAll()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
}