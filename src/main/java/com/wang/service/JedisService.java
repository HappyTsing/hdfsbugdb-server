package com.wang.service;

/**
 * @author happytsing
 */
public interface JedisService {
    /**
     * 写redis
     *
     * @param key   写入redis的key
     * @param value 写入redis的value
     */
    void set(String key, String value);

    /**
     * 读redis
     *
     * @param key 获取的redis数据的key
     * @return String类型的json字符串
     */
    String get(String key);

    /**
     * 检查redis中是否存在数据
     *
     * @param key 检查的key
     * @return 存在true，不存在false
     */
    Boolean isExisted(String key);
}
