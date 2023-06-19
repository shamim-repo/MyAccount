package com.telentandtech.myaccount.database.resultObjects;

public class AttendanceCount {
    private Long total_count;
    private Long present_count;
    private Long absent_count;

    public Long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Long total_count) {
        this.total_count = total_count;
    }

    public Long getPresent_count() {
        return present_count;
    }

    public void setPresent_count(Long present_count) {
        this.present_count = present_count;
    }

    public Long getAbsent_count() {
        return absent_count;
    }

    public void setAbsent_count(Long absent_count) {
        this.absent_count = absent_count;
    }

    public AttendanceCount(Long total_count, Long present_count, Long absent_count) {
        this.total_count = total_count;
        this.present_count = present_count;
        this.absent_count = absent_count;
    }
}
