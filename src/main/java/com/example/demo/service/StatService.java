package com.example.demo.service;

import com.example.demo.entity.Case;
import com.example.demo.entity.CaseCountWithTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatService {

    public Map<String, Object> getStatisticsByDateInterval(String startDate, String endDate);

    public Map<String, Object> getAllStatistics();

    public Map<String, Object> getStatisticsOfThisMonth();

    public Map<String, Object> getStatisticsWithinOneYear();

    public Map<String, Object> getStatisticsByWeek(Date date);
}
