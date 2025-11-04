import controller.GameController;
import controller.Parser;
import model.Character;
import model.WorldBuilder;
import view.ConsoleView;                


public class GameApp {
    public static void main(String[] args) {
        Character player = WorldBuilder.createPlayerAndWorld();
        Parser parser = new Parser();
        ConsoleView view = new ConsoleView();

        GameController game = new GameController(player, parser, view);
        game.startGame();
    }
}

