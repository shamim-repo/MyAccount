package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.User;

public class UserResult {
    private User user;
    private boolean isSuccessful;
    private String message;

    public UserResult(User user, boolean isAuthenticated, String message) {
        this.user = user;
        this.isSuccessful = isAuthenticated;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        this.isSuccessful = successful;
    }
}
