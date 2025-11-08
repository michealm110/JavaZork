package model;

public interface GameContext{
    void showMessage(String message);
    int getSecondsElapsed();
    int getTurnCount();
    void markFinalRoom();
}