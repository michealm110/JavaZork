package model;

import java.io.Serializable;

public class NPC implements Serializable{
    private final String id;
    private final String name;
    private final String description;
    private final String conversationPiece;
    private String currentRoomId;
    private final boolean isHostile;

    public NPC(String id, String name, String description, String converstationPiece, String currentRoomId, boolean isHostile) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.conversationPiece = converstationPiece;
        this.currentRoomId = currentRoomId;
        this.isHostile = isHostile;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCurrentRoomId() { return currentRoomId; }
    public boolean isHostile() { return isHostile; }
    public String getConversationPiece() { return conversationPiece; }

    public void setCurrentRoomId(String roomId) {
        this.currentRoomId = roomId;
    }
}