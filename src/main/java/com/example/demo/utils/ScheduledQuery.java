package com.example.demo.utils;

import com.example.demo.controller.CaseController;
import com.example.demo.entity.Case;
import com.example.demo.entity.CaseHistory;
import com.example.demo.entity.ReceivedMessage;
import com.example.demo.service.CaseHistoryService;
import com.example.demo.service.CaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ScheduledQuery {

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private CaseService caseService;

    @Autowired
    private CaseHistoryService caseHistoryService;

    ReentrantLock lock =  new  ReentrantLock( true );

    private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledQuery.class);

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.HOURS)
    public void updateFromDatabase() {
        List<Case> caseList = caseService.getTrackableCases();
        Collections.shuffle(caseList);
        lock.lock();
        scheduledTask(caseList);
        lock.unlock();
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void updateFromCache() {
        List<Case> caseList = new ArrayList<>();
        caseList.addAll(CaseController.cases);
        lock.lock();
        scheduledTask(caseList);
        lock.unlock();
    }

    public void scheduledTask(List<Case> caseList) {

        LOGGER.info(String.format("There are %d cases waiting for update", caseList.size()));

        for(Case item: caseList) {
            params.remove("loc");
            params.remove("caseNumber");
            params.add("loc", Utils.LOCATIONS.get(item.getLocation()));
            params.add("caseNumber", item.getCid());
            params.add("passportID", item.getPassportID());
            params.add("surname", item.getSurname());
            try {
                ReceivedMessage receivedMessage = Utils.sendPostRequest("http://localhost:8081/getStatus", params);

                while (receivedMessage.getStatus().equals("invalidCaptcha")) {       //验证码错误，重复查询直到验证码识别正确
                    receivedMessage = Utils.sendPostRequest("http://localhost:8081/getStatus", params);
                }

                boolean updated = true;

                LOGGER.info(String.format("Working on to update the status of %s, status is %s", item.getCid(), receivedMessage.getStatus()));

                if (receivedMessage.getStatus().equals("Rejected") || receivedMessage.getStatus().equals("Pending") || receivedMessage.getStatus().equals("Issued")) {

                    if("Issued".equals(receivedMessage.getActualStatus())) {
                        caseService.updateCaseDone(item.getCid());
                    }

                    String temp = Utils.convertDateToRightFormat(receivedMessage.getStatusDate());

                    List<CaseHistory> caseHistoryList = caseHistoryService.getCaseHistory(item.getCid());
                    if (caseHistoryList != null && caseHistoryList.size() != 0) {
                        for (CaseHistory caseHistory : caseHistoryList) {
                            if (
                                    caseHistory.getUpdatedDate().equals(temp) &&
                                            caseHistory.getStatus().equals(receivedMessage.getStatus())
                            ) {
                                updated = false;
                                break;
                            }
                        }
                    }
                    if (updated) {
                        caseHistoryService.addCaseHistory(new CaseHistory(item.getCid(), temp, receivedMessage.getStatus(), receivedMessage.getMessage(), receivedMessage.getActualStatus()));
                        if(CaseController.cases.contains(item)) {
                            CaseController.cases.remove(item);
                            item.setUpdatedDate(temp);
                            item.setStatus(receivedMessage.getStatus());
                            item.setNotes("");
                            caseService.addCase(item);
                        }
                        if (item.getEmail() != null && item.getEmail().length() != 0) {
                            mailUtils.sendSimpleMail(
                                    item.getEmail(),
                                    "Case status update",
                                    receivedMessage.getActualStatus() + "\n\n" +
                                            "To make sure you can receive updates in time, please add this email address to your contact list."
                            );

//                            mailUtils.sendSimpleMail(
//                                    "gaoyuan199325@gmail.com",
//                                    "Case status update",
//                                    receivedMessage.getActualStatus() + "\n\n" +
//                                            "To make sure you can receive updates in time, please add this email address to your contact list."
//                            );
                            LOGGER.info(String.format("Email has been sent to %s for case %s with status update: %s", item.getEmail(), item.getCid(), receivedMessage.getActualStatus()));
                        }
                        caseService.updateCase(item.getCid(), temp, receivedMessage.getStatus());
                        LOGGER.info(String.format("Status of case %s has been updated!", item.getCid()));
                    }
                }

                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {

                }

            } catch(RestClientException e) {
                LOGGER.info(String.format("Network error when updating the cases"));
            }
        }
    }
}
