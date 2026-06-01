package com.vendingmachine;

public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine machine = VendingMachine.getInstance();

        machine.adminAddItem("A1", "Soda", 1.50, 10);
        machine.adminAddItem("A2", "Chips", 1.00, 10);
        machine.adminAddItem("A3", "Candy", 0.50, 10);

        System.out.println("\n--- Transaction 1: Buy Soda (with change) ---");
        machine.insertMoney(2.0);
        machine.selectItem("A1");

        System.out.println("\n--- Transaction 2: Insufficient funds ---");
        machine.insertMoney(0.50);
        machine.selectItem("A1");
        machine.cancelTransaction();

        System.out.println("\n--- Transaction 3: Cancel and refund ---");
        machine.insertMoney(2.0);
        machine.cancelTransaction();

        System.out.println("\n--- Transaction 4: Out of stock ---");
        machine.adminAddItem("A4", "Premium Snack", 2.0, 0);
        machine.insertMoney(2.5);
        machine.selectItem("A4");
        machine.cancelTransaction();

        System.out.println("\n--- Transaction 5: Card payment ---");
        machine.insertMoneyWithCard("4111111111111111", 1.00);
        machine.selectItem("A2");

        System.out.println("\n--- Admin: View inventory & cash ---");
        machine.viewInventory();
        machine.viewCashCollected();
    }
}
