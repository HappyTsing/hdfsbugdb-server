package com.wang.dao;

import com.wang.pojo.Label;

import java.util.List;

public interface LabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Label row);

    int insertSelective(Label row);

    Label selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Label row);

    int updateByPrimaryKey(Label row);

    List<Label> findAll();
}