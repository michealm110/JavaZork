package controller;

import model.Character;
import model.Command;
import model.CommandWords;
import model.items.*;
import model.rooms.*;
import model.GameTimer;
import model.GameContext;
import model.TurnManager;
import model.Direction;
import model.Region;
import view.IGameView;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GameController implements GameContext{
    private Parser parser;
    private Character player;
    private IGameView view;
    private GameTimer timer;
    private TurnManager turnManager;

    private Map<String, Method> commandMethods;
    private Map<String, Boolean> commandTurnConsumption = new HashMap<>();

    // intorduced little bit of state to track whetehr game is ended, got confusing to use return values with annotations/refeleciton
    private boolean finished = false;
    
    public GameController(Character player, Parser parser, IGameView view, TurnManager turnManager, GameTimer timer) {
        this.player = player;
        this.parser = parser;
        this.view = view;
        this.timer = timer;
        this.turnManager = turnManager;
        this.commandMethods = new HashMap<>();

        this.initCommands();
    }

    private void initCommands() {
        //scans THIS CLASS ONLY for methods with @CommandDef annotation
        // and registers them in commandMethods map
        // this replaces the old big switch statement

        CommandWords validCommands = parser.getCommandWords();

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(CommandDef.class)) {
                CommandDef annotation = method.getAnnotation(CommandDef.class);
                String description = annotation.description();
                boolean consumesTurn = annotation.consumesTurn();

                for (String keyword : annotation.value()) {
                    commandMethods.put(keyword, method);
                    commandTurnConsumption.put(keyword, consumesTurn);
                    validCommands.addCommand(keyword, description);
                }
            }
        }
    }


    public void startGame() {
        timer.start();
        this.printWelcome();
        updateGuiState();


        view.showMessage("Thank you for playing. Goodbye.");

        timer.stop();
    }

    
    private void updateGuiState() {
        view.updateRoomInfo(player.getCurrentRoom());
        view.updateInventory(player.getItems());
    }

    private void startConsoleLoop() {
        startGame();
        while (!finished) {
           // Command command = this.parser.getCommand(this);
           // processCommand(command);
        }
        view.showMessage("Thank you for playing. Goodbye.");
        timer.stop();
    }

    private void printWelcome() {
        view.showBlankLine();
        view.showMessage("Welcome to DeliveryDash!");
        view.showMessage("Type 'help' if you need help.");
        view.showBlankLine();
        this.player.getCurrentRoom().onEnter(this);
    }

    public void handleInput(String input) {
        //can we get the console loop here too?
        if (finished) return;
        
        view.showMessage("> " + input);
        Command command = parser.parse(input);
        processCommand(command);
        
        // Refresh GUI state after every command
        updateGuiState();
    }


    private void processCommand(Command command) {
        String commandWord = command.getCommandWord();

        if (commandWord == null) {
            view.showMessage("I don't understand your command...");
            return;
        }

        Method method = commandMethods.get(commandWord);

        if (method != null) {
            // Check if this command consumes a turn
            Boolean consumesTurn = commandTurnConsumption.get(commandWord);
            if (consumesTurn != null && consumesTurn) {
                turnManager.nextTurn();
            }

            try {
                // Invoke the method using reflection
                method.invoke(this, command);
            } catch (Exception e) {
                view.showMessage("Error executing command: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            view.showMessage("I don't know what you mean...");
        }
    }

    @CommandDef(value = {"quit", "exit"}, description = "End the game", consumesTurn = false)
    public void executeQuit(Command command) {
        if (command.hasSecondWord()) {
            view.showMessage("Quit what?");
            return;
        }  else {
            finished = true;
        }
    }

    @CommandDef(value = {"inventory", "inv"}, description = "List current inventory", consumesTurn = false)
    public void executeInventory(Command command) {
        player.listItems(this);
    }

    @CommandDef(value = {"use"}, description = "Use an item from inventory", consumesTurn = true)
    private void useItem(Command command) {
        if (!command.hasSecondWord()) {
            view.showMessage("Use what?");
            return;
        }

        String itemName = command.getSecondWord();
        String target = command.getThirdWord();

        Item item = player.getItemFromInventory(itemName);

        if (item == null) {
            view.showMessage("You don't have a '" + itemName + "'.");
            return;
        }

        //polymorphic use item call
        item.use(this, player, target);
    }
     
    @CommandDef(value = {"take", "pickup"}, description = "Pick up an item")
    private void takeItem(Command command) {
        if (!command.hasSecondWord()) {
            view.showMessage("Take what?");
            return;
        }
        String itemName = command.getSecondWord();
        Room current = player.getCurrentRoom();
        Item item = current.removeItemByName(itemName);
        if (item == null) {
            view.showMessage("There is no '" + itemName + "' here.");
            return;
        }
        player.addItem(item);
        view.showMessage("You picked up the " + itemName + ".");
    }

    @CommandDef(value = {"help"}, description = "Show help", consumesTurn = false)
    private void printHelp(Command command) {
        view.showMessage("You are lost. You are alone. You wander around the streets.");
        String[] commands = parser.getCommandWords().getAllCommands();
        view.showCommands(commands);

    }

    @CommandDef(value = {"drop"}, description = "Drop an item")
    private void removeItem(Command command) {
        if (!command.hasSecondWord()) {
            view.showMessage("Drop what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.removeItem(itemName);

        if (item == null) {
            view.showMessage("You don't have a '" + itemName + "'.");
        } else {
            player.getCurrentRoom().addItem(item);
            view.showMessage("You dropped the " + item.getName() + ".");
        }
    }
    
    @CommandDef(value = {"go", "walk", "move"}, description = "Move to another room")
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            view.showMessage("Go where?");
            return;
        }
        String directionWord = command.getSecondWord();
        Direction direction;
        try {
            direction = Direction.fromString(directionWord);
        } catch (model.InvalidDirectionException e) {
            view.showMessage(e.getMessage());
            return;
        }

        Room current = this.player.getCurrentRoom();
        Exit exit = current.getExit(direction);

        if (exit == null) {
            view.showMessage("There is no door!");
            return;
        }

        if (exit.isLocked()) {
            view.showMessage("A locked gate blocks your way to the " + direction.getText() + ".");
            return;
        }

        //Region fromRegion = current.getRegion();
        //Region toRegion   = nextRoom.getRegion();

        Room nextRoom = exit.getTargetRoom();
        this.player.setCurrentRoom(nextRoom);

        // region change
        //if (fromRegion != toRegion) {
        //    showRegionTransition(fromRegion, toRegion);
        //}

        // polymorphic call to onEnter
        nextRoom.onEnter(this);

    }

    @CommandDef(value = {"time"}, description = "Show elapsed time in seconds", consumesTurn = false)
    public void showTime(Command command) {
        view.showMessage("Time elapsed: " + timer.getSecondsElapsed() + " seconds.");
    }

    @CommandDef(value = {"turns"}, description = "Show number of turns taken", consumesTurn = false)
    public void showTurns(Command command) {
        view.showMessage("Turns taken: " + turnManager.getTurnCount() + ".");
    }

    // Placeholder method kept from original code
    @CommandDef(value = {"open"}, description = "Open something")
    public void openSomething(Command command) {
        view.showMessage("Open what?");
    }
    
    private void showRegionTransition(Region from, Region to) {
        switch (to) {
            case STREETS:
                view.showMessage("You leave the warm glow of the takeaway and step out onto the street.");
                break;
            case PARK:
                view.showMessage("You leave the busy street behind and enter the quiet park.");
                break;
            case ESTATE:
                view.showMessage("You step into the estate. Rows of identical houses close in around you.");
                break;
            case TAKEAWAY:
                view.showMessage("You head back towards the familiar smell of fried food.");
                break;
        }
    }

    // GameContext methods for encapuslation
    @Override
    public void showBlankLine() {
        view.showBlankLine();
    }
    @Override
    public void showMessage(String message) {
        view.showMessage(message);
    }
    @Override
    public void showMessagePrint(String message) {
        view.showMessagePrint(message);
    }
    @Override
    public int getSecondsElapsed() {
        return timer.getSecondsElapsed();
    }
    @Override
    public int getTurnCount() {
        return turnManager.getTurnCount();
    }
    @Override
    public boolean keyInInventory() {
        return player.hasItem("Key");
    }

}