package com.example.demo.entity;

public class CaseCountWithTime {

    private String status;
    private int count;
    private String time;

    public CaseCountWithTime() {
    }

    public CaseCountWithTime(String status, int count, String time) {
        this.status = status;
        this.count = count;
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
