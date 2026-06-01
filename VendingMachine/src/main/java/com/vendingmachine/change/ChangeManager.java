package com.vendingmachine.change;

import java.util.HashMap;
import java.util.Map;

public class ChangeManager {
    private static final int[] DENOMINATIONS = {50, 20, 10, 5, 1};

    public Map<Integer, Integer> calculateChange(double change) {
        int cents = (int) Math.round(change * 100);
        Map<Integer, Integer> coinsDispensed = new HashMap<>();

        for (int denom : DENOMINATIONS) {
            int count = cents / denom;
            if (count > 0) {
                coinsDispensed.put(denom, count);
                cents %= denom;
            }
        }

        return coinsDispensed;
    }

    public void dispenseChange(double change) {
        if (change <= 0) {
            return;
        }

        Map<Integer, Integer> coinsDispensed = calculateChange(change);
        System.out.println("Dispensing change: $" + String.format("%.2f", change));
        coinsDispensed.forEach((denom, count) ->
                System.out.println("  " + count + " coin(s) of " + denom + " cent(s)")
        );
    }
}
