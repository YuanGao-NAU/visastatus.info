package com.example.demo.service.impl;

import com.example.demo.dao.CaseDao;
import com.example.demo.dao.CaseHistoryDao;
import com.example.demo.entity.Case;
import com.example.demo.entity.CaseCount;
import com.example.demo.service.CaseService;
import com.example.demo.utils.Utils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CaseServiceImpl implements CaseService {
    @Autowired
    private CaseDao caseDao;

    @Autowired
    private CaseHistoryDao caseHistoryDao;


    @Override
    public void addCase(Case newCase) {
        caseDao.addCase(newCase);
    }

    @Override
    public Case getCase(String cid) {
        return caseDao.getCase(cid);
    }

    @Override
    public List<Case> getAllCases() {
        return caseDao.getAllCases();
    }

    @Override
    public List<Case> getCaseByDate(String date) {
        return caseDao.getCaseByDate(date);
    }

    @Override
    public List<Case> getCaseByDateInterval(String startDate, String endDate) {
        return caseDao.getCaseByDateInterval(startDate, endDate);
    }

    public List<Case> getCaseByDateInterval(String startDate, String endDate, String category) {
        return caseDao.getCaseByDateIntervalAndCategory(startDate, endDate, category);
    }

    public Map<String, Integer> getTotalCount() {
        return caseDao.getTotalCount();
    }

    public List<CaseCount> getTotalCountByDateInterval(String startDate, String endDate) {
        return caseDao.getTotalCountByDateInterval(startDate, endDate);
    }

    public List<Case> getTrackableCases() {
        return caseDao.getTrackableCases(Utils.getDateOfToday());
    }

    public void updateCase(String cid, String updatedDate, String status) {
        caseDao.updateCase(cid, updatedDate, status);
    }

    public void removeCase(String cid) {
        caseDao.removeCase(cid);
        caseHistoryDao.removeCaseHistory(cid);
    }

    public void updateCaseEmail(String cid, String email) {
        caseDao.updateCaseEmail(cid, email);
    }

    public void updateCaseDone(String cid) {
        caseDao.updateCaseDone(cid);
    }
}
