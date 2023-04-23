package com.example.demo.controller;

import com.example.demo.entity.CaseCount;
import com.example.demo.entity.CaseCountWithTime;
import com.example.demo.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stat")
public class StatController {
    @Autowired
    private StatService statService;

    @RequestMapping("getStatisticsByDateInterval")
    public Map<String, Object> getTotalCountByDateInterval(
            @RequestParam(name = "startDate", required = false, defaultValue = "2010-01-01")
            String startDate,
            @RequestParam(name = "endDate", required = false, defaultValue = "2022-04-01")
            String endDate
    ) {
        return statService.getStatisticsByDateInterval(startDate, endDate);
    }

    @RequestMapping("getAllStatistics")
    public Map<String, Object> getAllStatistics() {
        return statService.getAllStatistics();
    }

    @RequestMapping("getStatisticsOfThisMonth")
    public Map<String, Object> getStatisticsOfThisMonth() {
        return statService.getStatisticsOfThisMonth();
    }

    @RequestMapping("getStatisticsWithinOneYear")
    public Map<String, Object> getStatisticsWithinOneYear() {
        return statService.getStatisticsByWeek(new Date());
    }
}
