package controller;

import model.Character;
import model.Command;
import model.CommandWords;   
import model.Item;
import model.rooms.Room;
import model.rooms.SpinnerRoom;
import model.rooms.EndRoom;
import model.ZorkUL;
import model.GameTimer;
import model.GameContext;
import model.TurnManager;
import view.ConsoleView;

public class GameController implements GameContext{
    private Parser parser;
    private Character player;
    private ConsoleView view;
    private GameTimer timer;
    private TurnManager turnManager;
    private boolean finalRoom = false;

    public GameController(Character player, Parser parser, ConsoleView view, TurnManager turnManager, GameTimer timer) {
        this.player = player;
        this.parser = parser;
        this.view = view;
        this.timer = timer;
        this.turnManager = turnManager;
    }


    public void startGame() {
        timer.start();

        this.printWelcome();

        Command command;
        for(boolean finished = false; !finished; finished = this.processCommand(command)) {
            command = this.parser.getCommand();
        }

        view.showMessage("Thank you for playing. Goodbye.");

        timer.stop();
    }
    private void pickUpPint() {}
    private void openChest() {}

    private void printWelcome() {
        view.showMessage();
        view.showMessage("Welcome to DeliveryDash!");
        view.showMessage("Type 'help' if you need help.");
        view.showMessage();
        this.player.getCurrentRoom().onEnter(this);
    }

    private boolean consumesTurn(String commandWord) {
        switch (commandWord) {
            case "inventory":
            case "help":
            case "time":
            case "turns":
                return false;
            default:
                return true;
        }
    }

    private boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();
        if (commandWord == null) {
            view.showMessage("I don't understand your command...");
            return false;
        } else {
            if (consumesTurn(commandWord)) {
                turnManager.nextTurn();
            }
            switch (commandWord) {
                case "inventory":
                    player.listItems();
                    break;
                case "help":
                    printHelp();
                    break;
                case "go":
                    goRoom(command);
                    if (finalRoom) {
                        return true;
                    }
                    break;
                case "quit":
                    if (command.hasSecondWord()) {
                        view.showMessage("Quit what?");
                        return false;
                    } 
                    return true;
                case "open":
                    openChest();
                    break;
                case "take":
                    takeItem(command);
                    break;
                case "drop":
                    removeItem(command);
                    break;
                case "mark":
                    markRoom(command);
                    break;
                case "time":
                    view.showMessage("Time elapsed: " + timer.getSecondsElapsed() + " seconds.");
                    break;
                case "turns":
                    view.showMessage("Turns taken: " + turnManager.getTurnCount() + ".");
                    break;
                default:
                    view.showMessage("I don't know what you mean...");
                    break;
            }

            return false;
        }
    }

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



    private void printHelp() {
        view.showMessage("You are lost. You are alone. You wander around the streets.");
        String[] commands = parser.getCommandWords().getAllCommands();
        view.showCommands(commands);

    }

    public void removeItem(Command command) {
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

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            view.showMessage("Go where?");
            return;
        } 
        String direction = command.getSecondWord();
        Room current = this.player.getCurrentRoom();
        Room nextRoom = current.getExit(direction);

        if (nextRoom == null) {
            view.showMessage("There is no door!");
            return;
        }
        this.player.setCurrentRoom(nextRoom);

        // polymorphic call to onEnter
        nextRoom.onEnter(this);
     
    }
    

    public void markRoom(Command command) {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.isMarked()) {
            view.showMessage("This room is already marked with chalk.");
        } else {
            currentRoom.mark(command.getSecondWord());
            view.showMessage("You have marked this room with chalk with the message " + command.getSecondWord() + ".");
        }
    }

    // GameContext methods for encapuslation
    @Override
    public void showMessage(String message) {
        view.showMessage(message);
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
    public void markFinalRoom() {
        finalRoom = true;
    }

}