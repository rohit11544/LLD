package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;

public class ChangeDispensingState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Dispensing change, cannot insert money");
    }

    @Override
    public void selectItem(VendingMachine machine, String itemCode) {
        System.out.println("Dispensing change");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("Cannot cancel, change being dispensed");
    }
}
