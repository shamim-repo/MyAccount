package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Fees;

import java.util.List;

public class FeesListResult {
    private List<Fees> feesList;
    private boolean successful;
    private String message;

    public FeesListResult(List<Fees> feesList, boolean successful, String message) {
        this.feesList = feesList;
        this.successful = successful;
        this.message = message;
    }

    public List<Fees> getFeesList() {
        return feesList;
    }

    public void setFeesList(List<Fees> feesList) {
        this.feesList = feesList;
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
