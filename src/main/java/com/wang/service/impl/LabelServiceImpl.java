package com.wang.service.impl;

import com.wang.dao.LabelMapper;
import com.wang.pojo.Label;
import com.wang.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author happytsing
 */
@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelMapper labelMapper;

    @Override
    public HashMap<Integer, String> getLabelMap() {
        List<Label> list = labelMapper.findAll();
        HashMap<Integer, String> labelMap = new HashMap<>();
        for (Label label : list) {
            labelMap.put(label.getId(), label.getName());
        }
        return labelMap;
    }

    @Override
    public HashMap<String, Integer> getLabelMapReverse() {
        List<Label> list = labelMapper.findAll();
        HashMap<String, Integer> labelMap = new HashMap<>();
        for (Label label : list) {
            labelMap.put(label.getName(), label.getId());
        }
        return labelMap;

    }
}
