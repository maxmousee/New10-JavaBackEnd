package com.nfsindustries.business;

import com.nfsindustries.model.*;

public class BalanceCalculator {

    public int getDaysInDebt(Document document) {
        int daysInDebt = 0;
        double netBalance = 0;
        BankToCustomerStatementV02 btcs = document.getBkToCstmrStmt();
        for (AccountStatement2 statement: btcs.getStmt()) {
            for (ReportEntry2 reportEntry2: statement.getNtry()) {
                if (reportEntry2.getCdtDbtInd() == CreditDebitCode.CRDT) {
                    netBalance += reportEntry2.getAmt().getValue().doubleValue();
                } else {
                    netBalance -= reportEntry2.getAmt().getValue().doubleValue();
                }
            }
            if(netBalance < 0) daysInDebt++;
        }
        return daysInDebt;
    }

    public double getEndBalance(Document document) {
        double balance = 0;
        BankToCustomerStatementV02 btcs = document.getBkToCstmrStmt();
        for (AccountStatement2 statement: btcs.getStmt()) {
            for (CashBalance3 cashBalance: statement.getBal()) {
                if (cashBalance.getTp().getCdOrPrtry().getCd() == BalanceType12Code.CLBD) {
                    balance = cashBalance.getAmt().getValue().doubleValue();
                }
            }
        }
        return balance;
    }

    public double getStartBalance(Document document) {
        double balance = 0;
        BankToCustomerStatementV02 btcs = document.getBkToCstmrStmt();
        for (AccountStatement2 statement: btcs.getStmt()) {
            for (CashBalance3 cashBalance: statement.getBal()) {
                if (cashBalance.getTp().getCdOrPrtry().getCd() == BalanceType12Code.OPBD) {
                    balance = cashBalance.getAmt().getValue().doubleValue();
                }
            }
        }
        return balance;
    }

    public double getNetBalance(Document document) {
        double netBalance = 0;
        BankToCustomerStatementV02 btcs = document.getBkToCstmrStmt();
        for (AccountStatement2 statement: btcs.getStmt()) {
            for (CashBalance3 cashBalance: statement.getBal()) {
                if (cashBalance.getCdtDbtInd() == CreditDebitCode.CRDT) {
                    netBalance += cashBalance.getAmt().getValue().doubleValue();
                } else {
                    netBalance -= cashBalance.getAmt().getValue().doubleValue();
                }
            }
        }
        return netBalance;
    }
}
