package com.wang.service;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface JedisService {
    void set(String key,String value);
    String get(String key);
    Boolean isExisted(String key);
}
