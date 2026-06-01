package com.vendingmachine.payment;

public interface PaymentProcessor {
    boolean process(double amount);
    void refund(double amount);
    double getAmountInserted();
    void addAmount(double amount);
}
