package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Classe;

public class ClassResult {
    private Classe classe;
    private boolean isSuccessful;
    private String message;

    public ClassResult(Classe classe, boolean isSuccessful, String message) {
        this.classe = classe;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
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
