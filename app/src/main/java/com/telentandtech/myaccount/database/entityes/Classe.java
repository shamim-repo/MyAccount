package com.telentandtech.myaccount.database.entityes;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(tableName = "classes")
public class Classe {
    @PrimaryKey(autoGenerate = true)
    private long class_id;
    private String class_name;
    private Timestamp created_at;
    private String uid;

    @Ignore
    public Classe(long class_id, String class_name, Timestamp created_at, String uid) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.created_at = created_at;
        this.uid = uid;
    }


    public Classe(String class_name, Timestamp created_at, String uid) {
        this.class_name = class_name;
        this.created_at = created_at;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public long getClass_id() {
        return class_id;
    }
    public void setClass_id(long class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
