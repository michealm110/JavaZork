import controller.GameController;
import controller.Parser;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Character;
import model.GameTimer;
import model.TurnManager;
import model.WorldBuilder;
import model.rooms.Room;
import view.GuiView;

public class GuiApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        WorldBuilder worldBuilder = new WorldBuilder();
        Room startingRoom = worldBuilder.load("game_data.json");
        Character player = new Character("Player", startingRoom);
        
        GameTimer timer = new GameTimer();
        TurnManager turnManager = new TurnManager();
        Parser parser = new Parser();

        GuiView guiView = new GuiView();

        GameController gameController = new GameController(player, parser, guiView, turnManager, timer);

        // When GUI sends input, pass it to gameController
        guiView.setInputProcessor(input -> gameController.handleInput(input));

        primaryStage.setTitle("DeliveryDash - GUI Edition");
        primaryStage.setScene(guiView.createScene());
        primaryStage.show();

        gameController.startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}