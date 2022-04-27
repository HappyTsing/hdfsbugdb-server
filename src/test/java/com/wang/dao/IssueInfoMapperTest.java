package com.wang.dao;

import com.wang.BaseJunitTest;
import com.wang.service.LabelService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class IssueInfoMapperTest extends BaseJunitTest {
    @Autowired
    private IssueInfoMapper issueInfoMapper;

    @Autowired
    private LabelService labelService;

    @Test
    public void pageQuery(){
        List<Map<String,Object>> list=issueInfoMapper.pageQuery(3,1);
        HashMap<Integer,String> labelMap = labelService.getLabelMap();

        HashMap<String, Object> data = new HashMap<>();
        List<String> labelList = Arrays.asList("Consequence","Code","Quality","Component","Significance");
        for(Map<String,Object> map :list) {
            Set<String> set = map.keySet();
            for (Object key : set) {
                Object value = map.get(key);
                if(labelList.contains(key)){
                    map.put((String) key,labelMap.get(value));
                    System.out.println(map.get(key));
                }
            }
        }

    }
}
