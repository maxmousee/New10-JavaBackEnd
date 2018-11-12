package com.nfsindustries.response;

public class BalanceResponse {
    private final double startBalance;
    private final double endBalance;
    private final double netBalance;
    private final int daysInDebt;

    public BalanceResponse(double startBalance, double endBalance, double netBalance, int daysInDebt) {
        this.startBalance = startBalance;
        this.endBalance = endBalance;
        this.netBalance = netBalance;
        this.daysInDebt = daysInDebt;
    }

    public double getStartBalance() {
        return startBalance;
    }

    public double getEndBalance() {
        return endBalance;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public int getDaysInDebt() {
        return daysInDebt;
    }

}
