package controller;

import model.*;
import model.items.*;
import model.rooms.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.IGameView;

import static org.junit.jupiter.api.Assertions.*;

class GameplayMechanicsTest {

    private GameController controller;
    private Character player;
    private PretendView pretendView;
    private Room startRoom;
    private Room lockedRoom;


    @BeforeEach
    void setUp() {
        //sssssetup a mini-world
        startRoom = new Room("start", "Start Room", "The beginning.");
        lockedRoom = new Room("end", "Locked Room", "You made it inside.");
        
        // created items
        Item key = new KeyItem("key_1", "Gold Key", "Opens the door");
        Item sword = new WeaponItem("sword_1", "Sword", "Sharp", 10);
        
        startRoom.addItem(key);
        startRoom.addItem(sword);

        //create a locked exit
        Exit lockedExit = Exit.createLockedExit(
            lockedRoom, 
            "key_1", 
            "It is locked.", 
            "You unlock the door.", 
            "Wrong key."
        );
        startRoom.setExit(Direction.NORTH, lockedExit);
        
        lockedRoom.setExit(Direction.SOUTH, Exit.createUnlockedExit(startRoom));

        //setup game
        player = new Character("Hero", startRoom);
        mockView = new MockView();
        Parser parser = new Parser();
        TurnManager turnManager = new TurnManager();
        GameTimer timer = new GameTimer();

        controller = new GameController(player, parser, mockView, turnManager, timer);
    }

    @Test
    void testMovementRestrictedByLock() {
        // locked door
        controller.handleInput("go north");

        // player shouldn;'t have moved
        assertEquals(startRoom, player.getCurrentRoom());
        assertEquals("It is locked.", mockView.getLastMessage());
    }

    @Test
    void testTakeItem() {

        assertFalse(startRoom.getItems().isEmpty());
        
        controller.handleInput("take Sword");

        // i   tem shoudl have moved from room to player
        assertTrue(startRoom.getItems().isEmpty());
        assertTrue(player.hasItem("Sword"));
        assertEquals("You picked up the Sword.", mockView.getLastMessage());
    }

    @Test
    void testUnlockAndMove() {
        controller.handleInput("take Gold Key");
        
        controller.handleInput("use Gold Key north");
        assertEquals("You unlock the door.", mockView.getLastMessage());
        
        controller.handleInput("go north");
        
        assertEquals(lockedRoom, player.getCurrentRoom());
    }

    @Test
    void testDropItem() {
        // give player item
        controller.handleInput("take Sword");
        assertTrue(player.hasItem("Sword"));

        // drop it
        controller.handleInput("drop Sword");

        assertFalse(player.hasItem("Sword"));
        assertNotNull(startRoom.removeItemByName("Sword")); // Should find it in room
        assertEquals("You dropped the Sword.", mockView.getLastMessage());
    }

    @Test
    void testInvalidDirection() {
        controller.handleInput("go west");
        assertEquals("There is no door!", mockView.getLastMessage());
        assertEquals(startRoom, player.getCurrentRoom());
    }

    @Test
    void testTurnCounterIncrements() {
        TurnManager tm = new TurnManager();
        tm = new TurnManager();
        controller = new GameController(player, new Parser(), pretendView, tm, new GameTimer());

        assertEquals(0, tm.getTurnCount());
        
        controller.handleInput("help");  //shouhdnt use a turn
        assertEquals(0, tm.getTurnCount());

        controller.handleInput("go west"); // should use a turn
        assertEquals(1, tm.getTurnCount());
    }
    
    @Test
    void testChalkMechanic() {
        // Add chalk to player
        Item chalk = new ChalkItem("chalk", "Chalk", "White chalk");
        player.addItem(chalk);
        
        assertFalse(startRoom.hasChalkMark());
        
        controller.handleInput("use Chalk");
        
        assertTrue(startRoom.hasChalkMark(), "Room should be marked after using chalk");
        assertEquals("You mark the floor with chalk.", mockView.getLastMessage());
    }
}