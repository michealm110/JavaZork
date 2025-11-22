package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.items.Item;
import model.rooms.Room;

import java.util.List;
import java.util.function.Consumer;

public class GuiView implements IGameView {

    @FXML private Label roomNameLabel;
    @FXML private Label roomDescLabel;
    @FXML private TextArea outputArea;
    @FXML private ListView<String> inventoryList;
    @FXML private TextField inputField;

    private Consumer<String> inputProcessor;

    @FXML
    public void initialize() {
        outputArea.setFocusTraversable(false); // keep focus on input
    }

    // --- FXML Event Handlers ---

    @FXML
    private void handleSend() {
        String text = inputField.getText();
        if (text != null && !text.isBlank()) {
            inputField.clear();
            processInput(text);
        }
    }

    @FXML
    private void handleUseItem() {
        String selected = inventoryList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            processInput("use " + selected);
        }
    }

    @FXML private void handleNorth() { processInput("go north"); }
    @FXML private void handleSouth() { processInput("go south"); }
    @FXML private void handleEast()  { processInput("go east"); }
    @FXML private void handleWest()  { processInput("go west"); }

    private void processInput(String cmd) {
        if (inputProcessor != null) {
            inputProcessor.accept(cmd);
        }
    }

    public void setInputProcessor(Consumer<String> processor) {
        this.inputProcessor = processor;
    }

    // --- IGameView Implementation ---

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> outputArea.appendText(message + "\n"));
    }

    @Override
    public void showMessagePrint(String message) {
        Platform.runLater(() -> outputArea.appendText(message));
    }

    @Override
    public void showBlankLine() {
        Platform.runLater(() -> outputArea.appendText("\n"));
    }

    @Override
    public void showCommands(String[] commands) {
        Platform.runLater(() -> {
            outputArea.appendText("Valid commands: ");
            for (String cmd : commands) {
                outputArea.appendText(cmd + " ");
            }
            outputArea.appendText("\n");
        });
    }

    @Override
    public void updateRoomInfo(Room room) {
        Platform.runLater(() -> {
            roomNameLabel.setText(room.getName());
            roomDescLabel.setText(room.getDescription());
        });
    }

    @Override
    public void updateInventory(List<Item> items) {
        Platform.runLater(() -> {
            inventoryList.getItems().clear();
            for (Item item : items) {
                inventoryList.getItems().add(item.getName());
            }
        });
    }
}