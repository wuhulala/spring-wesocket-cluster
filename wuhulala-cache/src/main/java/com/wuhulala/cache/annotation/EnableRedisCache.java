package com.wuhulala.cache.annotation;

import com.wuhulala.cache.redis.RedisCacheConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis开关
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisCacheConfig.class)
public @interface EnableRedisCache {
}
