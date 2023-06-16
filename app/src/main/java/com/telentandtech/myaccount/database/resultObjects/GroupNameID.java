package com.telentandtech.myaccount.database.resultObjects;

public class GroupNameID {
    private String group_name;
    private long group_id;

    public GroupNameID(String group_name, long group_id) {
        this.group_name = group_name;
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public long getGroup_id() {
        return group_id;
    }
}
