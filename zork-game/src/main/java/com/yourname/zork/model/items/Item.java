package model.items;

import model.GameContext;
import model.Character;

public abstract class Item {
    private String description;
    private String name;
    private final ItemType type;

    public Item(String name, String description, ItemType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ItemType getType() {
        return this.type;
    }

    public abstract void use(GameContext context, Character player);
}