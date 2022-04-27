package com.wang.dao;

import com.wang.BaseJunitTest;
import com.wang.pojo.Label;
import com.wang.service.Impl.IssueInfoServiceImplTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifyMapperTest extends BaseJunitTest {
    private static  final Logger logger = LoggerFactory.getLogger(ClassifyMapperTest.class);

    @Autowired
    private ClassifyMapper classifyMapper;
    @Autowired
    private LabelMapper labelMapper;

    public HashMap<Integer,String> getLabelMap(){
        List<Label> list = labelMapper.findAll();
        HashMap<Integer,String> labelMap = new HashMap<>();
        for(Label label : list){
            labelMap.put(label.getId(),label.getName());
        }
        logger.info("labelMap："+ labelMap);
        return labelMap;
    }

    public Map<String, Integer> getClassifyType(String type) throws Exception {

        HashMap<Integer,String> labelMap = getLabelMap();
        List<Map<String,Object>> list;
        switch(type){
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
                throw new Exception("type 错误");
        }
        Map<String, Integer> map_new = new HashMap<>();
        for(Map<String,Object> map :list) {
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

    @Test
    public void getClassify() throws Exception {
        Map<String, Integer> map = getClassifyType("Quality");
        logger.info(map.toString());
    }
}
