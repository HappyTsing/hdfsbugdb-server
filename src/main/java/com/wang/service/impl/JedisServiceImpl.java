package com.wang.service.impl;

import com.wang.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author happytsing
 */
@Service
public class JedisServiceImpl implements JedisService {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void set(String key, String value) {
        // try-with-resources 语句，使用完毕后会自动关闭资源（即jedis）
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
            // 设置过期时间 1 个月
            jedis.expire(key, 60 * 60 * 24 * 30);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String get(String key) {
        String jsonStr = null;
        try (Jedis jedis = jedisPool.getResource()) {
            jsonStr = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;

    }

    @Override
    public Boolean isExisted(String key) {
        boolean isExiste = false;
        try (Jedis jedis = jedisPool.getResource()) {
            isExiste = jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExiste;
    }
}
