package model.items;

import model.GameContext;
import model.Character;
import model.rooms.Room;

public class ChalkItem extends Item {
    public ChalkItem(String name, String description) {
        super(name, description, ItemType.CHALK);
    }

    public void use(GameContext context, Character player) {
        if (context.keyInInventory()) {
            context.showMessage("You have already used the chalk to mark your path.");
            return;
        }

        Room current = player.getCurrentRoom();

        if (!current.hasChalkMark()) {
            current.markWithChalk();
            context.showMessage("You mark the room with chalk to remember you've been here.");
        } else {
            context.showMessage("This room is already marked with chalk.");
        }
    }
}