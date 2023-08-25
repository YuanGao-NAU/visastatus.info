package com.example.demo.dao;

import com.example.demo.entity.Case;
import com.example.demo.entity.CaseCount;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface CaseDao {

    public void addCase(Case newCase);

    public Case getCase(String cid);

    public List<Case> getAllCases();

    public List<Case> getCaseByDate(String date);

    public List<Case> getCaseByDateInterval(@Param("startDate")String startDate, @Param("endDate")String endDate);
    public List<Case> getCaseByDateIntervalAndCategory(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("category")String category);

    public Map<String, Integer> getTotalCount();

    public List<CaseCount> getTotalCountByDateInterval(@Param("startDate")String startDate, @Param("endDate")String endDate);

    public List<Case> getTrackableCases(@Param("dateOfToday")String dateOfToday);

    public void updateCase(@Param("cid")String cid, @Param("updatedDate")String updatedDate, @Param("status")String status);

    public void removeCase(String cid);

    public void updateCaseEmail(@Param("cid")String cid, @Param("email")String email);

    public void updateCaseDone(@Param("cid")String cid);
}
