package com.telentandtech.myaccount.database.resultObjects;

import com.telentandtech.myaccount.database.entityes.Students;

import java.util.List;

public class StudentListResult {
    private List<Students> studentsList;
    private boolean isSuccessful;
    private String message;

    public StudentListResult(List<Students> studentsList, boolean isSuccessful, String message) {
        this.studentsList = studentsList;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public List<Students> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(List<Students> studentsList) {
        this.studentsList = studentsList;
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
