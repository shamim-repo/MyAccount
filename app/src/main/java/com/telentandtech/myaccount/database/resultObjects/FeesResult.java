package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Fees;

public class FeesResult {
    private Fees fees;
    private boolean successful;
    private String message;

    public FeesResult(Fees fees, boolean successful, String message) {
        this.fees = fees;
        this.successful = successful;
        this.message = message;
    }

    public Fees getFees() {
        return fees;
    }

    public void setFees(Fees fees) {
        this.fees = fees;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
