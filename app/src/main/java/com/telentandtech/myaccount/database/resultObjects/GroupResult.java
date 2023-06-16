package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Group;

public class GroupResult {
    private Group group;
    private boolean isSuccessful;
    private String message;

    public GroupResult(Group group, boolean isSuccessful, String message) {
        this.group = group;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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
