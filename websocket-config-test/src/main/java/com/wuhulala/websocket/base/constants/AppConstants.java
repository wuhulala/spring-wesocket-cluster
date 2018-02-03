package com.wuhulala.websocket.base.constants;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/2/3
 */
public interface AppConstants {
    String WEBSOCKET_SESSION_ID_REDIS_PREFIX = "ws_s:";
    /**
     * sessionId与用户名的联系
     */
    String WEBSOCKET_SESSION_RELATION_REDIS_PREFIX = "ws_r:";
    String WEBSOCKET_TOPIC_PREFIX = "websocket-server-";
    String STOMP_SESSION_ID_KEY = "simpSessionId";
}
