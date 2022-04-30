package com.wang.service;

import java.util.List;
import java.util.Map;

/**
 * @author happytsing
 */
public interface IssueInfoService {
    /**
     * 实现分页查询
     *
     * @param pageSize 每页多少数据
     * @param pageNum  查询第几页，从1开始
     * @return [{Cause:"xxx",IssueKey:"HDFS-16300","Component":"LIBS",...},{}...]
     */
    List<Map<String, Object>> pageQueryByNum(Integer pageSize, Integer pageNum);

    /**
     * 通过特定的值的分页查询
     *
     * @param pageSize    每页多少数据
     * @param pageNum     查询第几页，从1开始
     * @param searchType  支持如下几种类型：[ResearchId,Code,Consequence,Component,Quality,IssueKey]
     * @param searchValue 查询的值
     * @return 查询结果 [{Cause:"xxx",IssueKey:"HDFS-16300","Component":"LIBS",...},{}...]
     */
    List<Map<String, Object>> queryByValue(Integer pageSize, Integer pageNum, String searchType, String searchValue);

}
