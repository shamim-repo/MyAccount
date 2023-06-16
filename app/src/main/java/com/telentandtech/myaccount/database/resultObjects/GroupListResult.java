package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Group;

import java.util.List;

public class GroupListResult {
    private List<Group> groupList;
    private boolean isSuccessful;
    private String message;

    public GroupListResult(List<Group> groupList, boolean isSuccessful, String message) {
        this.groupList = groupList;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
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
