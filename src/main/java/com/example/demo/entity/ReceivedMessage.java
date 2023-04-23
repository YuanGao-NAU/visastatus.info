package com.example.demo.entity;

public class ReceivedMessage {
    private String status;
    private String submitDate;
    private String statusDate;
    private String message;
    private String actualStatus;

    public ReceivedMessage() {
    }

    public ReceivedMessage(String status, String submitDate, String statusDate, String message, String actualStatus) {
        this.status = status;
        this.submitDate = submitDate;
        this.statusDate = statusDate;
        this.message = message;
        this.actualStatus = actualStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
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
