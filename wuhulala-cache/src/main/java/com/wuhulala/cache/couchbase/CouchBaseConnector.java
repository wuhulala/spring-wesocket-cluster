package com.wuhulala.cache.couchbase;

import com.couchbase.client.CouchbaseConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;

/**
 * @author xueah20964
 * @date 2017/4/5
 */
@Component
public class CouchBaseConnector {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${couchBase.server}")
    private String serverAddress;
    @Value("${couchBase.name}")
    private String name;
    @Value("${couchBase.pwd}")
    private String pwd;

    public CouchbaseConnectionFactory connect() {
        CouchbaseConnectionFactory cf = null;
        try {
            String[] serverNames = serverAddress.split(",");
            ArrayList<URI> serverList = new ArrayList<URI>();
            for (String serverName : serverNames) {
                URI base = null;
                base = URI.create(String.format("http://%s/pools",serverName));
                serverList.add(base);
            }
            cf = new CouchbaseConnectionFactory(serverList, name, pwd);
            logger.info(cf.toString());
            return cf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close(){

    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
