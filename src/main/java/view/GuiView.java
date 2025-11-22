package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.items.Item;
import model.rooms.Room;

import java.util.List;
import java.util.function.Consumer;

public class GuiView implements IGameView {
    private TextArea outputArea;
    private ListView<String> inventoryList;
    private Label roomNameLabel;
    private Label roomDescLabel;
    private TextField inputField;
    
    // Callback to send input back to Controller
    private Consumer<String> inputProcessor;

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // --- TOP: Room Info ---
        VBox topBox = new VBox(5);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 10, 0));
        
        roomNameLabel = new Label("Room Name");
        roomNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        roomDescLabel = new Label("Room Description");
        roomDescLabel.setWrapText(true);
        
        topBox.getChildren().addAll(roomNameLabel, roomDescLabel);
        root.setTop(topBox);

        // --- CENTER: Game Log ---
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        root.setCenter(outputArea);

        // --- RIGHT: Inventory & Arrows ---
        VBox rightBox = new VBox(10);
        rightBox.setPadding(new Insets(0, 0, 0, 10));
        rightBox.setPrefWidth(200);

        Label invLabel = new Label("Inventory");
        invLabel.setStyle("-fx-font-weight: bold;");
        
        inventoryList = new ListView<>();
        inventoryList.setPrefHeight(200);
        
        // Button to use selected item
        Button useBtn = new Button("Use Selected");
        useBtn.setMaxWidth(Double.MAX_VALUE);
        useBtn.setOnAction(e -> {
            String selected = inventoryList.getSelectionModel().getSelectedItem();
            if (selected != null && inputProcessor != null) {
                // Heuristic: try to use the item. 
                // Alternatively, pre-fill the input box: inputField.setText("use " + selected);
                inputProcessor.accept("use " + selected);
            }
        });

        // Directional Arrows
        GridPane arrowGrid = new GridPane();
        arrowGrid.setHgap(5);
        arrowGrid.setVgap(5);
        arrowGrid.setAlignment(Pos.CENTER);
        
        Button btnN = new Button("N");
        Button btnS = new Button("S");
        Button btnE = new Button("E");
        Button btnW = new Button("W");
        
        // Size buttons
        List.of(btnN, btnS, btnE, btnW).forEach(b -> b.setPrefSize(40, 40));

        // Logic for arrows
        btnN.setOnAction(e -> sendCommand("go north"));
        btnS.setOnAction(e -> sendCommand("go south"));
        btnE.setOnAction(e -> sendCommand("go east"));
        btnW.setOnAction(e -> sendCommand("go west"));

        // Add to grid (Column, Row)
        arrowGrid.add(btnN, 1, 0);
        arrowGrid.add(btnW, 0, 1);
        arrowGrid.add(btnE, 2, 1);
        arrowGrid.add(btnS, 1, 2);

        rightBox.getChildren().addAll(invLabel, inventoryList, useBtn, new Separator(), new Label("Move"), arrowGrid);
        root.setRight(rightBox);

        // --- BOTTOM: Input ---
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));
        
        inputField = new TextField();
        inputField.setPromptText("Type command here...");
        HBox.setHgrow(inputField, Priority.ALWAYS);
        
        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(e -> handleSend());
        inputField.setOnAction(e -> handleSend()); // Allow Enter key

        bottomBox.getChildren().addAll(inputField, sendBtn);
        root.setBottom(bottomBox);

        return new Scene(root, 800, 600);
    }

    private void handleSend() {
        String text = inputField.getText();
        if (text != null && !text.isBlank()) {
            inputField.clear();
            sendCommand(text);
        }
    }

    private void sendCommand(String cmd) {
        if (inputProcessor != null) {
            inputProcessor.accept(cmd);
        }
    }

    public void setInputProcessor(Consumer<String> processor) {
        this.inputProcessor = processor;
    }

    // --- IGameView Implementation ---

    @Override
    public void showMessagePrint(String message) {
        // should be console only
    }
    
    @Override
    public void showMessage(String message) {
        // Run on UI thread just in case
        Platform.runLater(() -> outputArea.appendText(message + "\n"));
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
            // Short description for header, long description goes to chat log usually, 
            // but we can update the label too if it's not too long.
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