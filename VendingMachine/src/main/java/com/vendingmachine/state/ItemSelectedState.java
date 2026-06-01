package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;

public class ItemSelectedState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Item already selected, cannot insert more money");
    }

    @Override
    public void selectItem(VendingMachine machine, String itemCode) {
        System.out.println("Item already selected");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("Transaction cancelled, refunding money");
        machine.getPayment().refund(machine.getTotalInserted());
        machine.resetTransaction();
        machine.setState(new IdleState());
    }
}
