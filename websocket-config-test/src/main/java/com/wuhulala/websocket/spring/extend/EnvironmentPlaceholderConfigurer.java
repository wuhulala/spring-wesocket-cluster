package com.wuhulala.websocket.spring.extend;

import net.spy.memcached.compat.log.Logger;
import net.spy.memcached.compat.log.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.io.IOException;

/**
 * 将应用的properties 添加到应用环境之中
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
public class EnvironmentPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentPlaceholderConfigurer.class);

    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        super.postProcessBeanFactory(beanFactory);

        try {

            PropertySource<?> localPropertySource = new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, mergeProperties());

            ((StandardEnvironment) environment).getPropertySources().addLast(localPropertySource);

        } catch (IOException e) {
            logger.error("add properties to environment error, please check your config file !!!", e);
        }

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
