package view;

import model.items.Item;
import model.rooms.Room;
import java.util.ArrayList;
import java.util.List;

public class PretendView implements IGameView {
    public List<String> messages = new ArrayList<>();

    @Override
    public void showMessage(String message) {
        //just add them o a  list so we can check them later
        messages.add(message);
    }

    @Override
    public void showMessagePrint(String message) {
        messages.add(message);
    }

    @Override public void showBlankLine() {}
    @Override public void showCommands(String[] commands) {}
    @Override public void updateRoomInfo(Room room) {}
    @Override public void updateInventory(List<Item> inventory) {}
    
    public String getLastMessage() {
        if (messages.isEmpty()) return "";
        return messages.get(messages.size() - 1);
    }
    
    public void clearLog() {
        messages.clear();
    }
}