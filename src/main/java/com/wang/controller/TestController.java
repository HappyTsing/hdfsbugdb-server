package com.wang.controller;

import com.wang.pojo.IssueInfo;
import com.wang.service.impl.IssueInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @Autowired
    private IssueInfoServiceImpl issueInfoService;


    @GetMapping ("url")
    public String test(){
        System.out.println("进入了url");
        return "hello";
    }

    @GetMapping ("url2")

    public String test2(){
        return "hello2";
    }

    @GetMapping("db")
    public IssueInfo test3(){
        System.out.println("进入了db");
        return issueInfoService.getdata();
    }
}