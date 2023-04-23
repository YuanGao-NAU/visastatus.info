package com.example.demo.service.impl;

import com.example.demo.dao.CaseDao;
import com.example.demo.dao.StatDao;
import com.example.demo.entity.Case;
import com.example.demo.entity.CaseCountWithTime;
import com.example.demo.entity.DataToPlot;
import com.example.demo.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;

    @Autowired
    private CaseDao caseDao;

    private static final String pattern = "yyyy-MM-dd";
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(pattern);

    @Override
    public Map<String, Object> getStatisticsByDateInterval(String startDate, String endDate) {
        List<CaseCountWithTime> rejectedCases = statDao.getStatisticsByDateInterval(startDate, endDate, "Rejected");
        List<CaseCountWithTime> issuedCases = statDao.getStatisticsByDateInterval(startDate, endDate, "Issued");
        List<CaseCountWithTime> pendingCases = statDao.getStatisticsByDateInterval(startDate, endDate, "Pending");

        List<Integer> rejectedData = new ArrayList<>();
        List<Integer> issuedData = new ArrayList<>();
        List<Integer> pendingData = new ArrayList<>();

        int rejectedTotal = 0;
        int issuedTotal = 0;
        int pendingTotal = 0;

        DataToPlot rejectedDataToPlot = new DataToPlot("bar", "Refused", null, 0);
        DataToPlot issuedDataToPlot = new DataToPlot("bar", "Issued", null, 0);
        DataToPlot pendingDataToPlot = new DataToPlot("bar", "Pending", null, 0);

        int dataLength = rejectedCases.size();

        for(int i = 0; i < dataLength; i++) {
            rejectedData.add(rejectedCases.get(i).getCount());
            issuedData.add(issuedCases.get(i).getCount());
            pendingData.add(pendingCases.get(i).getCount());

            rejectedTotal += rejectedCases.get(i).getCount();
            issuedTotal += issuedCases.get(i).getCount();
            pendingTotal += pendingCases.get(i).getCount();
        }

        rejectedDataToPlot.setData(rejectedData);
        issuedDataToPlot.setData(issuedData);
        pendingDataToPlot.setData(pendingData);

        rejectedDataToPlot.setTotal(rejectedTotal);
        issuedDataToPlot.setTotal(issuedTotal);
        pendingDataToPlot.setTotal(pendingTotal);

        List<String> dayList = getDayList(startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        result.put("rejectedData", rejectedDataToPlot);
        result.put("issuedData", issuedDataToPlot);
        result.put("pendingData", pendingDataToPlot);
        result.put("dayList", dayList);

//        List<Case>  list = caseDao.getCaseByDateInterval(startDate, endDate);
//        result.put("cases", list);

        return result;
    }

    @Override
    public Map<String, Object> getAllStatistics() {
        List<CaseCountWithTime> rejectedCases = statDao.getAllStatistics("Rejected");
        List<CaseCountWithTime> issuedCases = statDao.getAllStatistics("Issued");
        List<CaseCountWithTime> pendingCases = statDao.getAllStatistics("Pending");

        List<Integer> rejectedData = new ArrayList<>();
        List<Integer> issuedData = new ArrayList<>();
        List<Integer> pendingData = new ArrayList<>();

        int rejectedTotal = 0;
        int issuedTotal = 0;
        int pendingTotal = 0;

        DataToPlot rejectedDataToPlot = new DataToPlot("bar", "Refused", null, 0);
        DataToPlot issuedDataToPlot = new DataToPlot("bar", "Issued", null, 0);
        DataToPlot pendingDataToPlot = new DataToPlot("bar", "Pending", null, 0);

        int dataLength = rejectedCases.size();

        List<String> dayList = new ArrayList<>();

        for(int i = 0; i < dataLength; i++) {
            rejectedData.add(rejectedCases.get(i).getCount());
            issuedData.add(issuedCases.get(i).getCount());
            pendingData.add(pendingCases.get(i).getCount());

            rejectedTotal += rejectedCases.get(i).getCount();
            issuedTotal += issuedCases.get(i).getCount();
            pendingTotal += pendingCases.get(i).getCount();
            dayList.add(rejectedCases.get(i).getTime());
        }

        rejectedDataToPlot.setData(rejectedData);
        issuedDataToPlot.setData(issuedData);
        pendingDataToPlot.setData(pendingData);

        rejectedDataToPlot.setTotal(rejectedTotal);
        issuedDataToPlot.setTotal(issuedTotal);
        pendingDataToPlot.setTotal(pendingTotal);

        Map<String, Object> result = new HashMap<>();
        result.put("rejectedData", rejectedDataToPlot);
        result.put("issuedData", issuedDataToPlot);
        result.put("pendingData", pendingDataToPlot);
        result.put("dayList", dayList);

//        List<Case>  list = caseDao.getAllCases();
//        result.put("cases", list);
        return result;
    }

    @Override
    public Map<String, Object> getStatisticsOfThisMonth() {
        String monthBegin = LocalDate.now().withDayOfMonth(1).format(dateFormat);
        String monthEnd = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1).format(dateFormat);
        return getStatisticsByDateInterval(monthBegin, monthEnd);
    }

    @Override
    public Map<String, Object> getStatisticsWithinOneYear() {
        String endDate = LocalDate.now().format(dateFormat);
        String startDate = LocalDate.now().minusDays(366).format(dateFormat);
        return getStatisticsByDateInterval(startDate, endDate);
    }

    @Override
    public Map<String, Object> getStatisticsByWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String endDate = null;
        String startDate = null;
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        if(weekday == 2) {
            endDate = sdf.format(date);
            cal.add(Calendar.WEEK_OF_YEAR, -52);
            startDate = sdf.format(cal.getTime());
        } else if(weekday == 1) {
            cal.add(Calendar.DAY_OF_MONTH, -6);
            endDate = sdf.format(date);
            cal.add(Calendar.WEEK_OF_YEAR, -52);
            startDate = sdf.format(cal.getTime());
        } else {
            cal.add(Calendar.DAY_OF_MONTH, (2-weekday));
            endDate = sdf.format(date);
            cal.add(Calendar.WEEK_OF_YEAR, -52);
            startDate = sdf.format(cal.getTime());
        }

        cal.add(Calendar.WEEK_OF_YEAR, 53);
        endDate = sdf.format(cal.getTime());
        cal.add(Calendar.WEEK_OF_YEAR, -53);

        Map<String, Object> tempResult = getStatisticsByDateInterval(startDate, endDate);

        DataToPlot rejectedCases = ((DataToPlot) tempResult.get("rejectedData"));
        DataToPlot issuedCases = ((DataToPlot) tempResult.get("issuedData"));
        DataToPlot pendingCases = ((DataToPlot) tempResult.get("pendingData"));

        int rejectedTotal = 0;
        int issuedTotal = 0;
        int pendingTotal = 0;

        List<String> dayList = new ArrayList<>();

        List<Integer> rejectedDataPoints = rejectedCases.getData();
        List<Integer> issuedDataPoints = issuedCases.getData();
        List<Integer> pendingDataPoints = pendingCases.getData();

        List<Integer> newRejected = new ArrayList<>();
        List<Integer> newIssued = new ArrayList<>();
        List<Integer> newPending = new ArrayList<>();

        for(int i = 0; i <= 52; i++) {
            if(i != 0) {
                cal.add(Calendar.DAY_OF_MONTH, 7);
            }
            dayList.add(sdf.format(cal.getTime()));
            for(int j = 0; j < 7; j++) {
                rejectedTotal += rejectedDataPoints.get(i*7+j);
                issuedTotal += issuedDataPoints.get(i*7+j);
                pendingTotal += pendingDataPoints.get(i*7+j);
            }
            newRejected.add(rejectedTotal);
            newIssued.add(issuedTotal);
            newPending.add(pendingTotal);
            rejectedTotal = 0;
            issuedTotal = 0;
            pendingTotal = 0;
        }

        rejectedCases.setData(newRejected);
        issuedCases.setData(newIssued);
        pendingCases.setData(newPending);

        tempResult.put("rejectedData", rejectedCases);
        tempResult.put("issuedData", issuedCases);
        tempResult.put("pendingData", pendingCases);
        tempResult.put("dayList", dayList);

        return tempResult;
    }

    private static List<String> getDayList(String startDate, String endDate) {
        // 生成年月日的日期序列
        List<String> res = new ArrayList<>();
        LocalDate newStartDate =
                LocalDate.parse(startDate, dateFormat).minusDays(1);  // 这里先对startDate减一天，最后的结果才能包含startDate
        LocalDate newEndDate = LocalDate.parse(endDate, dateFormat);
        while (!newStartDate.equals(newEndDate)) {
            newStartDate = newStartDate.plusDays(1);
            String dateString = dateFormat.format(newStartDate);
            res.add(dateString);
        }
        return res;
    }
}
