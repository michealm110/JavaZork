package model;

public class WorldBuilder {
    // Implementation for building the game world
    public static Character createPlayerAndWorld() {
        // Create rooms
        Room outside = new Room("outside the main entrance of the university");
        Room theatre = new Room("in a lecture theatre");
        Room pub = new Room("in the campus pub");
        Room lab = new Room("in a computing lab");
        Room office = new Room("in the computing admin office");
        Room toilet = new Room("in the toilet");
        // Create items
        Item key = new Item("Key", "A large golden key.");
        Item fountain = new Item("Fountain", "A marble fountain with clear water.");
        Item shovel = new Item("Shovel", "A sturdy shovel, slightly muddy.");
        Item pint = new Item("Pint", "A pint. ");
        Item chest = new Item("Chest", "Chest in the corner");

        // Place items in rooms
        toilet.addItem(key);
        toilet.addItem(fountain);
        toilet.addItem(shovel);

        pub.addItem(pint);
        pub.addItem(chest);


        // Define room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        theatre.setExit("west", outside);
        pub.setExit("east", outside);
        lab.setExit("north", outside);
        lab.setExit("east", office);
        office.setExit("west", lab);
        toilet.setExit("north", pub);
        toilet.setExit("east", lab);
        pub.setExit("south", toilet);
        lab.setExit("south", toilet);

        Character player = new Character("player", outside);
        return player;
    }

}