package com.wang.dao;

import com.wang.pojo.Label;

public interface LabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Label row);

    int insertSelective(Label row);

    Label selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Label row);

    int updateByPrimaryKey(Label row);
}