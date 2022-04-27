package com.wang.dao;

import com.wang.pojo.Classify;

import java.util.List;
import java.util.Map;

public interface ClassifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Classify row);

    int insertSelective(Classify row);

    Classify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Classify row);

    int updateByPrimaryKey(Classify row);

    List<Map<String,Object>> getQuality();
    List<Map<String,Object>> getComponent();
    List<Map<String,Object>> getConsequence();
    List<Map<String,Object>> getCode();
    List<Map<String,Object>> getSignificance();

    int vitalCount();

}