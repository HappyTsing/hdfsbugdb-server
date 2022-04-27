package com.wang.service.impl;

import com.wang.dao.IssueInfoMapper;
import com.wang.service.IssueInfoService;
import com.wang.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IssueInfoServiceImpl  implements IssueInfoService {
    @Autowired
    private LabelService labelService;

    @Autowired
    private IssueInfoMapper issueInfoMapper;


    @Override
    public List<Map<String, Object>> pageQueryByNum(Integer pageSize, Integer pageNum) {
        HashMap<Integer,String> labelMap = labelService.getLabelMap();

        Integer limit = pageSize;
        Integer offset = pageSize*(pageNum-1);
        List<Map<String,Object>> list=issueInfoMapper.pageQuery(limit,offset);

        // 下述列表中的数据的值是int，需要查询labelMap将其转化为对应的String
        List<String> labelList = Arrays.asList("Consequence","Code","Quality","Component","Significance");
        for(Map<String,Object> map :list) {
            Set<String> set = map.keySet();
            for (Object key : set) {
                Object value = map.get(key);
                if(labelList.contains(key)){
                    map.put((String) key,labelMap.get(value));
                }
            }
        }

        return list;
    }
}
