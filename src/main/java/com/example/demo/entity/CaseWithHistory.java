package com.example.demo.entity;

import java.util.List;

public class CaseWithHistory{

    private List<CaseHistory> caseHistory;
    private Case aCase;

    public CaseWithHistory() {
        this.caseHistory = null;
        aCase = null;
    }

    public CaseWithHistory(Case aCase, List<CaseHistory> caseHistory) {
        this.aCase = aCase;
        this.caseHistory = caseHistory;
    }

    public List<CaseHistory> getCaseHistory() {
        return caseHistory;
    }

    public void setCaseHistory(List<CaseHistory> caseHistory) {
        this.caseHistory = caseHistory;
    }

    public Case getaCase() {
        return aCase;
    }

    public void setaCase(Case aCase) {
        this.aCase = aCase;
    }
}
