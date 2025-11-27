package model;

import java.util.ArrayList;
import java.util.List;

import model.items.Item;

// one of these will be the inventory. ItemContainer<Item> inventory;
// another one coulde be a keyring. ItemContainer<KeyItem> keyRing;
public class ItemContainer<T extends Item> {
    
    // Internal storage using the generic type
    private final List<T> items;
    private final int capacity; 

    public ItemContainer() {
        this.items = new ArrayList<>(3);
        this.capacity = 3;
    }

    public ItemContainer(int capacity) {
        this.items = new ArrayList<>();
        this.capacity = capacity;
    }

    public void add(T item) throws InventoryFullException {
        if (items.size() < capacity) {
            items.add(item);
        } else {
            throw new InventoryFullException();
        }
    }

    public T find(String itemName) {
        for (T item : this.items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }        

    public T remove(String itemName) {
        for (T item : this.items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                this.items.remove(item);
                return item;
            }
        }
        return null;
    }

    public boolean has(String itemName) {
        return find(itemName) != null;
    }

    public List<T> getAll() {
        //is this a bit stupid?
        return new ArrayList<>(items); 
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }
}