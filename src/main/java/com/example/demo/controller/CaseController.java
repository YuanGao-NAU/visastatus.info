package com.example.demo.controller;

import com.example.demo.entity.Case;
import com.example.demo.entity.CaseCount;
import com.example.demo.entity.CaseHistory;
import com.example.demo.entity.ReceivedMessage;
import com.example.demo.service.CaseHistoryService;
import com.example.demo.service.CaseService;
import com.example.demo.service.StatService;
import com.example.demo.utils.MailUtils;
import com.example.demo.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/case")
public class CaseController {

    public static Set<Case> cases = new HashSet<>();

    @Autowired
    private CaseService caseService;

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private CaseHistoryService caseHistoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CaseController.class);

    @RequestMapping("add")
    public ReceivedMessage add(@RequestBody Case newCase) {

        newCase.setAutoRefreshExpireDate(Utils.getAutoExpirationDate(newCase.getInterviewDate()));
        if(!Utils.LOCATIONS.containsKey(newCase.getLocation())) return null;

        if(newCase.getCid() == null) return null;

        if(newCase.getCid() != null) {
            newCase.setCid(newCase.getCid().toUpperCase());
        }

        if(caseService.getCase(newCase.getCid()) != null || cases.contains(newCase)) {
            LOGGER.info(String.format("duplicated case detected: %s", newCase));
            return new ReceivedMessage("duplicatedCase", null, null, null, null);
        }

        LOGGER.info(newCase.toString());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("loc", Utils.LOCATIONS.get(newCase.getLocation()));
        params.add("caseNumber", newCase.getCid());
        ReceivedMessage receivedMessage = Utils.sendPostRequest("http://localhost:8081/getStatus", params);

        while(receivedMessage.getStatus().equals("invalidCaptcha")) {       //验证码错误，重复查询直到验证码识别正确
            receivedMessage = Utils.sendPostRequest("http://localhost:8081/getStatus", params);
        }

        if(receivedMessage.getStatus().equals("Rejected") || receivedMessage.getStatus().equals("Pending") || receivedMessage.getStatus().equals("Issued")) {
            String temp = Utils.convertDateToRightFormat(receivedMessage.getStatusDate());
            caseHistoryService.addCaseHistory(new CaseHistory(newCase.getCid(), temp, receivedMessage.getStatus(), receivedMessage.getMessage(), receivedMessage.getActualStatus()));

            newCase.setUpdatedDate(temp);
            newCase.setStatus(receivedMessage.getStatus());
            if(newCase.getEmail() == null || newCase.getEmail().length() == 0) {
                newCase.setEmail("");
                LOGGER.info(String.format("Case %s has been saved!", newCase.getCid()));
            } else {
                LOGGER.info(String.format("Case %s has been saved!, the email address is : %s", newCase.getCid(), newCase.getEmail()));
            }
            newCase.setNotes("");
            caseService.addCase(newCase);
            while(caseService.getCase(newCase.getCid()) == null);       //在数据库中找不到当前保存的case就不退出循环

            if(newCase.getEmail() != null && newCase.getEmail().length() != 0) {
                mailUtils.sendSimpleMail(
                        newCase.getEmail(),
                        "Case has been submitted",
                        "current status: " + receivedMessage.getActualStatus() +  "\n\n" +
                                "To make sure you can receive updates in time, please add this email address to your contact list."
                );

//                mailUtils.sendSimpleMail(
//                        "gaoyuan199325@gmail.com",
//                        "Case has been submitted",
//                        "current status: " + receivedMessage.getActualStatus() + ", for case: " + newCase.getCid() +  "\n\n" +
//                                "To make sure you can receive updates in time, please add this email address to your contact list."
//                );

                LOGGER.info(String.format("Email has been sent to %s for case %s with status: %s", newCase.getEmail(), newCase.getCid(), receivedMessage.getActualStatus()));
            }
        } else if(receivedMessage.getStatus().equals("serverDown")) {
            if(!cases.contains(newCase)) {

                cases.add(newCase);
                LOGGER.info("Case has been cached: " + newCase);
                receivedMessage.setMessage("We have received your case, but CEAC is not available now for us. Will process your case later!");

                if (newCase.getEmail() != null && newCase.getEmail().length() != 0) {
                    mailUtils.sendSimpleMail(
                            newCase.getEmail(),
                            "Case has been submitted",
                            "CEAC website is currently unavailable for us, will notify you when we are able to process." + "\n\n" +
                                    "To make sure you can receive updates in time, please add this email address to your contact list."
                    );

//                    mailUtils.sendSimpleMail(
//                            "gaoyuan199325@gmail.com",
//                            "Case has been submitted",
//                            "CEAC website is currently unavailable for us, will notify you when we are able to process." + "\n\n" +
//                                    "To make sure you can receive updates in time, please add this email address to your contact list."
//                    );

                    LOGGER.info(String.format("Email has been sent to %s for case %s", newCase.getEmail(), newCase.getCid()));
                }
            } else {
                receivedMessage.setStatus("duplicatedCase");
            }
        }

        return receivedMessage;
    }

    @RequestMapping("list")
    public List<Case> list() {
        return caseService.getAllCases();
    }

    @RequestMapping("getTotalCountByDateInterval")
    public List<CaseCount> getTotalCountByDateInterval(
            @RequestParam(name = "startDate", required = false, defaultValue = "2010-01-01")
            String startDate,
            @RequestParam(name = "endDate", required = false, defaultValue = "2099-01-01")
            String endDate
            ) {
        return caseService.getTotalCountByDateInterval(startDate, endDate);
    }

    @RequestMapping("remove")
    public  void remove(
            @RequestBody Case newCase
    ) {
        caseService.removeCase(newCase.getCid());
        LOGGER.info(String.format("Case %s has been removed!", newCase.getCid()));
    }

    @RequestMapping("done")
    public void updateCaseDone(@RequestParam(name = "cid", required = true)
                                   String cid) {
        caseService.updateCaseDone(cid);
    }
}
