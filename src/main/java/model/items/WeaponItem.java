package model.items;

import model.GameContext;
import model.Character;

public class WeaponItem extends Item {
    private int damage;

    public WeaponItem(String id, String name, String description, int damage) {
        super(id, name, description, true, ItemType.WEAPON);
        this.damage = damage;
    }

    @Override
    public void use(GameContext context, Character player, String target) {
        // In the future, this could look for an NPC in the room to attack
        context.showMessage("You swing the " + getName() + " menacingly!");
        context.showMessage("It looks like it could deal " + damage + " damage.");
    }
}