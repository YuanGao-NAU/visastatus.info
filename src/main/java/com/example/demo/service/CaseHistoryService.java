package com.example.demo.service;

import com.example.demo.entity.CaseHistory;

import java.util.List;

public interface CaseHistoryService {

    List<CaseHistory> getCaseHistory(String cid);

    void addCaseHistory(CaseHistory caseHistory);
}
