package com.example.demo.controller;

import com.example.demo.entity.CaseCount;
import com.example.demo.entity.CaseWithHistory;
import com.example.demo.service.CaseWithHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cases")
public class CaseWithHistoryController {

    @Autowired
    private CaseWithHistoryService caseWithHistoryService;

    @RequestMapping("list")
    public Map<String, Object> getTotalCountByDateInterval(
            @RequestParam(name = "startDate", required = false, defaultValue = "2010-01-01")
            String startDate,
            @RequestParam(name = "endDate", required = false, defaultValue = "2099-01-01")
            String endDate,
            @RequestParam(name = "category", required = false, defaultValue = "")
            String category
    ) {
        Map<String, Object> map = new HashMap<>();
        if(category.equals("")) {
            map.put("cases", caseWithHistoryService.getCasesWithHistory(startDate, endDate));
        } else {
            map.put("cases", caseWithHistoryService.getCasesWithHistory(startDate, endDate, category));
        }
        return map;
    }
}
