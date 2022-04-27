package com.wang.controller;

import com.wang.dto.Issues;
import com.wang.dto.Result;
import com.wang.pojo.IssueInfo;
import com.wang.service.impl.IssueInfoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class APIController {
    private static  final Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    private IssueInfoServiceImpl issueInfoService;



    @GetMapping ("/classify")
    public String classify(){
        logger.info("Enter APIController/classify");
        return "hello";
    }


    @GetMapping("/issues/{pagesize}/{pagenum}")
    public Result<Issues> issues(@PathVariable(value="pagesize") String pagesize, @PathVariable(value="pagenum") String pagenum){
        logger.info("Enter APIController/issues");

        return  new Result<Issues>(HttpStatus.OK,new Issues("1","quality",200));
    }
}