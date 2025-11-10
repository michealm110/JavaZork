import controller.GameController;
import controller.Parser;
import model.Character;
import model.WorldBuilder;
import model.GameTimer;   
import model.TurnManager; 
import view.ConsoleView;  
          


public class GameApp {
    public static void main(String[] args) {
        GameTimer timer = new GameTimer();
        TurnManager turnManager = new TurnManager();
        Parser parser = new Parser();
        ConsoleView view = new ConsoleView();

        Character player = WorldBuilder.createPlayerAndWorld();

        GameController game = new GameController(player, parser, view, turnManager, timer);
        
        game.startGame();
    }
}

