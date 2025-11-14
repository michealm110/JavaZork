package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void testFullDirectionNames() throws InvalidDirectionException {
        assertEquals(Direction.NORTH, Direction.fromString("north"));
        assertEquals(Direction.SOUTH, Direction.fromString("SOUTH"));
        assertEquals(Direction.EAST,  Direction.fromString("EaSt"));
        assertEquals(Direction.WEST,  Direction.fromString("west"));
    }

    @Test
    void testShortDirectionNames() throws InvalidDirectionException {
        assertEquals(Direction.NORTH, Direction.fromString("n"));
        assertEquals(Direction.SOUTH, Direction.fromString("S"));
        assertEquals(Direction.EAST,  Direction.fromString("e"));
        assertEquals(Direction.WEST,  Direction.fromString("W"));
    }

    @Test
    void testInvalidInputsThrowException() {
        assertThrows(InvalidDirectionException.class,
                () -> Direction.fromString(null));
        assertThrows(InvalidDirectionException.class,
                () -> Direction.fromString(""));
        assertThrows(InvalidDirectionException.class,
                () -> Direction.fromString(" "));
        assertThrows(InvalidDirectionException.class,
                () -> Direction.fromString("nope"));
        assertThrows(InvalidDirectionException.class,
                () -> Direction.fromString("x"));
        assertThrows(InvalidDirectionException.class,
                () -> Direction.fromString("north-east"));
    }
}
