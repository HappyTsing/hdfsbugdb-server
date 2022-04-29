package com.wang.service.Impl;

import com.wang.BaseJunitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisServiceTest extends BaseJunitTest {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void testRedis(){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.set("a","atest");
            System.out.println(jedis.get("a"));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }
}
