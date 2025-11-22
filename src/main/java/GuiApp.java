import controller.GameController;
import controller.Parser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Character;
import model.GameTimer;
import model.TurnManager;
import model.WorldBuilder;
import model.rooms.Room;
import view.GuiView;

import java.io.IOException;

public class GuiApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game_view.fxml"));
            Parent root = loader.load();

            GuiView guiView = loader.getController();

            WorldBuilder worldBuilder = new WorldBuilder();
            Room startingRoom = worldBuilder.load("game_data.json");
            Character player = new Character("Player", startingRoom);
            
            GameTimer timer = new GameTimer();
            TurnManager turnManager = new TurnManager();
            Parser parser = new Parser();

            GameController gameController = new GameController(player, parser, guiView, turnManager, timer);

            guiView.setInputProcessor(input -> gameController.handleInput(input));

            Scene scene = new Scene(root);
            primaryStage.setTitle("DeliveryDash - FXML Edition");
            primaryStage.setScene(scene);
            primaryStage.show();

            gameController.startGame();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML file.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}