package com.example.demo.service.impl;

import com.example.demo.dao.CaseHistoryDao;
import com.example.demo.entity.CaseHistory;
import com.example.demo.service.CaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class caseHistoryServiceImpl implements CaseHistoryService {

    @Autowired
    private CaseHistoryDao caseHistoryDao;

    @Override
    public List<CaseHistory> getCaseHistory(String cid) {
        return caseHistoryDao.getCaseHistory(cid);
    }

    @Override
    public void addCaseHistory(CaseHistory caseHistory) {
        caseHistoryDao.addCaseHistory(caseHistory);
    }
}
