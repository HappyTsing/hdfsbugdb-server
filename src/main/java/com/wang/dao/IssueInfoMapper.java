package com.wang.dao;

import com.wang.pojo.IssueInfo;
// 千万不要导错spring的@Param！
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IssueInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IssueInfo row);

    int insertSelective(IssueInfo row);

    IssueInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IssueInfo row);

    int updateByPrimaryKeyWithBLOBs(IssueInfo row);

    int updateByPrimaryKey(IssueInfo row);


    List<Map<String,Object>> pageQuery(@Param("limit")Integer limit, @Param("offset")Integer offset);

    List<Map<String,Object>> queryByValue(@Param("limit")Integer limit, @Param("offset")Integer offset,@Param("searchType")String searchType,@Param("searchValue")String searchValue);



}