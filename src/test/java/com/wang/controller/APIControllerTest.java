package com.wang.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

public class APIControllerTest {
    private static  final Logger logger = LoggerFactory.getLogger(APIController.class);




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
