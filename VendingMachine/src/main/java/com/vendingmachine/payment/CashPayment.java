package com.vendingmachine.payment;

import com.vendingmachine.change.ChangeManager;

public class CashPayment implements PaymentProcessor {
    private double amountInserted;
    private final ChangeManager changeManager;

    public CashPayment(double amount, ChangeManager changeManager) {
        this.amountInserted = amount;
        this.changeManager = changeManager;
    }

    @Override
    public boolean process(double amount) {
        return amountInserted >= amount;
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refunding $" + amount + " in coins");
        changeManager.dispenseChange(amount);
    }

    @Override
    public double getAmountInserted() {
        return amountInserted;
    }

    @Override
    public void addAmount(double amount) {
        this.amountInserted += amount;
    }
}
