package com.wuhulala.cache.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.wuhulala.cache.CacheClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

/**
 * @author wuhulala
 * @date 2017/4/5
 */
@Component
public class CouchBaseCacheClient extends CouchbaseClient implements CacheClient<String, Object> {

    public static final Logger logger = LoggerFactory.getLogger(CouchBaseCacheClient.class);


    @Autowired
    public CouchBaseCacheClient(CouchBaseConnector connector) throws IOException {
        super(connector.connect());
    }


    @Override
    public void init() {

    }

    @Override
    public Object getValue(String key) {
        return this.get(key);
    }

    @Override
    public void setValue(String key, Object value) {
        // logger.debug("存入couchbase----"+key);
        this.set(key, value);
    }

    @Override
    public void setValue(String key, Object value, long expire) {
        this.set(key, (int) (expire / 1000), value);
    }

    @Override
    public void delValue(String key) {
        this.delete(key);
    }

    @Override
    public void flushDB() {
        logger.debug("flushDB....");
    }

    @Override
    public Long dbSize() {
        return 0L;
    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public long getExpireMills(String key) {
        return 0;
    }
}
