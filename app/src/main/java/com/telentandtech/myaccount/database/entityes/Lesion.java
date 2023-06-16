package com.telentandtech.myaccount.database.entityes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "lesion")
public class Lesion {
    @PrimaryKey(autoGenerate = true)
    private long lesion_id;
    private long class_id;
    private String class_name;
    private long group_id;
    private String group_name;
    private long lesion_date;
    private String uid;



    public Lesion(long lesion_id, long class_id, String class_name,
                  long group_id, String group_name, long lesion_date, String uid) {
        this.lesion_id = lesion_id;
        this.class_id = class_id;
        this.class_name = class_name;
        this.group_id = group_id;
        this.group_name = group_name;
        this.lesion_date = lesion_date;
        this.uid = uid;
    }


    @Ignore
    public Lesion(long class_id, String class_name, long group_id, String group_name,
                  long lesion_date, String uid) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.group_id = group_id;
        this.group_name = group_name;
        this.lesion_date = lesion_date;
        this.uid = uid;
    }

    public long getLesion_id() {
        return lesion_id;
    }

    public void setLesion_id(long lesion_id) {
        this.lesion_id = lesion_id;
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

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public long getLesion_date() {
        return lesion_date;
    }

    public void setLesion_date(long lesion_date) {
        this.lesion_date = lesion_date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
