package model.rooms;

import model.GameContext;
import model.Region;

public class EndRoom extends Room {

    public EndRoom(String description, Region region) {
        super(description, region);
    }

    @Override
    public void onEnter(GameContext game) {
        game.showMessage("""
        You knock on the door.
        A sleepy man answers, confused.

        "Ah! The delivery! I ordered that ages ago!"

        You check the receipt in your pocket... it’s smudged from the rain.
        You have fought your way across twisty streets, warped spinner alleys,
        and confusing lanes…

        JUST TO DELIVER A BAG OF CHIPS.

        The man hands you a £20 tip.

        You survived the estate.
        You delivered the order.
        You are a hero.

        """);

        game.showMessage("This delivery took you " + game.getSecondsElapsed() + " seconds.");
        game.showMessage("You made " + game.getTurnCount() + " turns.");
        game.showMessage("THE END.");

        game.markFinalRoom();
    }
}