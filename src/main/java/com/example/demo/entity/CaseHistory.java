package com.example.demo.entity;

public class CaseHistory {

    private String cid;             //case id
    private String updatedDate;     //when it was updated
    private String status;          //status updated
    private String message;         //message along with the update

    private String actualStatus;

    public CaseHistory() {
    }

    public CaseHistory(String cid, String updatedDate, String status, String message, String actualStatus) {
        this.cid = cid;
        this.updatedDate = updatedDate;
        this.status = status;
        this.message = message;
        this.actualStatus = actualStatus;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActualStatus() {
        return actualStatus;
    }

    public void setActualStatus(String actualStatus) {
        this.actualStatus = actualStatus;
    }
}
