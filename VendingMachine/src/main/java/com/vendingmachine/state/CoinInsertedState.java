package com.vendingmachine.state;

import com.vendingmachine.VendingMachine;
import com.vendingmachine.inventory.Item;

public class CoinInsertedState implements VendingMachineState {
    @Override
    public void insertMoney(VendingMachine machine, double amount) {
        System.out.println("Additional money inserted: $" + amount);
        machine.addPayment(amount);
    }

    @Override
    public void selectItem(VendingMachine machine, String itemCode) {
        Item item = machine.getInventory().getItem(itemCode);

        if (item == null) {
            System.out.println("Item not found");
            return;
        }

        if (!machine.getInventory().isInStock(itemCode)) {
            System.out.println("Item out of stock");
            System.out.println("State remains: COIN_INSERTED — select another item or cancel");
            return;
        }

        if (machine.getPayment().process(item.getPrice())) {
            machine.setSelectedItem(item);
            machine.setState(new ItemSelectedState());
            machine.dispenseItem();
        } else {
            double inserted = machine.getTotalInserted();
            System.out.println("Insufficient funds. Item costs: $" + item.getPrice());
            System.out.println("You inserted: $" + inserted);
            System.out.println("Additional needed: $" + (item.getPrice() - inserted));
        }
    }

    @Override
    public void cancelTransaction(VendingMachine machine) {
        System.out.println("Transaction cancelled, refunding money");
        machine.getPayment().refund(machine.getTotalInserted());
        machine.resetTransaction();
        machine.setState(new IdleState());
    }
}
