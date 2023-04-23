package com.example.demo.service;

import com.example.demo.entity.CaseCount;
import com.example.demo.entity.CaseWithHistory;

import java.util.List;

public interface CaseWithHistoryService {

    public List<CaseWithHistory> getCasesWithHistory(String startDate, String endDate);
}
