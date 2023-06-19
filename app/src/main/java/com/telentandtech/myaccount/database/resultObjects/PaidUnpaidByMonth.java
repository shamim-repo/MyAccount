package com.telentandtech.myaccount.database.resultObjects;

public class PaidUnpaidByMonth {
    private Long month;
    private Long paid;
    private Long due;

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }

    public Long getDue() {
        return due;
    }

    public void setDue(Long due) {
        this.due = due;
    }

    public PaidUnpaidByMonth(Long month, Long paid, Long due) {
        this.month = month;
        this.paid = paid;
        this.due = due;
    }
}
