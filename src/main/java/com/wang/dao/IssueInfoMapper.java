package com.wang.dao;

import com.wang.pojo.IssueInfo;

public interface IssueInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IssueInfo row);

    int insertSelective(IssueInfo row);

    IssueInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IssueInfo row);

    int updateByPrimaryKeyWithBLOBs(IssueInfo row);

    int updateByPrimaryKey(IssueInfo row);
}