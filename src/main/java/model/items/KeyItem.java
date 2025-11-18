package model.items;

import model.Character;
import model.Direction;
import model.GameContext;   
import model.rooms.Room; 
import model.rooms.Exit;


public class KeyItem extends model.items.Item {

    public KeyItem(String id, String name, String description) {
        super(id, name, description, true, ItemType.KEY);
    }

    @Override
    public void use(GameContext context, Character player, String target) {
        if (target == null) {
            context.showMessage("Use the key on what? (Example: 'use key north')");
            return;
        }

        Direction dir;
        try {
            dir = Direction.fromString(target);
        } catch (Exception e) {
            context.showMessage("That is not a valid direction.");
            return;
        }

        Room currentRoom = player.getCurrentRoom();

        Exit exit = currentRoom.getExit(dir);

        if (exit == null) {
            context.showMessage("There is no exit to the " + dir.getText() + ".");
            return;
        }

        if (!exit.isLocked()) {
            context.showMessage("The door to the " + dir.getText() + " is already unlocked.");
            return;
        }



        if (exit.unlock(this.getId())) {    
            context.showMessage(exit.getMessageUnlock());
        } else {
            context.showMessage(exit.getMessageUnlockFail());
        }

    }
}
