package com.wuhulala.websocket.config;

import com.wuhulala.cache.CacheClient;
import com.wuhulala.cache.annotation.EnableRedisCache;
import com.wuhulala.websocket.base.WebSocketSessionInfo;
import com.wuhulala.websocket.base.constants.AppConstants;
import com.wuhulala.websocket.handler.ServerInfoHandshakeInterceptor;
import com.wuhulala.websocket.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.List;

/**
 * @author wuhulala
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableRedisCache
@EnableKafka
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Value("${websocket.server.id}")
    private String serverId;

    @Autowired
    CacheClient<String, String> cacheClient;


    public static final String WEBSOCKET_NATIVE_HEADERS = "nativeHeaders";
    public static final String STOMP_USERNAME = "login";
    public static final String DEFAULT_USERNAME = "";
    public static final String STOMP_PASSCODE = "passcode";
    public static final String DEFAULT_PASSCODE = "";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/portfolio").setAllowedOrigins("*").withSockJS().setInterceptors(new ServerInfoHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue", "/user");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                String sessionId = (String) accessor.getHeader(AppConstants.STOMP_SESSION_ID_KEY);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String userName = accessor.getLogin();
                    saveSession(userName, sessionId);
                    logger.info(">>>>>>>>>>>>用户[{}]登录到服务器[{}]<<<<<<<<<<<", userName, serverId);
                }
                if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    String userName = removeSession(sessionId);
                    logger.info(">>>>>>>>>>>>用户[{}]退出到服务器[{}]<<<<<<<<<<<", userName, serverId);
                }
                return message;
            }
        });
    }

    private String removeSession(String sessionId) {
        String userName = cacheClient.getValue(AppConstants.WEBSOCKET_SESSION_RELATION_REDIS_PREFIX + sessionId);
        if (StringUtils.isEmpty(userName)) {
            return "";
        }

        cacheClient.delValue(AppConstants.WEBSOCKET_SESSION_RELATION_REDIS_PREFIX + sessionId);
        cacheClient.delValue(AppConstants.WEBSOCKET_SESSION_ID_REDIS_PREFIX + userName);

        return userName;
    }

    private void saveSession(String userName, String sessionId) {
        WebSocketSessionInfo info = new WebSocketSessionInfo();
        info.setName(userName);
        info.setServerId(serverId);
        cacheClient.setValue(AppConstants.WEBSOCKET_SESSION_ID_REDIS_PREFIX + userName, JsonUtils.toJson(info));
        cacheClient.setValue(AppConstants.WEBSOCKET_SESSION_RELATION_REDIS_PREFIX + sessionId, userName);
    }

    private String getUserName(Message<?> message) {
        LinkedMultiValueMap<String, Object> nativeHeaders = getNativeHeaders(message);
        List<Object> loginNames = nativeHeaders.get(STOMP_USERNAME);
        if (loginNames.size() > 0 && loginNames.get(0) instanceof String) {
            return (String) loginNames.get(0);
        }
        return DEFAULT_USERNAME;
    }


    private LinkedMultiValueMap<String, Object> getNativeHeaders(Message<?> message) {
        MessageHeaders messageHeader = message.getHeaders();
        return (LinkedMultiValueMap<String, Object>) messageHeader.get(WEBSOCKET_NATIVE_HEADERS);
    }
}