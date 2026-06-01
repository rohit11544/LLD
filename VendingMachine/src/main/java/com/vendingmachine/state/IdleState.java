package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;
import com.vendingmachine.payment.CashPayment;

public class IdleState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Money inserted: $" + amount);
        machine.setPayment(new CashPayment(amount, machine.getChangeManager()));
        machine.setState(new CoinInsertedState());
    }

    @Override
    public void selectItem(VendingMachine machine, String itemCode) {
        System.out.println("Please insert money first");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("No transaction to cancel");
    }
}
