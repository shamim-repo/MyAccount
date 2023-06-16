package com.telentandtech.myaccount.database.entityes;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private long user_id;
    @NonNull
    private String uid;
    private String full_name;
    private String email;
    private String password;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Ignore
    public User(String uid, String full_name, String email) {
        this.uid = uid;
        this.full_name = full_name;
        this.email = email;;
    }

    @Ignore
    public User(String uid, String full_name, String email, String password) {
        this.uid = uid;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    public User(long user_id, String uid , String full_name, String email, String password) {
        this.user_id = user_id;
        this.uid = uid;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
