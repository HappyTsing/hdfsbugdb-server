package com.wang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @GetMapping ("url")
    public String test(){
        return "hello";
    }

    @GetMapping ("url2")
    public String test2(){
        return "hello2";
    }
}