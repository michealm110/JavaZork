package model.rooms;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import model.GameContext;
import model.Region;

public class SpinnerRoom extends Room {
    public SpinnerRoom(String description, Region region) {
        super(description, region);
    }

    @Override
    public void onEnter(GameContext game) {
        randomizeExits();
        game.showMessage("The walls spin! You lose your sense of direction...");
        game.showMessage(getLongDescription());
    }
}