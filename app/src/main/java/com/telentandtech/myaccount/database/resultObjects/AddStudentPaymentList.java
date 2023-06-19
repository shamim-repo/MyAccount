package com.telentandtech.myaccount.database.resultObjects;

public class AddStudentPaymentList {
    private long payment_month;
    private Double payment_amount;
    private long fees_id;

    public AddStudentPaymentList(long payment_month, Double payment_amount, long fees_id) {
        this.payment_month = payment_month;
        this.payment_amount = payment_amount;
        this.fees_id = fees_id;
    }

    public long getPayment_month() {
        return payment_month;
    }

    public void setPayment_month(long payment_month) {
        this.payment_month = payment_month;
    }

    public Double getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(Double payment_amount) {
        this.payment_amount = payment_amount;
    }

    public long getFees_id() {
        return fees_id;
    }

    public void setFees_id(long fees_id) {
        this.fees_id = fees_id;
    }
}
