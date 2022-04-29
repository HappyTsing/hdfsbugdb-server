package com.wang.utils;

import com.wang.BaseJunitTest;
import com.wang.dto.Result;
import com.wang.service.ClassifyService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class SerializationUtilTest extends BaseJunitTest {

    @Autowired
    ClassifyService classifyService;


    @Test
    public void getBeanToJsonTest() throws Exception {
        Map<String, Map<String, Integer>> map = classifyService.getClassify();

        Result<Map<String, Map<String, Integer>>> result =new Result<Map<String, Map<String, Integer>>>(HttpStatus.OK,map);
        String jsonStr = SerializationUtil.getBeanToJson(result);
        System.out.println(jsonStr);
    }


    @Test
    public void getJsonToBeanTest() throws Exception {
        Map<String, Map<String, Integer>> map = classifyService.getClassify();

        Result<Map<String, Map<String, Integer>>> result =new Result<Map<String, Map<String, Integer>>>(HttpStatus.OK,map);
        String jsonStr = SerializationUtil.getBeanToJson(result);

        Result jsonToMap = SerializationUtil.getJsonToBean(jsonStr,result.getClass());
        System.out.println(jsonToMap);
    }

    @Test
    public void hashTest(){
        String str = "/issues/classify";
        System.out.println(str.hashCode());
    }


}
