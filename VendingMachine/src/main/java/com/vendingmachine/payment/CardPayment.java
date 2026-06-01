package com.vendingmachine.payment;

public class CardPayment implements PaymentProcessor {
    private final String cardNumber;
    private double authorizedAmount;
    private double amountCharged;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setAuthorizedAmount(double amount) {
        this.authorizedAmount = amount;
    }

    @Override
    public boolean process(double amount) {
        if (authorizedAmount < amount) {
            System.out.println("Card declined: insufficient authorized amount");
            return false;
        }
        System.out.println("Processing card payment of $" + amount + " for card ending " + maskCard());
        amountCharged = amount;
        return true;
    }

    @Override
    public void refund(double amount) {
        System.out.println("Refunding $" + amount + " to card ending " + maskCard());
        amountCharged = 0;
    }

    @Override
    public double getAmountInserted() {
        return authorizedAmount;
    }

    @Override
    public void addAmount(double amount) {
        authorizedAmount += amount;
    }

    private String maskCard() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return cardNumber.substring(cardNumber.length() - 4);
    }
}
