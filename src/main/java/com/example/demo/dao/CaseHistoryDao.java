package com.example.demo.dao;

import com.example.demo.entity.CaseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CaseHistoryDao {

    public List<CaseHistory> getCaseHistory(String cid);

    public void addCaseHistory(CaseHistory caseHistory);

    public void removeCaseHistory(String cid);
}
