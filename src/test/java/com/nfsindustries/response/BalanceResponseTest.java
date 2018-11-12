package com.nfsindustries.response;

import org.junit.Test;

import static org.junit.Assert.*;

public class BalanceResponseTest {

    @Test
    public void getStartBalance() {
        BalanceResponse balanceResponse = new BalanceResponse(1.0, 1.0, 0.0, 0);
        assertEquals(1.0, balanceResponse.getStartBalance(), 0.0);
    }

    @Test
    public void getEndBalance() {
        BalanceResponse balanceResponse = new BalanceResponse(2.0, 8.0, 2.0, 3);
        assertEquals(8.0, balanceResponse.getEndBalance(), 0.0);
    }

    @Test
    public void getNetBalance() {
        BalanceResponse balanceResponse = new BalanceResponse(2.0, 8.0, 9.0, 3);
        assertEquals(9.0, balanceResponse.getNetBalance(), 0.0);
    }

    @Test
    public void getDaysInDebt() {
        BalanceResponse balanceResponse = new BalanceResponse(2.0, 8.0, 2.0, 7);
        assertEquals(7, balanceResponse.getDaysInDebt());
    }
}