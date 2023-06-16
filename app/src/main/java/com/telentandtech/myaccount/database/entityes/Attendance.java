package com.telentandtech.myaccount.database.entityes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "attendance")
public class Attendance {
    @PrimaryKey(autoGenerate = true)
    private long attendance_id;
    private long class_id;
    private String class_name;
    private long group_id;
    private String group_name;
    private String student_name;
    private long student_id;
    private String id;
    private String phone;
    private boolean attended;
    private long date;
    private String uid;

    public Attendance(long attendance_id, long class_id, String class_name, long group_id,
                      String group_name, String student_name, long student_id, String id,
                      String phone, boolean attended, long date, String uid) {
        this.attendance_id = attendance_id;
        this.class_id = class_id;
        this.class_name = class_name;
        this.group_id = group_id;
        this.group_name = group_name;
        this.student_name = student_name;
        this.student_id = student_id;
        this.id = id;
        this.phone = phone;
        this.attended = attended;
        this.date = date;
        this.uid = uid;
    }
    @Ignore
    public Attendance(long class_id, String class_name, long group_id, String group_name,
                      String student_name, long student_id, String id, String phone, boolean attended,
                      long date, String uid) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.group_id = group_id;
        this.group_name = group_name;
        this.student_name = student_name;
        this.student_id = student_id;
        this.id = id;
        this.phone = phone;
        this.attended = attended;
        this.date = date;
        this.uid = uid;
    }

    public long getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(long attendance_id) {
        this.attendance_id = attendance_id;
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

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
