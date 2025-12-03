package controller;

import model.Character;
import model.Direction;
import model.NPC;
import model.WanderingNPC;
import model.rooms.Exit;
import model.rooms.Room;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WanderingNPCMoverThread implements Runnable {

    private final Map<String, Room> allRooms;
    private final List<NPC> allNpcs;
    private final GameController gameController; 
    private final Character player;
    private volatile boolean running = true;
    private final Random random = new Random();

    public WanderingNPCMoverThread (GameController controller, Map<String, Room> rooms, List<NPC> npcs, Character player) {
        this.gameController = controller;
        this.allRooms = rooms;
        this.allNpcs = npcs;
        this.player = player;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Wait between 20 and 60 seconds before moving NPCs
                int delay = 20000 + random.nextInt(40000);
                Thread.sleep(delay);

                if (!running) break;

                moveWanderers();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    private void moveWanderers() {
        for (NPC npc : allNpcs) {
            if (npc instanceof WanderingNPC) {
                moveOneNpc(npc);
            }
        }
    }

    private void moveOneNpc(NPC npc) {
        Room currentRoom = allRooms.get(npc.getCurrentRoomId());
        if (currentRoom == null) return;

        Direction direction = currentRoom.getRandomExitDirection();
        if (direction == null) return; // trapped, should't happen

        Exit exit = currentRoom.getExit(direction);
        Room targetRoom = exit.getTargetRoom();

        currentRoom.removeNPC(npc);
        targetRoom.addNPC(npc);

        npc.setCurrentRoomId(targetRoom.getId());

        Room playerRoom = player.getCurrentRoom();

        if (playerRoom == currentRoom) {
            gameController.showMessage(npc.getName() + " wanders off to the " + direction.getText() + ".");
        } 
        else if (playerRoom == targetRoom) {
            gameController.showMessage(npc.getName() + " walks in from the " + direction.getOpposite().getText() + ".");
        }
    }
}