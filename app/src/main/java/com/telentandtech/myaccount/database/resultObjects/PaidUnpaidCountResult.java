package com.telentandtech.myaccount.database.resultObjects;

public class PaidUnpaidCountResult {

    private Long totalCount;
    private Long paidCount;
    private Long unpaidCount;

    public PaidUnpaidCountResult(Long totalCount, Long paidCount, Long unpaidCount) {
        this.totalCount = totalCount;
        this.paidCount = paidCount;
        this.unpaidCount = unpaidCount;
    }

    public Long getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(Long paidCount) {
        this.paidCount = paidCount;
    }

    public Long getUnpaidCount() {
        return unpaidCount;
    }

    public void setUnpaidCount(Long unpaidCount) {
        this.unpaidCount = unpaidCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
