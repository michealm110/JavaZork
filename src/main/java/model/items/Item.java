package model.items;

import model.GameContext;
import model.Character;

//we'll get to polymorphism later
public abstract class Item {
    private final String id;
    private final String name;
    private final String description;
    private final boolean isPortable;
    private final ItemType type;

    public Item(String id, String name, String description, boolean isPortable, ItemType type) {
        this.id = id;
        this.isPortable = isPortable;
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

    public String getId() {
        return this.id;
    }

    public abstract void use(GameContext context, Character player, String target);
}