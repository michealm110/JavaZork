package model.rooms;

public class Exit {
    private Room targetRoom;
    private boolean isLocked;
    private String key;
    private String message;
    private String messageUnlock;
    private String messageUnlockFail;

    //should we make polymorphic exits for different types of locks?
    //for now, i'll make 2 static factory methods

    public static Exit createLockedExit(Room targetRoom, String key, String message, String messageUnlock, String messageUnlockFail) {
         Exit exit = new Exit(targetRoom, true, key, message, messageUnlock, messageUnlockFail);
         return exit;
    }

    public static Exit createUnlockedExit(Room targetRoom) {
        Exit exit = new Exit(targetRoom, false, null, null, null, null);
        return exit;
    }

    private Exit(Room room, boolean isLocked, String key, String message, String messageUnlock, String messageUnlockFail) {
        this.targetRoom = room;
        this.isLocked = isLocked;
        this.key = key;
        this.message = message;
        this.messageUnlock = messageUnlock;
        this.messageUnlockFail = messageUnlockFail;
    }

    public Room getTargetRoom() {
        return targetRoom;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean unlock(String key_id) {
        if (this.key.equals(key_id)) {
            this.isLocked = false;
            return true;
        } else {
            return false;
        }
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageUnlock() {
        return messageUnlock;
    }
    public String getMessageUnlockFail() {
        return messageUnlockFail;
    }
}
