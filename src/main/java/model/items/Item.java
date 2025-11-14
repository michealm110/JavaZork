package model.items;

//we'll get to polymorphism later
public class Item {
    private String id;
    private String name;
    private String description;
    private boolean isPortable;
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

    //public abstract void use(GameContext context, Character player);
}