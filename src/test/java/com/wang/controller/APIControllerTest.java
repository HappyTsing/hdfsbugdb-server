package com.wang.controller;

import com.wang.dto.Issues;
import com.wang.dto.Result;
import com.wang.service.impl.IssueInfoServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

public class APIControllerTest {
    private static  final Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    private IssueInfoServiceImpl issueInfoService;



    @GetMapping("/classify")
    public String classify(){
        logger.info("Enter APIController/classify");
        return "hello";
    }
//
//    @Test
//    public void  issues(){
//        logger.info("Enter APIController/issues");
//        int code = HttpStatus.OK.value();
//        Issues i = new Issues("1","quality");
//        Result result=  new Result<Issues>(code,i);
////        Result result=  new Result<>(code,1);
//        logger.info(result.toString());
//    }
}
