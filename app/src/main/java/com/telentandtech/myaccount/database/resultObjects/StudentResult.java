package com.telentandtech.myaccount.database.resultObjects;


import com.telentandtech.myaccount.database.entityes.Students;

public class StudentResult {
    private Students students;
    private boolean isSuccessful;
    private String message;

    public StudentResult(Students students, boolean isSuccessful, String message) {
        this.students = students;
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
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
