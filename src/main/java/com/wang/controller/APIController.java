package com.wang.controller;

import com.wang.dto.Issues;
import com.wang.dto.Result;
import com.wang.service.ClassifyService;
import com.wang.service.IssueInfoService;
import com.wang.service.JedisService;
import com.wang.utils.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 1. 可以直接返回HashMap，当使用@RestController 或 @Controller和@ResponseBody 的时候，会自动转换为json
 * 2. 返回一个对象，对象也会自动转化为json格式，本项目都讲返回值包装在Result对象中
 */
@RestController
@RequestMapping("/api")
public class APIController {
    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private IssueInfoService issueInfoService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private HttpServletRequest request;


    @GetMapping("/classify")
    public String classify() {

        String uri = request.getRequestURI();
        String hashKey = String.valueOf(uri.hashCode());
        String result;
        Result<Map<String, Map<String, Integer>>> resultObj = null;
        if (jedisService.isExisted(hashKey)) {
            logger.info(uri + " Redis Cache Success");
            result = jedisService.get(hashKey);
        } else {
            logger.info(uri + " Redis Cache Failure");
            try {

                Map<String, Map<String, Integer>> map = classifyService.getClassify();
                resultObj = new Result<Map<String, Map<String, Integer>>>(HttpStatus.OK, map);
                logger.info(uri + " Search From Mysql Success");
                result = SerializationUtil.getBeanToJson(resultObj);
                logger.info(uri + " Try Set to Redis");
                jedisService.set(hashKey, result);
            } catch (Exception e) {
                logger.info(uri + " Search From Mysql Failure");
                resultObj = new Result<Map<String, Map<String, Integer>>>(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
                result = SerializationUtil.getBeanToJson(resultObj);
            }
        }
        return result;
    }


    @GetMapping("/issues/all/{pagesize}/{pagenum}")
    public String getAllIssues(@PathVariable(value = "pagesize") Integer pagesize, @PathVariable(value = "pagenum") Integer pagenum) {
        String uri = request.getRequestURI();
        String hashKey = String.valueOf(uri.hashCode());
        String result;
        Result<Issues> resultObj = null;
        if (jedisService.isExisted(hashKey)) {
            logger.info(uri + " Redis Cache Success");
            result = jedisService.get(hashKey);
        } else {
            logger.info(uri + " Redis Cache Failure");
            try {
                Issues issues = new Issues();
                List<Map<String, Object>> list = issueInfoService.pageQueryByNum(pagesize, pagenum);
                Integer sum = list.size();
                issues.setSum(sum);
                issues.setPageNum(pagenum);
                issues.setPageSize(pagesize);
                issues.setData(list);
                if (pagenum == 1) {
                    Integer vitalCount = classifyService.vitalCount();
                    issues.setTotal(vitalCount);
                }
                resultObj = new Result<Issues>(HttpStatus.OK, issues);
                logger.info(uri + " Search From Mysql Success");
                result = SerializationUtil.getBeanToJson(resultObj);
                logger.info(uri + " Try Set to Redis");
                jedisService.set(hashKey, result);
            } catch (Exception e) {
                logger.info(uri + " Search From Mysql Failure");
                resultObj = new Result<Issues>(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
                result = SerializationUtil.getBeanToJson(resultObj);
            }
        }
        return result;
    }


    @GetMapping("/issues/search/{pagesize}/{pagenum}/{searchType}/{searchValue}")
    public String getIssueByIssueKey(@PathVariable(value = "pagesize") Integer pagesize, @PathVariable(value = "pagenum") Integer pagenum, @PathVariable(value = "searchType") String searchType, @PathVariable(value = "searchValue") String searchValue) {
        String uri = request.getRequestURI();
        String hashKey = String.valueOf(uri.hashCode());
        String result;
        Result<Issues> resultObj = null;
        if (jedisService.isExisted(hashKey)) {
            logger.info(uri + " Redis Cache Success");
            result = jedisService.get(hashKey);
        } else {
            logger.info(uri + " Redis Cache Failure");
            try {
                Issues issues = new Issues();
                int sum;
                List<Map<String, Object>> data;
                /**
                 * 第一次获取时，为了拿到total，先查询所有的内容。传入MAX_SIZE，由于数据库一共只有381条数据，因此可以查询到所有的数据。
                 */
                if (pagenum == 1) {
                    Integer MAX_SIZE = 500;
                    List<Map<String, Object>> list = issueInfoService.queryByValue(MAX_SIZE, pagenum, searchType, searchValue);
                    Integer total = list.size();
                    issues.setTotal(total);

                    if (total < pagesize) {
                        sum = total;
                        data = list;
                    } else {
                        sum = pagesize;
                        data = list.subList(0, pagesize);
                    }
                } else {
                    List<Map<String, Object>> list = issueInfoService.queryByValue(pagesize, pagenum, searchType, searchValue);
                    sum = list.size();
                    data = list;
                }
                issues.setSum(sum);
                issues.setPageNum(pagenum);
                issues.setPageSize(pagesize);
                issues.setData(data);
                resultObj = new Result<Issues>(HttpStatus.OK, issues);
                logger.info(uri + " Search From Mysql Success");
                result = SerializationUtil.getBeanToJson(resultObj);
                logger.info(uri + " Try Set to Redis");
                jedisService.set(hashKey, result);
            } catch (Exception e) {
                logger.info(uri + " Search From Mysql Failure");
                resultObj = new Result<Issues>(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
                result = SerializationUtil.getBeanToJson(resultObj);
            }
        }
        return result;
    }
}


    /* 无缓存版本
     @GetMapping("/issues/all/{pagesize}/{pagenum}")
     public Result<Issues> getAllIssues(@PathVariable(value="pagesize") Integer pagesize, @PathVariable(value="pagenum") Integer pagenum){
     System.out.println(request.getRequestURI());

     Issues issues = new Issues();
     List<Map<String,Object>> list=issueInfoService.pageQueryByNum(pagesize,pagenum);
     Integer sum = list.size();
     issues.setSum(sum);
     issues.setPageNum(pagenum);
     issues.setPageSize(pagesize);
     issues.setData(list);
     if(pagenum==1){
     Integer vitalCount = classifyService.vitalCount();
     issues.setTotal(vitalCount);
     }
     return  new Result<Issues>(HttpStatus.OK,issues);
     }
     */
