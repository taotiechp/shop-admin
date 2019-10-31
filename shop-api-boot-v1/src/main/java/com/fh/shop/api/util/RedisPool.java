package com.fh.shop.api.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private RedisPool(){}

    private static JedisPool jedisPool;

    private static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(100);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestOnBorrow(true);
        //jedisPoolConfig.set
        jedisPool = new JedisPool(jedisPoolConfig,"192.168.37.129",7020);
    }

    //静态代码块，只执行一次。加载类的时候执行
    static {
        initPool();
    }

    public static Jedis getResource(){
        return jedisPool.getResource();
    }


}
