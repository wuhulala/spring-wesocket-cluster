package com.wuhulala.cache.redis;

/**
 * redis 配置项 固定前缀
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
public interface RedisConfigConstans {
    String CONFIG_PREFIX = "redis.config.prefix";
    String CONFIG_SEPARATOR = ".";
    String CONFIG_HOSTNAME = "hostname";
    String CONFIG_DATABASE = "database";
    String CONFIG_PORT = "port";
    String CONFIG_PASSWORD = "password";
    String CONFIG_TIMEOUT = "timeout";
    String CONFIG_MAX_TOTAL = "max_total";
    String CONFIG_MAX_WAIT_MILLIS = "max_wait_mills";
    String CONFIG_MAX_IDLE = "max_idle";
}
