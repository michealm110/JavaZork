package controller;

import model.Character;
import model.Command;
import model.CommandWords;   
import model.Item;
import model.Room;
import model.ZorkUL;
import view.ConsoleView;

public class GameController {
    private Parser parser;
    private Character player;
    private ConsoleView view;

    public GameController(Character player, Parser parser, ConsoleView view) {
        this.player = player;
        this.parser = parser;
        this.view = view;
    }


    public void startGame() {
        this.printWelcome();

        Command command;
        for(boolean finished = false; !finished; finished = this.processCommand(command)) {
            command = this.parser.getCommand();
        }

        view.showMessage("Thank you for playing. Goodbye.");
    }
    private void pickUpPint() {}
    private void openChest() {}

    private void printWelcome() {
        view.showMessage();
        view.showMessage("Welcome to the University adventure!");
        view.showMessage("Type 'help' if you need help.");
        view.showMessage();
        view.showMessage(this.player.getCurrentRoom().getLongDescription());
    }

    private boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();
        if (commandWord == null) {
            view.showMessage("I don't understand your command...");
            return false;
        } else {
            switch (commandWord) {
                case "inventory":
                    player.listItems();
                    break;
                case "help":
                    printHelp();
                    break;
                case "go":
                    goRoom(command);
                    break;
                case "quit":
                    if (command.hasSecondWord()) {
                        view.showMessage("Quit what?");
                        return false;
                    }
                case "open":
                    openChest();
                    break;
                case "take":
                    takeItem(command);
                    break;
                case "drop":
                    removeItem(command);
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

        player.addItem(item);
        current.removeItemByName(itemName);
        view.showMessage("You picked up the " + itemName + ".");
    }



    private void printHelp() {
        view.showMessage("You are lost. You are alone. You wander around the university.");
        view.showMessage("Your command words are: ");
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
        } else {
            String direction = command.getSecondWord();
            Room nextRoom = this.player.getCurrentRoom().getExit(direction);
            if (nextRoom == null) {
                view.showMessage("There is no door!");
            } else {
                this.player.setCurrentRoom(nextRoom);
                view.showMessage(this.player.getCurrentRoom().getLongDescription());
            }

        }
    }
}