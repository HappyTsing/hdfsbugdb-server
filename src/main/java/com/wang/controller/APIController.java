package com.wang.controller;

import com.wang.dao.IssueInfoMapper;
import com.wang.dto.Issues;
import com.wang.dto.Result;
import com.wang.service.ClassifyService;
import com.wang.service.IssueInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 1. 可以直接返回HashMap，当使用@RestController 或 @Controller和@ResponseBody 的时候，会自动转换为json
 * 2. 返回一个对象，对象也会自动转化为json格式，本项目都讲返回值包装在Result对象中
 */
@RestController
@RequestMapping("/api")
public class APIController {
    private static  final Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private IssueInfoService issueInfoService;



    @GetMapping ("/classify")
    public Result<Map<String, Map<String, Integer>>> classify(){
        try{
            Map<String, Map<String, Integer>> map = classifyService.getClassify();
            return new Result<Map<String, Map<String, Integer>>>(HttpStatus.OK,map);
        }catch (Exception e){
            return new Result<Map<String, Map<String, Integer>>>(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
        }
    }


    @GetMapping("/issues/{pagesize}/{pagenum}")
    public Result<Issues> issues(@PathVariable(value="pagesize") Integer pagesize, @PathVariable(value="pagenum") Integer pagenum){
        logger.info("Enter APIController/issues");


        List<Map<String,Object>> list=issueInfoService.pageQueryByNum(pagesize,pagenum);
        Integer vitalCount = classifyService.vitalCount();
        Integer sum = list.size();
        Issues issues = new Issues(vitalCount,pagesize,pagenum, sum,list);
        return  new Result<Issues>(HttpStatus.OK,issues);
    }
}