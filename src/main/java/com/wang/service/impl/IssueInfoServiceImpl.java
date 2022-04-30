package com.wang.service.impl;

import com.wang.dao.IssueInfoMapper;
import com.wang.service.IssueInfoService;
import com.wang.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author happytsing
 */
@Service
public class IssueInfoServiceImpl implements IssueInfoService {
    @Autowired
    private LabelService labelService;

    @Autowired
    private IssueInfoMapper issueInfoMapper;


    @Override
    public List<Map<String, Object>> pageQueryByNum(Integer pageSize, Integer pageNum) {
        HashMap<Integer, String> labelMap = labelService.getLabelMap();

        // 下述列表中的数据的值是int，需要查询labelMap将其转化为对应的String
        List<String> labelList = Arrays.asList("Consequence", "Code", "Quality", "Component", "Significance");

        Integer limit = pageSize;
        Integer offset = pageSize * (pageNum - 1);
        List<Map<String, Object>> list = issueInfoMapper.pageQuery(limit, offset);
        for (Map<String, Object> map : list) {
            Set<String> set = map.keySet();
            for (Object key : set) {
                Object value = map.get(key);
                if (labelList.contains(key)) {
                    map.put((String) key, labelMap.get(value));
                }
            }
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> queryByValue(Integer pageSize, Integer pageNum, String searchType, String searchValue) {
        HashMap<Integer, String> labelMap = labelService.getLabelMap();
        HashMap<String, Integer> labelMapReverse = labelService.getLabelMapReverse();

        /**
         * 述列表中的数据的值是int，需要查询labelMap将其转化为对应的String。然后才能返回给前端
         */
        List<String> labelList = Arrays.asList("Consequence", "Code", "Quality", "Component", "Significance");

        /**
         * 前端传过来的是注入Datanode等字符串，这些label的关系存在label表中。查询Classify表的时候应该传入数字，故而再此转换以下。
         */
        if (labelList.contains(searchType)) {
            searchValue = String.valueOf(labelMapReverse.get(searchValue));
        }

        Integer limit = pageSize;
        Integer offset = pageSize * (pageNum - 1);


        List<Map<String, Object>> list = issueInfoMapper.queryByValue(limit, offset, searchType, searchValue);
        for (Map<String, Object> map : list) {
            Set<String> set = map.keySet();
            for (Object key : set) {
                Object value = map.get(key);
                if (labelList.contains(key)) {
                    map.put((String) key, labelMap.get(value));
                }
            }
        }

        return list;
    }
}
