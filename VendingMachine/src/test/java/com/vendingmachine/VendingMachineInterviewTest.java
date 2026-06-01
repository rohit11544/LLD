package com.vendingmachine;

import com.vendingmachine.payment.CardPayment;
import com.vendingmachine.payment.CashPayment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for scenarios commonly asked in LLD interviews.
 */
public class VendingMachineInterviewTest {

    private static final String SODA = "A1";
    private static final String CHIPS = "A2";

    private VendingMachine machine;

    @Before
    public void setUp() {
        VendingMachine.resetForTesting();
        machine = VendingMachine.getInstance();
        machine.addItem(SODA, "Soda", 1.50, 5);
        machine.addItem(CHIPS, "Chips", 1.00, 5);
    }

    @After
    public void tearDown() {
        VendingMachine.resetForTesting();
    }

    // --- Happy path ---

    @Test
    public void successfulPurchase_reducesStock_andReturnsToIdle() {
        machine.insertMoney(2.00);
        assertEquals("CoinInsertedState", machine.getCurrentStateName());

        machine.selectItem(SODA);
        assertEquals("IdleState", machine.getCurrentStateName());
        assertEquals(4, machine.getInventory().getStock(SODA));
        assertNull(machine.getPayment());
        assertEquals(0, machine.getTotalInserted(), 0.001);
    }

    @Test
    public void exactAmountPurchase_noChangeStillCompletes() {
        machine.insertMoney(1.50);
        machine.selectItem(SODA);
        assertEquals("IdleState", machine.getCurrentStateName());
        assertEquals(4, machine.getInventory().getStock(SODA));
    }

    // --- Insufficient funds ---

    @Test
    public void insufficientFunds_staysInCoinInserted_doesNotReduceStock() {
        machine.insertMoney(0.50);
        machine.selectItem(SODA);

        assertEquals("CoinInsertedState", machine.getCurrentStateName());
        assertEquals(5, machine.getInventory().getStock(SODA));
        assertTrue(machine.getPayment() instanceof CashPayment);
    }

    @Test
    public void additionalMoneyAfterInsufficient_thenPurchaseSucceeds() {
        machine.insertMoney(0.50);
        machine.selectItem(SODA);
        assertEquals("CoinInsertedState", machine.getCurrentStateName());

        machine.insertMoney(1.00);
        machine.selectItem(SODA);

        assertEquals("IdleState", machine.getCurrentStateName());
        assertEquals(4, machine.getInventory().getStock(SODA));
    }

    // --- Out of stock ---

    @Test
    public void outOfStock_staysInCoinInserted_doesNotReduceStock() {
        machine.addItem("EMPTY", "Premium", 2.00, 0);
        machine.insertMoney(2.50);
        machine.selectItem("EMPTY");

        assertEquals("CoinInsertedState", machine.getCurrentStateName());
        assertEquals(0, machine.getInventory().getStock("EMPTY"));
    }

    @Test
    public void unknownItemCode_doesNotChangeStateOrStock() {
        machine.insertMoney(2.00);
        machine.selectItem("UNKNOWN");

        assertEquals("CoinInsertedState", machine.getCurrentStateName());
        assertEquals(5, machine.getInventory().getStock(SODA));
    }

    // --- Cancel / refund ---

    @Test
    public void cancelAfterInsertMoney_returnsToIdle_clearsPayment() {
        machine.insertMoney(2.00);
        machine.cancelTransaction();

        assertEquals("IdleState", machine.getCurrentStateName());
        assertNull(machine.getPayment());
        assertEquals(0, machine.getTotalInserted(), 0.001);
        assertEquals(5, machine.getInventory().getStock(SODA));
    }

    @Test
    public void cancelFromIdle_isNoOp() {
        machine.cancelTransaction();
        assertEquals("IdleState", machine.getCurrentStateName());
    }

    // --- Payment strategy ---

    @Test
    public void cardPayment_success_reducesStock() {
        machine.insertMoneyWithCard("4111111111111111", 1.50);
        assertEquals("CoinInsertedState", machine.getCurrentStateName());
        assertTrue(machine.getPayment() instanceof CardPayment);

        machine.selectItem(SODA);
        assertEquals("IdleState", machine.getCurrentStateName());
        assertEquals(4, machine.getInventory().getStock(SODA));
    }

    @Test
    public void selectItemWithoutMoney_staysIdle() {
        machine.selectItem(SODA);
        assertEquals("IdleState", machine.getCurrentStateName());
        assertEquals(5, machine.getInventory().getStock(SODA));
    }

    // --- Admin ---

    @Test
    public void refillItem_increasesStock() {
        machine.addItem("LOW", "Water", 0.50, 1);
        machine.refillItem("LOW", 4);
        assertEquals(5, machine.getInventory().getStock("LOW"));
    }
}
