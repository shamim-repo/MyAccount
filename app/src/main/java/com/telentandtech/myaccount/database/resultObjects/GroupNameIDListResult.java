package com.telentandtech.myaccount.database.resultObjects;

import java.util.List;

public class GroupNameIDListResult {

    private List<GroupNameID> groupNameIDList;
    private Boolean isSuccessful;
    private String message;

    public GroupNameIDListResult(List<GroupNameID> groupNameIDList, Boolean isSuccessful, String message) {
        this.groupNameIDList = groupNameIDList;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<GroupNameID> getGroupNameIDList() {
        return groupNameIDList;
    }

    public void setGroupNameIDList(List<GroupNameID> groupNameIDList) {
        this.groupNameIDList = groupNameIDList;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
