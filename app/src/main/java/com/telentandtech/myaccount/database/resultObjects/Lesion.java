package com.telentandtech.myaccount.database.resultObjects;

public class Lesion {
    private Lesion lesion;
    private boolean isSuccessful;
    private String message;

    public Lesion(Lesion lesion, boolean isSuccessful, String message) {
        this.lesion = lesion;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public Lesion getLesion() {
        return lesion;
    }

    public void setLesion(Lesion lesion) {
        this.lesion = lesion;
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
