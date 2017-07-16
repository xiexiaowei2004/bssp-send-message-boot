package com.powerbridge.bssp.saspass.controller;

import com.powerbridge.bssp.saspass.entity.SasPassportBsc;
import com.powerbridge.bssp.saspass.service.ISasPassportBscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 宋轲 on 2017/7/10.
 */
@RestController
public class TestController {

    @Autowired
    private ISasPassportBscService sasPassportBscService;

    @GetMapping("/insert")
    public Object testInsert(){
        return sasPassportBscService.insert(new SasPassportBsc());
    }

    @GetMapping("/select")
    public Object selectByUid(){
        return sasPassportBscService.selectByUid("402885815d109ce4015d173a59630b3e");
    }
}
