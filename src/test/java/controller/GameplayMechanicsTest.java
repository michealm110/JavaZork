package controller;

import model.*;
import model.Character;
import model.items.*;
import model.rooms.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.IGameView;
import view.PretendView;

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
        Item key = new KeyItem("key_1", "GoldKey", "Shiny gold key");
        Item sword = new WeaponItem("sword_1", "Sword", "A big broadsword", 10);

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
        pretendView= new PretendView();
        Parser parser = new Parser();
        TurnManager turnManager = new TurnManager();
        GameTimer timer = new GameTimer();

        controller = new GameController(player, parser, pretendView, turnManager, timer);
    }

    @Test
    void testMovementLockedDoor() {
        // locked door
        controller.handleInput("go north");

        // player shouldn;'t have moved
        assertEquals(startRoom, player.getCurrentRoom());
        assertEquals("It is locked.", pretendView.getLastMessage());
    }

    @Test
    void testTakeItem() {

        assertEquals(2, startRoom.getItems().size());
        controller.handleInput("take Sword");

        // i   tem shoudl have moved from room to player
        assertEquals(1, startRoom.getItems().size());
        assertTrue(player.hasItem("Sword"));
        assertEquals("You picked up the Sword.", pretendView.getLastMessage());
    }

    @Test
    void testUnlockAndMove() {
        controller.handleInput("take GoldKey");

        controller.handleInput("use GoldKey north");
        assertEquals("You unlock the door.", pretendView.getLastMessage());

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
        assertEquals("You dropped the Sword.", pretendView.getLastMessage());
    }

    @Test
    void testInvalidDirection() {
        controller.handleInput("go west");
        assertEquals("There is no door!", pretendView.getLastMessage());
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
    void testChalkMechanic() throws InventoryFullException{
        // Add chalk to player
        Item chalk = new ChalkItem("chalk", "Chalk", "White chalk");
        player.addItem(chalk);

        assertFalse(startRoom.hasChalkMark());

        controller.handleInput("use Chalk");

        assertTrue(startRoom.hasChalkMark(), "Room should be marked after using chalk");
        assertEquals("You mark the floor with chalk.", pretendView.getLastMessage());
    }
}
