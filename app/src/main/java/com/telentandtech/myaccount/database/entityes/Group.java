package com.telentandtech.myaccount.database.entityes;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;


@Entity(tableName = "groups")
public class Group {
    @PrimaryKey(autoGenerate = true)
    private long group_id;
    private String group_name;
    private String class_name;
    private long class_id;
    private boolean active;
    private long start_date;
    @Nullable
    private long end_date;
    private Timestamp start_time;
    private Timestamp end_time;
    private Timestamp created_at;
    private String uid;
    
    /*Constructor String group_name, String class_name, long class_id, boolean active, 
    long start_date, long end_date, long start_time, long end_time, 
    Timestamp created_at, long uid
     */
    @Ignore
    public Group(String group_name, String class_name, long class_id, boolean active, 
                 long start_date, long end_date, Timestamp start_time,
                 Timestamp end_time, Timestamp created_at, String uid) {
        this.group_name = group_name;
        this.class_name = class_name;
        this.class_id = class_id;
        this.active = active;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.created_at = created_at;
        this.uid = uid;
    }
    
    /*Constructor long group_id, String group_name, String class_name, long class_id,
    boolean active, long start_date, long end_date, long start_time, long end_time,
    Timestamp created_at, long uid
     */
    public Group(long group_id, String group_name, String class_name, long class_id,
                 boolean active, long start_date, long end_date, Timestamp start_time, Timestamp end_time,
                 Timestamp created_at, String uid) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.class_name = class_name;
        this.class_id = class_id;
        this.active = active;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.end_time = end_time;
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

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public long getClass_id() {
        return class_id;
    }

    public void setClass_id(long class_id) {
        this.class_id = class_id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getStart_date() {
        return start_date;
    }

    public void setStart_date(long start_date) {
        this.start_date = start_date;
    }

    public long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(long end_date) {
        this.end_date = end_date;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }
}
