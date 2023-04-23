package com.example.demo.service;

import com.example.demo.entity.Case;
import com.example.demo.entity.CaseCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CaseService {

    void addCase(Case newCase);

    Case getCase(String cid);

    List<Case> getAllCases();

    List<Case> getCaseByDate(String date);

    List<Case> getCaseByDateInterval(String startDate, String endDate);

    public Map<String, Integer> getTotalCount();

    public List<CaseCount> getTotalCountByDateInterval(String startDate, String endDate);

    public List<Case> getTrackableCases();

    public void updateCase(String cid, String updatedDate, String status);

    public void removeCase(String cid);

    public void updateCaseEmail(String cid, String email);

    public void updateCaseDone(String cid);
}
