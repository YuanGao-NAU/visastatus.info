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

@Service
public class CaseWithHistoryServiceImpl implements CaseWithHistoryService {

    @Autowired
    private CaseDao caseDao;

    @Autowired
    private CaseHistoryDao caseHistoryDao;

    @Override
    public List<CaseWithHistory> getCasesWithHistory(String startDate, String endDate) {
        List<CaseWithHistory> list = new ArrayList<>();
        List<Case> cases = caseDao.getCaseByDateInterval(startDate, endDate);
        for(Case aCase : cases) {
            List<CaseHistory> caseHistoryList = caseHistoryDao.getCaseHistory(aCase.getCid());
            aCase.hidePersonalInfo();
            list.add(new CaseWithHistory(aCase, caseHistoryList));
        }
        return list;
    }
}
