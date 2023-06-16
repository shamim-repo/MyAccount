package com.telentandtech.myaccount.database.resultObjects;

import java.util.List;

public class ClassNameIdListResult {

        private List<ClassNameId> classNameIdList;
        private Boolean isSuccessful;
        private String message;

        public ClassNameIdListResult(List<ClassNameId> classNameIdList, Boolean isSuccessful, String message) {
            this.classNameIdList = classNameIdList;
            this.isSuccessful = isSuccessful;
            this.message = message;
        }

    public List<ClassNameId> getClassNameIdList() {
        return classNameIdList;
    }

    public void setClassNameIdList(List<ClassNameId> classNameIdList) {
        this.classNameIdList = classNameIdList;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
