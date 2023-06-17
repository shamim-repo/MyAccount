package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Payments;

public class PaymentsResult {
    private Payments payments;
    private boolean isSuccessful;
    private String message;

    public PaymentsResult(Payments payments, boolean isSuccessful, String message) {
        this.payments = payments;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
