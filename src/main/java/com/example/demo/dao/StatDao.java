package com.example.demo.dao;

import com.example.demo.entity.CaseCountWithTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatDao {

    public List<CaseCountWithTime> getAllStatistics(@Param("status")String status);
    public List<CaseCountWithTime> getStatisticsByDateInterval(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("status")String status);
}
