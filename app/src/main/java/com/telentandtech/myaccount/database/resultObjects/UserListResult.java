package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.User;

import java.util.List;

public class UserListResult {
    private List<User> users;
    private Boolean isSuccessful;
    private String message;

    public UserListResult(List<User> users, Boolean isSuccessful, String message) {
        this.users = users;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
