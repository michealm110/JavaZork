package model;

public class TurnManager {
    private int turnCount;

    public TurnManager() {
        this.turnCount = 0;
    }

    public void nextTurn() {
        this.turnCount++;
    }

    public int getTurnCount() {
        return this.turnCount;
    }
}