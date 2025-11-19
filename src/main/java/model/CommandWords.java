package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class CommandWords {
    private Map<String, String> validCommands = new HashMap<>();

    public CommandWords() {
        // Constructor is now empty. 
        // Commands are registered dynamically by the GameController via addCommand.
    }

    public void addCommand(String commandWord, String description) {
        this.validCommands.put(commandWord, description);
    }

    public boolean isCommand(String commandWord) {
        return this.validCommands.containsKey(commandWord);
    }

    public Map<String, String> getCommandMap() {
        return this.validCommands;
    }

    public String[] getAllCommands() {
        String[] commands = this.validCommands.keySet().toArray(new String[0]);
        Arrays.sort(commands);
        return commands;
    }
}