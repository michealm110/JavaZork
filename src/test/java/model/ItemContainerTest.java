package model;

import model.items.Item;
import model.items.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemContainerTest {

    static class TestItem extends Item {
        public TestItem(String name) {
            super("id_" + name, name, "A test item", true, ItemType.GENERIC);
        }

        @Override
        public void use(GameContext context, Character player, String target) {
            // do nothingjk
        }
    }

    private ItemContainer<Item> container;
    private TestItem sword;
    private TestItem shield;
    private TestItem potion;

    @BeforeEach
    void setUp() {
        container = new ItemContainer<>(2);
        sword = new TestItem("Sword");
        shield = new TestItem("Shield");
        potion = new TestItem("Potion");
    }

    @Test
    void testAddAndSize() throws InventoryFullException {
        assertEquals(0, container.size(), "Should start empty");
        
        container.add(sword);
        assertEquals(1, container.size());
        assertTrue(container.has("Sword"));
    }

    @Test
    void testCapacityLimitThrowsException() throws InventoryFullException {
        container.add(sword);
        container.add(shield);

        assertThrows(InventoryFullException.class, () -> {
            container.add(potion);
        });

        assertEquals(2, container.size(), "Size should remain at capacity");
    }

    @Test
    void testFind() throws InventoryFullException {
        container.add(sword);
        
        Item found = container.find("Sword");
        assertNotNull(found);
        assertEquals("Sword", found.getName());

        Item foundLower = container.find("sword");
        assertNotNull(foundLower);

        assertNull(container.find("NonExistent"));
    }

    @Test
    void testRemove() throws InventoryFullException {
        container.add(sword);
        container.add(shield);

        Item removed = container.remove("Sword");
        assertEquals(sword, removed);
        assertEquals(1, container.size());
        assertFalse(container.has("Sword"));

        Item notFound = container.remove("Gold");
        assertNull(notFound);
        assertEquals(1, container.size());
    }

}