package com.wang.service.impl;

import com.wang.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisServiceImpl implements JedisService {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void set(String key, String value) {
        // try-with-resources 语句，使用完毕后会自动关闭资源（此处是jedis）
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
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
