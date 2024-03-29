package com.wang.service.impl;

import com.wang.dao.ClassifyMapper;
import com.wang.service.ClassifyService;
import com.wang.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author happytsing
 */
@Service
public class ClassifyServiceImpl implements ClassifyService {
    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private LabelService labelService;

    private Map<String, Integer> getClassifyType(String type) throws Exception {

        HashMap<Integer, String> labelMap = labelService.getLabelMap();
        List<Map<String, Object>> list;
        switch (type) {
            case "Quality":
                list = classifyMapper.getQuality();
                break;
            case "Component":
                list = classifyMapper.getComponent();
                break;

            case "Consequence":
                list = classifyMapper.getConsequence();
                break;
            case "Code":
                list = classifyMapper.getCode();
                break;

            case "Significance":
                list = classifyMapper.getSignificance();
                break;
            default:
                throw new Exception("type = " + type + ",which is not in [Quality,Component,Consequence,Code,Significance]");
        }
        Map<String, Integer> map_new = new HashMap<>();
        for (Map<String, Object> map : list) {
            String key = null;
            Integer value = null;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if ("type".equals(entry.getKey())) {
                    key = labelMap.get((Integer) entry.getValue());
                } else if ("count".equals(entry.getKey())) {
                    value = ((Long) entry.getValue()).intValue();
                }
            }
            map_new.put(key, value);
        }
        return map_new;
    }

    @Override
    public Map<String, Map<String, Integer>> getClassify() throws Exception {
        Map<String, Integer> mapQuality = getClassifyType("Quality");
        Map<String, Integer> mapComponent = getClassifyType("Component");
        Map<String, Integer> mapConsequence = getClassifyType("Consequence");
        Map<String, Integer> mapCode = getClassifyType("Code");
        Map<String, Integer> mapSignificance = getClassifyType("Significance");

        Map<String, Map<String, Integer>> map = new HashMap<>();
        map.put("Quality", mapQuality);
        map.put("Component", mapComponent);
        map.put("Consequence", mapConsequence);
        map.put("Code", mapCode);
        map.put("Significance", mapSignificance);

        return map;

    }

    @Override
    public Integer vitalCount() {
        return classifyMapper.vitalCount();
    }

}
