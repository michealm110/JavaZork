package model;

import model.rooms.*;
import model.items.*;

public class WorldBuilder {

    public static Character createPlayerAndWorld() {

        Room takeawayFront = new Room(
                "outside the front door of Kingly Kebab, neon light spilling onto the wet pavement.",
                Region.TAKEAWAY
        );

        Room staffAlley = new Room(
                "in the narrow staff alley behind the takeaway. Bins, crates, and the smell of old chips.",
                Region.TAKEAWAY
        );

        Room stockRoom = new Room(
                "in the cramped stock room: boxes of sauces, spare menus, and random junk piled everywhere.",
                Region.TAKEAWAY
        );

        Room mainStreetWest = new Room(
                "on Main Street West. Shuttered shops and parked cars line the road.",
                Region.STREETS
        );

        Room mainStreetEast = new Room(
                "on Main Street East, closer to the estates. An off-licence hums with low music nearby.",
                Region.STREETS
        );

        Room parkEntrance = new Room(
                "at the entrance to a dimly lit park. Trees arch overhead and a path winds into the darkness.",
                Region.PARK
        );

        Room darkPark = new Room(
                "deep in the park. It’s so dark that without extra light you feel like you could get lost.",
                Region.PARK
        );

        Room estateEntrance = new Room(
                "at the entrance to Crooked Lane estate. Identical houses sprawl in every direction.",
                Region.ESTATE
        );

        Room twistyJunction = new Room(
                "at a junction of narrow estate roads. Every corner looks weirdly similar.",
                Region.ESTATE
        );

        Room northStreet = new Room(
                "on a silent residential street. The same type of semi-detached houses repeat endlessly.",
                Region.ESTATE
        );

        Room deepStreet = new Room(
                "on a deeper stretch of road in the estate. It feels like you are further from civilisation.",
                Region.ESTATE
        );

        Room dizzyStreet = new SpinnerRoom(
                "at a strange corner where the houses seem to shift when you’re not looking.",
                Region.ESTATE
        );

        Room shadowAlley = new Room(
                "in a narrow alley between houses. It’s quiet, and you see your shadow in each window.",
                Region.ESTATE
        );

        Room southStreet = new Room(
                "on a street ending at a tall metal gate to the south. A small sign reads ‘Private walkway’.",
                Region.ESTATE
        );

        Room gardenPath = new Room(
                "on a narrow garden path that winds behind the houses. A single house light glows ahead.",
                Region.ESTATE
        );

        Room customerHouse = new EndRoom(
                "outside No. 14 Crooked Lane, your customer’s house. Warm light spills from behind the curtains.",
                Region.ESTATE
        );

        Item key = new KeyItem(
                "Key",
                "A large golden key with ‘CL’ scratched into it. It probably opens something in the estate."
        );

        Item chalk = new ChalkItem(
                "Chalk",
                "A stub of white chalk. Perfect for marking walls so you don’t get lost."
        );

        stockRoom.addItem(key);   
        darkPark.addItem(chalk);  

        takeawayFront.setExit("south", staffAlley);
        staffAlley.setExit("north", takeawayFront);

        staffAlley.setExit("east", stockRoom);
        stockRoom.setExit("west", staffAlley);

        takeawayFront.setExit("east", mainStreetWest);
        mainStreetWest.setExit("west", takeawayFront);

        mainStreetWest.setExit("east", mainStreetEast);
        mainStreetEast.setExit("west", mainStreetWest);

        mainStreetEast.setExit("north", parkEntrance);
        parkEntrance.setExit("south", mainStreetEast);

        parkEntrance.setExit("north", darkPark);
        darkPark.setExit("south", parkEntrance);

        mainStreetEast.setExit("south", estateEntrance);
        estateEntrance.setExit("north", mainStreetEast);

        estateEntrance.setExit("east", twistyJunction);
        twistyJunction.setExit("west", estateEntrance);

        twistyJunction.setExit("north", northStreet);
        northStreet.setExit("south", twistyJunction);

        northStreet.setExit("east", deepStreet);
        deepStreet.setExit("west", northStreet);

        deepStreet.setExit("south", dizzyStreet);
        dizzyStreet.setExit("north", deepStreet);

        dizzyStreet.setExit("south", shadowAlley);
        shadowAlley.setExit("north", dizzyStreet);

        twistyJunction.setExit("south", southStreet);
        southStreet.setExit("north", twistyJunction);

        southStreet.setExit("south", gardenPath);
        southStreet.setLockedExit("south");  

        gardenPath.setExit("north", southStreet);
        gardenPath.setExit("east", customerHouse);
        customerHouse.setExit("west", gardenPath); 

        Character player = new Character("Player", takeawayFront);
        return player;
    }
}