package model;

public class Command {
    private String commandWord;
    private String secondWord;

    public Command(String firstWord, String secondWord) {
        this.commandWord = firstWord;
        this.secondWord = secondWord;
    }

    public String getCommandWord() {
        return this.commandWord;
    }

    public String getSecondWord() {
        return this.secondWord;
    }

    public boolean isUnknown() {
        return this.commandWord == null;
    }

    public boolean hasSecondWord() {
        return this.secondWord != null;
    }
}