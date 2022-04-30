package com.wang.dao;

import com.wang.pojo.Research;

/**
 * @author happytsing
 */
public interface ResearchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Research row);

    int insertSelective(Research row);

    Research selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Research row);

    int updateByPrimaryKey(Research row);


}