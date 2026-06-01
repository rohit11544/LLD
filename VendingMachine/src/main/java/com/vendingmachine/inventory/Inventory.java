package com.vendingmachine.inventory;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Item> items = new HashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();

    public void addItem(String code, Item item, int quantity) {
        items.put(code, item);
        stock.put(code, quantity);
    }

    public Item getItem(String code) {
        return items.get(code);
    }

    public boolean isInStock(String code) {
        return stock.getOrDefault(code, 0) > 0;
    }

    public int getStock(String code) {
        return stock.getOrDefault(code, 0);
    }

    public boolean dispenseItem(String code) {
        if (!isInStock(code)) {
            return false;
        }
        stock.put(code, stock.get(code) - 1);
        System.out.println("Dispensing: " + items.get(code).getName());
        return true;
    }

    public void refillItem(String code, int quantity) {
        stock.put(code, stock.getOrDefault(code, 0) + quantity);
    }

    public void viewInventory() {
        System.out.println("--- Inventory ---");
        for (Map.Entry<String, Item> entry : items.entrySet()) {
            String code = entry.getKey();
            Item item = entry.getValue();
            System.out.printf("  %s | %s | $%.2f | qty: %d%n",
                    code, item.getName(), item.getPrice(), getStock(code));
        }
    }
}
