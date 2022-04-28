package com.wang.service;

import java.util.List;
import java.util.Map;

public interface IssueInfoService {
    List<Map<String, Object>> pageQueryByNum(Integer pageSize, Integer pageNum);

    List<Map<String, Object>> queryByValue(Integer pageSize, Integer pageNum,String searchType,String searchValue);

}
