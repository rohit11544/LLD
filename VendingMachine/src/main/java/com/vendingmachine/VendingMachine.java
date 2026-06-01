package com.vendingmachine;

import com.vendingmachine.admin.AdminInterface;
import com.vendingmachine.change.ChangeManager;
import com.vendingmachine.inventory.Inventory;
import com.vendingmachine.inventory.Item;
import com.vendingmachine.payment.CardPayment;
import com.vendingmachine.payment.CashPayment;
import com.vendingmachine.payment.PaymentProcessor;
import com.vendingmachine.state.ChangeDispensingState;
import com.vendingmachine.state.CoinInsertedState;
import com.vendingmachine.state.DispensingState;
import com.vendingmachine.state.IdleState;
import com.vendingmachine.state.VendingMachineState;

public class VendingMachine implements AdminInterface {
    private static VendingMachine instance;

    private VendingMachineState state;
    private final Inventory inventory;
    private PaymentProcessor payment;
    private Item selectedItem;
    private double totalInserted;
    private final ChangeManager changeManager;
    private double cashCollected;

    public static synchronized VendingMachine getInstance() {
        if (instance == null) {
            instance = new VendingMachine();
        }
        return instance;
    }

    private VendingMachine() {
        this.state = new IdleState();
        this.inventory = new Inventory();
        this.changeManager = new ChangeManager();
        this.totalInserted = 0;
        this.cashCollected = 0;
    }

    public void setState(VendingMachineState newState) {
        this.state = newState;
        System.out.println("State changed to: " + newState.getClass().getSimpleName());
    }

    public void insertMoney(double amount) {
        if (state instanceof IdleState) {
            totalInserted = amount;
        } else {
            totalInserted += amount;
        }
        state.insertMoney(this, amount);
    }

    public void insertMoneyWithCard(String cardNumber, double amount) {
        if (!(state instanceof IdleState)) {
            System.out.println("Cannot start card payment — machine is not idle");
            return;
        }
        System.out.println("Card authorized for up to: $" + amount);
        CardPayment cardPayment = new CardPayment(cardNumber);
        cardPayment.setAuthorizedAmount(amount);
        payment = cardPayment;
        totalInserted = amount;
        setState(new CoinInsertedState());
    }

    public void selectItem(String itemCode) {
        state.selectItem(this, itemCode);
    }

    public void cancelTransaction() {
        state.cancelTransaction(this);
    }

    public void dispenseItem() {
        setState(new DispensingState());

        if (inventory.dispenseItem(selectedItem.getCode())) {
            double price = selectedItem.getPrice();
            cashCollected += price;

            double change = totalInserted - price;
            if (change > 0) {
                setState(new ChangeDispensingState());
                changeManager.dispenseChange(change);
            }

            resetTransaction();
            setState(new IdleState());
        } else {
            System.out.println("Failed to dispense item");
            payment.refund(totalInserted);
            resetTransaction();
            setState(new IdleState());
        }
    }

    public boolean hasSufficientFunds(double itemPrice) {
        return totalInserted >= itemPrice;
    }

    public void addPayment(double amount) {
        if (payment != null) {
            payment.addAmount(amount);
        }
    }

    public void resetTransaction() {
        totalInserted = 0;
        selectedItem = null;
        payment = null;
    }

    @Override
    public void refillItem(String itemCode, int quantity) {
        inventory.refillItem(itemCode, quantity);
        System.out.println("Admin: Refilled " + itemCode + " with " + quantity + " units");
    }

    @Override
    public void collectCash(double amount) {
        if (amount > cashCollected) {
            System.out.println("Admin: Cannot collect $" + amount + " — only $" + cashCollected + " available");
            return;
        }
        cashCollected -= amount;
        System.out.println("Admin: Collected $" + amount + " | Remaining in machine: $" + cashCollected);
    }

    @Override
    public void addItem(String code, String name, double price, int quantity) {
        inventory.addItem(code, new Item(code, name, price), quantity);
        System.out.println("Admin: Added item " + name + " ($" + price + ") qty=" + quantity);
    }

    public void adminRefillItem(String itemCode, int quantity) {
        refillItem(itemCode, quantity);
    }

    public void adminCollectCash(double amount) {
        collectCash(amount);
    }

    public void adminAddItem(String code, String name, double price, int quantity) {
        addItem(code, name, price, quantity);
    }

    @Override
    public void viewInventory() {
        inventory.viewInventory();
    }

    @Override
    public void viewCashCollected() {
        System.out.println("Cash in machine: $" + cashCollected);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PaymentProcessor getPayment() {
        return payment;
    }

    public double getTotalInserted() {
        return totalInserted;
    }

    public void setPayment(PaymentProcessor payment) {
        this.payment = payment;
    }

    public void setSelectedItem(Item item) {
        this.selectedItem = item;
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public ChangeManager getChangeManager() {
        return changeManager;
    }
}
