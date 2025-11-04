package view;

public class ConsoleView {
    public void showCommands(String[] commands) {
        System.out.print("Valid commands are: ");
        for (String command : commands) {
            System.out.print(command + " ");
        }
        System.out.println();
    }
    public void showMessage() {
        System.out.println();
    }
    public void showMessage(String message) {
        System.out.println(message);
    }
}
