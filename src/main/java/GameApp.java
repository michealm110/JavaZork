import controller.GameController;
import controller.Parser;
import model.Character;
import model.WorldBuilder;
import model.GameTimer;   
import model.TurnManager; 
import model.rooms.Room;
import view.ConsoleView;  
          


public class GameApp {
    public static void main(String[] args) {
        GameTimer timer = new GameTimer();
        TurnManager turnManager = new TurnManager();
        Parser parser = new Parser();
        ConsoleView view = new ConsoleView();

        WorldBuilder worldBuilder = new WorldBuilder();
        Room startingRoom = worldBuilder.load("game_data.json");

        Character player = new Character("Player", startingRoom);
        GameController game = new GameController(player, parser, view, turnManager, timer);

        game.setWorldMapData(worldBuilder.getRooms(), worldBuilder.getNpcs());
        game.startGame();
    }
}

