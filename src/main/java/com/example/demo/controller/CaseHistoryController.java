package com.example.demo.controller;

import com.example.demo.entity.Case;
import com.example.demo.entity.CaseHistory;
import com.example.demo.service.CaseHistoryService;
import com.example.demo.service.CaseService;
import com.example.demo.utils.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/casehistory")
public class CaseHistoryController {

    @Autowired
    private CaseHistoryService caseHistoryService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private MailUtils mailUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(CaseHistoryController.class);

    @RequestMapping("list")
    public List<CaseHistory> getCaseHistory(@RequestParam(name = "cid", required = true)
                                                String cid,
                                            @RequestParam(name = "email", required = false)
                                                    String email) {
        cid = cid.toUpperCase();
        LOGGER.info(String.format("Searching for history of case: %s, the email address is : %s", cid, (email==null || email.length()==0) ? "" : email ));
        if(email != null && email.length() != 0) {
            Case newCase = caseService.getCase(cid);
            if(newCase != null) {
                if (newCase.getEmail() == null || newCase.getEmail().length() == 0) {
                    mailUtils.sendSimpleMail(
                            email,
                            "Case has been submitted!",
                            "To make sure you can receive updates in time, please add this email address to your contact list."
                    );
                }
            } else {
                for (Case aCase : CaseController.cases) {
                    if(aCase.getCid().equals(cid)) {
                        aCase.setEmail(email);
                        mailUtils.sendSimpleMail(
                                email,
                                "Case has been submitted!",
                                "Case has been submitted. However, CEAC website is currently unavailable for us, will notify you when we are able to process." + "\n\n" +
                                        "To make sure you can receive updates in time, please add this email address to your contact list."
                        );
                    }
                }

            }
            caseService.updateCaseEmail(cid, email);
        }

        return caseHistoryService.getCaseHistory(cid);
    }

    @RequestMapping("add")
    public void addCaseHistory(CaseHistory caseHistory) {
        return;
    }
}
