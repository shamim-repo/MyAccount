package com.telentandtech.myaccount.database.entityes;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;


@Entity(tableName = "payments")
public class Payments {
    @PrimaryKey(autoGenerate = true)
    private long payment_id;
    private long fees_id;
    private String class_name;
    private long class_id;
    private String group_name;
    private long group_id;
    private Double payment_amount;
    private Boolean payment_status;
    private long payment_month;
    @Nullable
    private Timestamp payment_timestamp;
    private long student_id;
    private String id;
    private String student_name;
    private String guardian_name;
    private String phone;
    private String uid;


    public Payments(long payment_id, long fees_id, String class_name, long class_id, String group_name, long group_id,
                    Double payment_amount, Boolean payment_status, long payment_month, Timestamp payment_timestamp,
                    long student_id, String id, String student_name, String guardian_name, String phone, String uid) {
        this.payment_id = payment_id;
        this.fees_id = fees_id;
        this.class_name = class_name;
        this.class_id = class_id;
        this.group_name = group_name;
        this.group_id = group_id;
        this.payment_amount = payment_amount;
        this.payment_status = payment_status;
        this.payment_month = payment_month;
        this.payment_timestamp = payment_timestamp;
        this.student_id = student_id;
        this.id = id;
        this.student_name = student_name;
        this.guardian_name = guardian_name;
        this.phone = phone;
        this.uid = uid;
    }

    @Ignore
    public Payments() {
    }

    public long getFees_id() {
        return fees_id;
    }

    public void setFees_id(long fees_id) {
        this.fees_id = fees_id;
    }

    @Ignore
    public Payments(String class_name, long class_id, String group_name, long group_id, Double payment_amount,
                    Boolean payment_status, long payment_month, Timestamp payment_timestamp,
                    long student_id, String id, String student_name, String guardian_name, String phone, String uid) {

        this.class_name = class_name;
        this.class_id = class_id;
        this.group_name = group_name;
        this.group_id = group_id;
        this.payment_amount = payment_amount;
        this.payment_status = payment_status;
        this.payment_month = payment_month;
        this.payment_timestamp = payment_timestamp;
        this.student_id = student_id;
        this.id = id;
        this.student_name = student_name;
        this.guardian_name = guardian_name;
        this.phone = phone;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(long payment_id) {
        this.payment_id = payment_id;
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

    public Double getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(Double payment_amount) {
        this.payment_amount = payment_amount;
    }

    public long getPayment_month() {
        return payment_month;
    }

    public void setPayment_month(long payment_month) {
        this.payment_month = payment_month;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
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

    public Timestamp getPayment_timestamp() {
        return payment_timestamp;
    }

    public void setPayment_timestamp(Timestamp payment_timestamp) {
        this.payment_timestamp = payment_timestamp;
    }

    public Boolean getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(Boolean payment_status) {
        this.payment_status = payment_status;
    }
}
