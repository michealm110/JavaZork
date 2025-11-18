package model;

public class Command {
    private String commandWord;
    private String secondWord;
    private String thirdWord;

    public Command(String first, String second, String third) {
        this.commandWord = first;
        this.secondWord = second;
        this.thirdWord = third;
    }

    public String getCommandWord() { return commandWord; }
    public String getSecondWord() { return secondWord; }
    public String getThirdWord() { return thirdWord; }

    public boolean hasSecondWord() { return secondWord != null; }
    public boolean hasThirdWord() { return thirdWord != null; } 
}