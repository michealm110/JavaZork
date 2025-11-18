package model.items;

import model.GameContext;
import model.Character;
import model.rooms.Room;

public class ChalkItem extends Item {
    public ChalkItem(String id, String name, String description) {
        super(id, name, description, true, ItemType.CHALK);
    }

    @Override
    public void use(GameContext context, Character player, String target) {
        // Chalk ignores the target. "use chalk north" vs "use chalk" does the same thing.
        Room current = player.getCurrentRoom();
        current.markWithChalk();
        context.showMessage("You mark the floor with chalk.");
    }
}