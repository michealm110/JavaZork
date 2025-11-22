package view;

import java.util.List;

import model.rooms.Room;
import model.items.Item;


public class ConsoleView implements IGameView{

    @Override
    public void showCommands(String[] commands) {
        System.out.print("Valid commands are: ");
        for (String command : commands) {
            System.out.print(command + " ");
        }
        System.out.println();
    }

    @Override
    public void showBlankLine() {
        System.out.println();
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showMessagePrint(String message) {
        System.out.print(message);
    }
    
    @Override
    public void updateRoomInfo(Room room) {
        // In console, we usually just print the description when we enter
        // This can be left empty or used to reprint the room desc
    }

    @Override
    public void updateInventory(List<Item> inventory) {
        // In console, inventory is usually shown only on command
    }
}
