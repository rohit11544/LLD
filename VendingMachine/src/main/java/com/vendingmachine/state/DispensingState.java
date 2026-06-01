package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;

public class DispensingState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Dispensing in progress, cannot insert money");
    }

    @Override
    public void selectItem(VendingMachine machine, String itemCode) {
        System.out.println("Dispensing in progress");
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("Cannot cancel during dispensing");
    }
}
