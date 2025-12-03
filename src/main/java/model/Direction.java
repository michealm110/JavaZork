package model;

public enum Direction {
    NORTH("north", "n"),
    SOUTH("south", "s"),
    EAST("east",  "e"),
    WEST("west",  "w");

    private final String text;
    private final String shortText;

    Direction(String text, String shortText) {
        this.text = text;
        this.shortText = shortText;
    }

    public String getText() {
        return text;
    }

    public static Direction fromString(String value) throws InvalidDirectionException {
        if (value == null) {
            throw new InvalidDirectionException("null is not a valid direction.");
        }

        value = value.trim().toLowerCase();
        if (value.isEmpty()) {
            throw new InvalidDirectionException("empty string is not a valid direction.");
        }

        for (Direction d : values()) {
            if (d.text.equals(value) || d.shortText.equals(value)) {
                return d;
            }
        }

        throw new InvalidDirectionException("Unrecognised direction: \"" + value + "\"");
    }

    public Direction getOpposite() {
        switch(this) {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case EAST: return WEST;
            case WEST: return EAST;
            default: return null; // Should not happen
        }
    }
}
