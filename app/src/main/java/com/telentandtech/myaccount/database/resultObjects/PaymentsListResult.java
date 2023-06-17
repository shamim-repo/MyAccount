package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Payments;

import java.util.List;

public class PaymentsListResult {
    private List<Payments> paymentsList;
    private boolean isSuccessful;
    private String message;

    public PaymentsListResult(List<Payments> paymentsList, boolean isSuccessful, String message) {
        this.paymentsList = paymentsList;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<Payments> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<Payments> paymentsList) {
        this.paymentsList = paymentsList;
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
