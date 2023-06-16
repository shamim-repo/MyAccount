package com.telentandtech.myaccount.database.entityes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;


@Entity(tableName = "fees")
public class Fees {
    @PrimaryKey(autoGenerate = true)
    private long fee_id;
    private String class_name;

    private long class_id;
    private String group_name;
    private long group_id;
    private Double fee_amount;
    private long fee_month;
    private Timestamp created_at;
    private String uid;


    public Fees(long fee_id, String class_name, long class_id, String group_name,
                long group_id, Double fee_amount, long fee_month, Timestamp created_at, String uid) {
        this.fee_id = fee_id;
        this.class_name = class_name;
        this.class_id = class_id;
        this.group_name = group_name;
        this.group_id = group_id;
        this.fee_amount = fee_amount;
        this.fee_month = fee_month;
        this.created_at = created_at;
        this.uid = uid;
    }
    @Ignore
    public Fees(String class_name, long class_id, String group_name, long group_id,
                Double fee_amount, long fee_month, Timestamp created_at, String uid) {
        this.class_name = class_name;
        this.class_id = class_id;
        this.group_name = group_name;
        this.group_id = group_id;
        this.fee_amount = fee_amount;
        this.fee_month = fee_month;
        this.created_at = created_at;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public long getFee_month() {
        return fee_month;
    }

    public void setFee_month(long fee_month) {
        this.fee_month = fee_month;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }



    public long getFee_id() {
        return fee_id;
    }

    public void setFee_id(long fee_id) {
        this.fee_id = fee_id;
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

    public Double getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(Double fee_amount) {
        this.fee_amount = fee_amount;
    }
}
