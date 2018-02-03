package com.wuhulala.cache.redis;

import com.wuhulala.cache.CacheClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisCacheClient<K, V> implements CacheClient<K, V> {

    private RedisTemplate<K, V> redisTemplate;

    private ValueOperations<K, V> valueOperations;

    @Override
    public void init() {
        System.out.println("i do not need to init it...");
    }

    @Override
    public V getValue(K key) {
        return valueOperations.get(key);
    }

    @Override
    public void setValue(K key, V value) {
        valueOperations.set(key, value);
    }

    @Override
    public void setValue(K key, V value, long expire) {
        valueOperations.set(key, value, expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public void delValue(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void flushDB() {
        System.out.println("清空缓存数据库？ 臣妾做不到....");
    }

    @Override
    public Long dbSize() {
        return null;
    }

    @Override
    public Set<K> keys(String pattern) {
        return null;
    }

    @Override
    public long getExpireMills(K key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }


    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValueOperations(ValueOperations<K, V> valueOperations) {
        this.valueOperations = valueOperations;
    }
}