package com.wang.service;

import java.util.Map;

/**
 * @author happytsing
 */
public interface ClassifyService {
    /**
     * 获取分类的汇总结果
     *
     * @return 分类的汇总结果
     * @throws Exception
     */
    Map<String, Map<String, Integer>> getClassify() throws Exception;

    /**
     * 返回总共有多少重要的issue
     *
     * @return 重要的issue值
     */
    Integer vitalCount();

}
