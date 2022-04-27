package com.wang.service;

import java.util.List;
import java.util.Map;

public interface IssueInfoService {
    List<Map<String, Object>> pageQueryByNum(Integer pageSize, Integer pageNum);

}
