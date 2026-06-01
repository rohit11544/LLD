package com.vendingmachine.inventory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Interview focus: stock checks and dispense.
 */
public class InventoryTest {

    private Inventory inventory;

    @Before
    public void setUp() {
        inventory = new Inventory();
        inventory.addItem("A1", new Item("A1", "Soda", 1.50), 2);
    }

    @Test
    public void isInStock_whenQuantityPositive_returnsTrue() {
        assertTrue(inventory.isInStock("A1"));
    }

    @Test
    public void isInStock_whenZeroStock_returnsFalse() {
        inventory.addItem("A2", new Item("A2", "Chips", 1.00), 0);
        assertFalse(inventory.isInStock("A2"));
    }

    @Test
    public void dispenseItem_reducesStockByOne() {
        assertTrue(inventory.dispenseItem("A1"));
        assertEquals(1, inventory.getStock("A1"));
    }

    @Test
    public void dispenseItem_whenOutOfStock_returnsFalse() {
        inventory.dispenseItem("A1");
        inventory.dispenseItem("A1");
        assertFalse(inventory.dispenseItem("A1"));
        assertEquals(0, inventory.getStock("A1"));
    }
}
