package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Attendance;

import java.util.List;

public class AttendanceListResult {
    private List<Attendance> attendanceList;
    private boolean isSuccessful;
    private String message;

    public AttendanceListResult(List<Attendance> attendanceList, boolean isSuccessful, String message) {
        this.attendanceList = attendanceList;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
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
