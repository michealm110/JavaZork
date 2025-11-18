package model.items;

import model.GameContext;
import model.Character;

public class GenericItem extends Item {
    public GenericItem(String id, String name, String description, boolean isPortable) {
        super(id, name, description, isPortable, ItemType.GENERIC);
    }

    @Override
    public void use(GameContext context, Character player, String target) {
        context.showMessage("You try to use the " + getName() + ", but nothing interesting happens.");
    }
}