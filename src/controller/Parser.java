package controller;

import java.util.Scanner;
import model.Command;
import model.CommandWords;

public class Parser {
    private CommandWords commands = new CommandWords();
    private Scanner reader;

    public Parser() {
        this.reader = new Scanner(System.in);
    }

    public Command getCommand() {
        System.out.print("> ");
        String inputLine = this.reader.nextLine();
        String word1 = null;
        String word2 = null;
        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next();
            }
        }

        return this.commands.isCommand(word1) ? new Command(word1, word2) : new Command((String)null, word2);
    }

    public CommandWords getCommandWords() {
        return this.commands;
    }
}