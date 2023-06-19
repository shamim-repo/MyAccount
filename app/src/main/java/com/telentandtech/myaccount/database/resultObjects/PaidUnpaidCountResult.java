package com.telentandtech.myaccount.database.resultObjects;

public class PaidUnpaidCountResult {
    private Long paidCount;
    private Long unpaidCount;
    private Long totalCount;

    public PaidUnpaidCountResult(Long paidCount, Long unpaidCount, Long totalCount) {
        this.paidCount = paidCount;
        this.unpaidCount = unpaidCount;
        this.totalCount = totalCount;
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
