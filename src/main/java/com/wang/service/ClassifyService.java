package com.wang.service;

import com.wang.pojo.IssueInfo;

import java.util.HashMap;
import java.util.Map;

public interface ClassifyService {
    Map<String, Map<String, Integer>> getClassify() throws Exception;
    Integer vitalCount();

}
