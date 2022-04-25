package com.wang.dao;

import com.wang.pojo.Classify;

public interface ClassifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Classify row);

    int insertSelective(Classify row);

    Classify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Classify row);

    int updateByPrimaryKey(Classify row);
}