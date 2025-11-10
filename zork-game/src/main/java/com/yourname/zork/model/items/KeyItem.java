package model.items;

import model.Character;
import model.GameContext;   
import model.rooms.Room; 


public class KeyItem extends Item {

    public KeyItem(String name, String description) {
        super(name, description, ItemType.KEY);
    }

    public void use(GameContext game, Character player) {
        Room current = player.getCurrentRoom();

        if (current.isExitLocked("south")) {
            current.unlockExit("south");
            game.showMessage("You unlock the gate to the south with the key. It swings open with a creak.");
        } else {
            game.showMessage("You jiggle the key in the air, but there’s nothing to unlock here.");
        }
    }
}
