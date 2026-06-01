package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;

public interface VendingMachineState {
    void insertMoney(VendingMachine machine, double amount);
    void selectItem(VendingMachine machine, String itemCode);
    void cancelTransaction(VendingMachine machine);
}
