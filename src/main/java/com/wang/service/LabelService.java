package com.wang.service;

import java.util.HashMap;

/**
 * @author happytsing
 */
public interface LabelService {


    /**
     * 获取Label中 id 和 name 的对应关系
     *
     * @return key为id，value为name。例：{1=Vital}
     */
    HashMap<Integer, String> getLabelMap();

    /**
     * 获取Label中 id 和 name 的对应关系。但key-value与getLabelMap()相反
     *
     * @return key为name，value为id。例：{Vital=1}
     */
    HashMap<String, Integer> getLabelMapReverse();
}