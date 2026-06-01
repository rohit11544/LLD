package com.vendingmachine.change;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Interview focus: greedy change algorithm.
 */
public class ChangeManagerTest {

    private ChangeManager changeManager;

    @Before
    public void setUp() {
        changeManager = new ChangeManager();
    }

    @Test
    public void calculateChange_exactFiftyCents_oneCoin() {
        Map<Integer, Integer> coins = changeManager.calculateChange(0.50);
        assertEquals(Integer.valueOf(1), coins.get(50));
        assertEquals(1, coins.values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void calculateChange_sixtyEightCents_greedyMinimumCoins() {
        // $0.68 → 50 + 10 + 5 + 1 + 1 + 1 = 6 coins
        Map<Integer, Integer> coins = changeManager.calculateChange(0.68);
        assertEquals(Integer.valueOf(1), coins.get(50));
        assertEquals(Integer.valueOf(1), coins.get(10));
        assertEquals(Integer.valueOf(1), coins.get(5));
        assertEquals(Integer.valueOf(3), coins.get(1));
        assertEquals(6, coins.values().stream().mapToInt(Integer::intValue).sum());
    }

    @Test
    public void calculateChange_zeroAmount_emptyMap() {
        Map<Integer, Integer> coins = changeManager.calculateChange(0);
        assertTrue(coins.isEmpty());
    }
}
