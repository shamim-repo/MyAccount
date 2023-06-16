package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Attendance;

public class AttendanceResult {
    private Attendance attendance;
    private boolean isSuccessful;
    private String message;

    public AttendanceResult(Attendance attendance, boolean isSuccessful, String message) {
        this.attendance = attendance;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
