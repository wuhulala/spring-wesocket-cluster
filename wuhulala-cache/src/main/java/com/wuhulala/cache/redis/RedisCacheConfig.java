package com.wuhulala.cache.redis;

import com.wuhulala.cache.CacheClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import static com.wuhulala.cache.redis.RedisConfigConstans.CONFIG_SEPARATOR;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
@Configuration
public class RedisCacheConfig implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        //
        redisConnectionFactory.setHostName(getProp(RedisConfigConstans.CONFIG_HOSTNAME, "localhost"));

        redisConnectionFactory.setDatabase(Integer.valueOf(getProp(RedisConfigConstans.CONFIG_DATABASE, 0)));

        redisConnectionFactory.setPort(Integer.valueOf(getProp(RedisConfigConstans.CONFIG_PORT, Protocol.DEFAULT_PORT)));

        String password = getProp(RedisConfigConstans.CONFIG_PASSWORD, "");
        if (!valueIsEmpty(password)) {
            redisConnectionFactory.setPassword(password);
        }

        redisConnectionFactory.setTimeout(Integer.valueOf(getProp(RedisConfigConstans.CONFIG_TIMEOUT, Protocol.DEFAULT_TIMEOUT)));

        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(Integer.valueOf(getProp(RedisConfigConstans.CONFIG_MAX_TOTAL, 10)));
        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(Long.valueOf(getProp(RedisConfigConstans.CONFIG_MAX_WAIT_MILLIS, 1000)));
        // 设置空间连接
        config.setMaxIdle(Integer.valueOf(getProp(RedisConfigConstans.CONFIG_MAX_IDLE, 10)));

        redisConnectionFactory.setPoolConfig(config);

        return redisConnectionFactory;
    }

    private String getProp(String configKey, Object defaultValue) {

        String value = getProp(configKey);

        return valueIsEmpty(value) ? String.valueOf(defaultValue) : value;
    }

    private boolean valueIsEmpty(String value) {
        return value == null || "".equals(value);
    }

    private String getProp(String configKey) {

        if (environment.getProperty(RedisConfigConstans.CONFIG_PREFIX) != null) {
            configKey = environment.getProperty(RedisConfigConstans.CONFIG_PREFIX)
                    + CONFIG_SEPARATOR
                    + configKey;
        }

        return environment.getProperty(configKey);
    }


    /**
     * Redis Json-Serializer
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setKeySerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis OpsValues
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public CacheClient redisCacheClient(RedisTemplate<String, Object> redisTemplate, ValueOperations<String, Object> valueOperations) {
        RedisCacheClient cacheClient = new RedisCacheClient();
        cacheClient.setRedisTemplate(redisTemplate);
        cacheClient.setValueOperations(valueOperations);
        return cacheClient;
    }
}
