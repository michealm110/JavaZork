package model;

import java.io.Serializable;

public class GameState implements Serializable {
    private Character player;
    private int secondsElapsed;
    private int turnCount;

    public GameState(Character player, int secondsElapsed, int turnCount) {
        this.player = player;
        this.secondsElapsed = secondsElapsed;
        this.turnCount = turnCount;
    }

    public Character getPlayer() { return player; }
    public int getSecondsElapsed() { return secondsElapsed; }
    public int getTurnCount() { return turnCount; }
}