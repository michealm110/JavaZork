package view;

import java.util.List;
import model.items.Item;
import model.rooms.Room;

public interface IGameView {
    void showMessage(String message);
    void showBlankLine();

    void showCommands(String[] commands);
    void showMessagePrint(String message); 

    // GUI-only methods:
    void updateRoomInfo(Room room);
    void updateInventory(List<Item> inventory);
}