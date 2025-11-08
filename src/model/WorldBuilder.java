package model;

import java.util.Random;
import model.rooms.Room;
import model.rooms.EndRoom;
import model.rooms.SpinnerRoom;

public class WorldBuilder {

    public static Character createPlayerAndWorld() {
        Room takeawayFront = new Room("outside the front door of the Chinese takeaway, the Golden Elephant");
        Room customerHouse = new EndRoom("outside the house of your customer, your final delivery destination");

        // puzzle rooms
      
      

        // create maze rooms
        Room twistyJunction = new Room("in a maze of twisty dark streets, all alike");
        Room northStreet = new Room("in a maze of twisty dark streets, all alike");
        Room deepStreet = new Room("in a maze of twisty dark streets, all alike");
        Room gardenPath = new Room("in a maze of twisty dark streets, all alike");
        Room southStreet = new Room("in a maze of twisty dark streets, all alike");
        Room dizzyStreet = new SpinnerRoom("in a maze of twisty dark streets, all alike (you feel dizzy here)");
        Room shadowAlley = new Room("in a maze of twisty dark streets, all alike");
        Room storageShed = new Room("in a maze of twisty dark streets, all alike");
        // create items
        Item key = new Item("Key", "A large golden key — could it unlock something ahead?");
        Item fountain = new Item("Fountain", "A marble fountain trickling faintly in the corner.");
        Item shovel = new Item("Shovel", "A sturdy shovel, slightly muddy.");
        Item pint = new Item("Pint", "A refreshing pint of stout.");
        Item chest = new Item("Chest", "An old wooden chest with iron bands.");
        Item compass = new Item("Compass", "A small brass compass that points roughly west.");
        Item chalk = new Item("Chalk", "A piece of white chalk — maybe you can mark the walls with it.");
        Item map = new Item("Map", "A tattered hand-drawn map showing a house to the west.");

        // add items to rooms
        //puzzle.addItem(key);
        //puzzle.addItem(fountain);
        //puzzle.addItem(shovel);
        //puzzle2.addItem(pint);
        //puzzle2.addItem(chest);

        //maze1.addItem(compass);
        deepStreet.addItem(chalk);
        //maze4.addItem(map);

        takeawayFront.setExit("west", storageShed);
        storageShed.setExit("east", takeawayFront);

        takeawayFront.setExit("east", twistyJunction);
        twistyJunction.setExit("west", takeawayFront);

        twistyJunction.setExit("north", northStreet);
        northStreet.setExit("south", twistyJunction);

        northStreet.setExit("west", deepStreet);
        deepStreet.setExit("east", northStreet);

        deepStreet.setExit("south", dizzyStreet);
        dizzyStreet.setExit("north", deepStreet);

        dizzyStreet.setExit("south", shadowAlley);
        shadowAlley.setExit("north", dizzyStreet);

        twistyJunction.setExit("south", southStreet);
        southStreet.setExit("north", twistyJunction);

        southStreet.setExit("south", gardenPath);
        gardenPath.setExit("north", southStreet);

        gardenPath.setExit("east", customerHouse);
        customerHouse.setExit("west", gardenPath);

        Character player = new Character("Player", takeawayFront);
        return player;
    }
}