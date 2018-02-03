package com.wuhulala.websocket.handler;

import com.wuhulala.websocket.util.WebUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
public class ServerInfoHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        InetAddress ipAddress = WebUtils.getCurrentIp();
        InetSocketAddress serverAddress = request.getLocalAddress();
        if(ipAddress != null) {
            System.setProperty("server-host", ipAddress.getHostAddress());
        }
        if(serverAddress != null){
            System.setProperty("server-port", String.valueOf(serverAddress.getPort()));
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
