package com.wang.controller;

import com.wang.dto.Result;
import com.wang.service.ClassifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIController {
    private static  final Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    private ClassifyService classifyService;



    @GetMapping ("/classify")
    public Result classify(){
        try{
            Map<String, Map<String, Integer>> map = classifyService.getClassify();
            return new Result(HttpStatus.OK,map);
        }catch (Exception e){
            return new Result(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
        }
    }

//    @GetMapping("/issues/{pagesize}/{pagenum}")
//    public Result<Map> issues(@PathVariable(value="pagesize") String pagesize, @PathVariable(value="pagenum") String pagenum){
//        logger.info("Enter APIController/issues");
//        return  new Result<Map>(HttpStatus.OK,new Issues("1","quality",200));
//    }
}