package com.wuhulala.cache;

import java.util.Set;

/**
 * 各种缓存客户端需要实现的接口
 *
 * @author xueah20964
 * @date 2017/4/5
 */
public interface CacheClient <K,V>{
    /**
     * 初始化连接缓存客户端
     */
    public void init();

    /**
     * 获取缓存值
     * @param key
     * @return
     */
    public V getValue(K key);

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return
     */
    public void setValue(K key, V value);

    /**
     * set
     * @param key
     * @param value
     * @param expire 过期时间 ms
     * @return
     */
    public void setValue(K key, V value, long expire);

    /**
     * del
     * @param key
     */
    public void delValue(K key);

    /**
     * flush 清空缓存
     */
    public void flushDB();

    /**
     * size
     */
    public Long dbSize();

    /**
     * 获取所有key
     * @return
     */
    public Set<K> keys(String pattern);

    /**
     * 获取过期毫秒数
     * @param key
     * @return
     */
    long getExpireMills(K key);
}
