package com.example.demo.entity;

import java.util.List;

public class DataToPlot {

    private String type;
    private String name;
    private List<Integer> data;

    private int total;

    public DataToPlot() {
    }

    public DataToPlot(String type, String name, List<Integer> data, int total) {
        this.type = type;
        this.name = name;
        this.data = data;
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
