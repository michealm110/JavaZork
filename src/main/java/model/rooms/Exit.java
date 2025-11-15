package model.rooms;

public class Exit {
    private Room targetRoom;
    private boolean isLocked;
    private String key;
    private String message;

    //should we make polymorphic exits for different types of locks?
    //for now, i'll make 2 static factory methods

    public static Exit createLockedExit(Room targetRoom, String key, String message) {
        Exit exit = new Exit(targetRoom.getId(), true, key, message);
        return exit;
    }

    public static Exit createUnlockedExit(Room targetRoom) {
        Exit exit = new Exit(targetRoom.getId(), false, null, null);
        return exit;
    }

    private Exit(String targetRoomId, boolean isLocked, String key, String message) {
        this.targetRoom = null; // to be set later
        this.isLocked = isLocked;
        this.key = key;
        this.message = message;
    }

    public Room getTargetRoom() {
        return targetRoom;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void unlock() {
        this.isLocked = false;
    }
    
}
