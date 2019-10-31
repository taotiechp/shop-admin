package com.fh.shop.api.util;

import redis.clients.jedis.Jedis;

public class RedisUtil {

    /**
     * 赋值
     * @param key
     * @param value
     */
    public static void set(String key,String value){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
    }

    /**
     *查询
     */
    public static String get(String key){
        Jedis resource = null;
        String value = null;
        try {
            resource = RedisPool.getResource();
            value = resource.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
        return value;
    }

    /**
     * 删除
     */
    public static long del(String key){
        Jedis resource = null;
        Long dels = null;
        try {
            resource = RedisPool.getResource();
            dels = resource.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
        return dels;
    }

    /**
     * 记时刷新
     */
    public static void setEX(String key,String value,Integer time){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.setex(key,time,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
    }

    /**
     * 判断是否过期
     */
    public static boolean exists(String key){
        Jedis resource = null;
        Boolean exists = false;
        try {
            resource = RedisPool.getResource();
            exists = resource.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
        return exists;
    }

    /**
     * 设置过期时间
     */
    public static void setExpire(String key,Integer time){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.expire(key,time);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
    }

    /**
     * 赋值hset
     * @param key
     * @param value
     */
    public static void hset(String key,String fieid,String value){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.hset(key,fieid,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
    }

    /**
     *查询hmap
     */
    public static String hget(String key,String fieid){
        Jedis resource = null;
        String value = null;
        try {
            resource = RedisPool.getResource();
            value = resource.hget(key,fieid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
        return value;
    }

    /**
     *删除hmap
     */
    public static void hdel(String key,String fieid){
        Jedis resource = null;
        try {
            resource = RedisPool.getResource();
            resource.hdel(key,fieid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (resource != null){
                resource.close();
            }
        }
    }

}
