package model;

import java.util.HashMap;
import java.util.Map;

public class CommandWords {
    private Map<String, String> validCommands = new HashMap();

    public CommandWords() {
        this.validCommands.put("go", "Move to another room");
        this.validCommands.put("quit", "End the game");
        this.validCommands.put("help", "Show help");
        this.validCommands.put("look", "Look around");
        this.validCommands.put("eat", "Eat something");
        this.validCommands.put("leave", "Exit the room you are in");
        this.validCommands.put("inventory", "List current inventory");
        this.validCommands.put("take", "Pick up an item");
        this.validCommands.put("drop", "Drop the item");
    }

    public boolean isCommand(String commandWord) {
        return this.validCommands.containsKey(commandWord);
    }
    public Map<String, String> getCommandMap() {
        return this.validCommands;
    }
    public String[] getAllCommands() {
        return this.validCommands.keySet().toArray(new String[0]);
    }
}