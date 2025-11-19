package model;

public interface GameContext{
    void showBlankLine();
    void showMessage(String message);
    void showMessagePrint(String message);
    int getSecondsElapsed();
    int getTurnCount();
    boolean keyInInventory();
}