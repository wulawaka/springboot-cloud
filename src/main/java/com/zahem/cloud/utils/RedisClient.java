package com.zahem.cloud.utils;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class RedisClient {
    @Resource
    private RedisTemplate redisTemplate;

    public boolean set(Object k,Object v){
       try {
           redisTemplate.opsForValue().set(k, v);
            return true;
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
    }

    /**
     * 设置Hash
     * @param h
     * @param hk
     * @param hv
     * @return
     */
    public boolean set(Object h,Object hk,Object hv){
        try{
            redisTemplate.opsForHash().put(h,hk,hv);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //获取value值
    public Object get(Object key){
        return redisTemplate.opsForValue().get(key);
    }
    //获取Hash值
    public Object get(Object h,Object o){
        return redisTemplate.opsForHash().get(h,o);
    }
    //是否存在key
    public Boolean hasKey(Object key) {
        return redisTemplate.hasKey(key);
    }
    //自增1
    public Long incr(Object k){
        return redisTemplate.opsForValue().increment(k);
    }
}
