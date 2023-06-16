package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Classe;

import java.util.List;

public class ClassListResult {
    private List<Classe> classList;
    private boolean isSuccessful;
    private String message;

    public ClassListResult(List<Classe> classList, boolean inSuccessful, String message) {
        this.classList = classList;
        this.isSuccessful = inSuccessful;
        this.message = message;
    }

    public List<Classe> getClassList() {
        return classList;
    }

    public void setClassList(List<Classe> classList) {
        this.classList = classList;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        this.isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
