package com.telentandtech.myaccount.database.resultObjects;


public class ClassNameId {
    private String class_name;
    private long class_id;

    public ClassNameId(String class_name, long class_id) {
        this.class_name = class_name;
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public long getClass_id() {
        return class_id;
    }
}
