package com.nfsindustries.business;

import com.nfsindustries.model.AccountStatement2;
import com.nfsindustries.model.BankToCustomerStatementV02;
import com.nfsindustries.model.Document;

public class BalanceCalculator {

    public int getDaysInDebt(Document document) {
        BankToCustomerStatementV02 btcs = document.getBkToCstmrStmt();
        for (AccountStatement2 statement: btcs.getStmt()) {
//            statement.getBal()
        }
        return 0;
    }
}
