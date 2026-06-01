package com.vendingmachine.admin;

public interface AdminInterface {
    void refillItem(String itemCode, int quantity);
    void collectCash(double amount);
    void addItem(String code, String name, double price, int quantity);
    void viewInventory();
    void viewCashCollected();
}
