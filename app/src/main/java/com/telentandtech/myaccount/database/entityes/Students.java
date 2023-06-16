package com.telentandtech.myaccount.database.entityes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;


@Entity(tableName = "students")
public class Students {
    @PrimaryKey(autoGenerate = true)
    private long student_id;
    private String full_name;
    private String id;
    private String school_name;
    private String guardian_name;
    private String phone;
    private String address;
    private String class_name;
    private long class_id;
    private String group_name;
    private long group_id;
    private long starting_date;
    private Timestamp created_at;
    private String uid;


    public Students(long student_id, String full_name, String id, String school_name, String guardian_name,
                    String phone, String address, String class_name, long class_id, String group_name,
                    long group_id, long starting_date, Timestamp created_at, String uid) {
        this.student_id = student_id;
        this.full_name = full_name;
        this.id = id;
        this.school_name = school_name;
        this.guardian_name = guardian_name;
        this.phone = phone;
        this.address = address;
        this.class_name = class_name;
        this.class_id = class_id;
        this.group_name = group_name;
        this.group_id = group_id;
        this.starting_date = starting_date;
        this.created_at = created_at;
        this.uid = uid;
    }

    @Ignore
    public Students(String full_name, String id, String school_name, String guardian_name, String phone,
                    String address, String class_name, long class_id, String group_name, long group_id,
                    long starting_date, Timestamp created_at, String uid) {
        this.full_name = full_name;
        this.id = id;
        this.school_name = school_name;
        this.guardian_name = guardian_name;
        this.phone = phone;
        this.address = address;
        this.class_name = class_name;
        this.class_id = class_id;
        this.group_name = group_name;
        this.group_id = group_id;
        this.starting_date = starting_date;
        this.created_at = created_at;
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getClass_id() {
        return class_id;
    }

    public void setClass_id(long class_id) {
        this.class_id = class_id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public long getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(long starting_date) {
        this.starting_date = starting_date;
    }
}
