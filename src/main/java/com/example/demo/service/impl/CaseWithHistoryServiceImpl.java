package com.example.demo.service.impl;

import com.example.demo.dao.CaseDao;
import com.example.demo.dao.CaseHistoryDao;
import com.example.demo.entity.Case;
import com.example.demo.entity.CaseHistory;
import com.example.demo.entity.CaseWithHistory;
import com.example.demo.service.CaseWithHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaseWithHistoryServiceImpl implements CaseWithHistoryService {

    @Autowired
    private CaseDao caseDao;

    @Autowired
    private CaseHistoryDao caseHistoryDao;

    @Override
    public List<CaseWithHistory> getCasesWithHistory(String startDate, String endDate) {
        return caseDao.getCaseByDateInterval(startDate, endDate).stream()
                .map(aCase -> {
                    List<CaseHistory> caseHistoryList = caseHistoryDao.getCaseHistory(aCase.getCid());
                    aCase.hidePersonalInfo();
                    return new CaseWithHistory(aCase, caseHistoryList);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseWithHistory> getCasesWithHistory(String startDate, String endDate, String category) {
        return caseDao.getCaseByDateIntervalAndCategory(startDate, endDate, category).stream()
                .map(aCase -> {
                    List<CaseHistory> caseHistoryList = caseHistoryDao.getCaseHistory(aCase.getCid());
                    aCase.hidePersonalInfo();
                    return new CaseWithHistory(aCase, caseHistoryList);
                })
                .collect(Collectors.toList());
    }
}
