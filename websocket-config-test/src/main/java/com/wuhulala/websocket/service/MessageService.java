package com.wuhulala.websocket.service;

import com.wuhulala.cache.CacheClient;
import com.wuhulala.websocket.base.BaseMessage;
import com.wuhulala.websocket.base.WebSocketSessionInfo;
import com.wuhulala.websocket.base.constants.AppConstants;
import com.wuhulala.websocket.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/7
 */
@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Value("${websocket.server.id}")
    private String serverId;

    @Autowired
    CacheClient<String, String> cacheClient;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public MessageService() {
        System.out.println("===================" + messagingTemplate);
    }


    public void handleData(String data) {
        System.out.println("data:[" + data + "]");
        this.messagingTemplate.convertAndSend("/topic/greetings", data);
    }

    @KafkaListener(id="server-${websocket.server.id}",
            topics = AppConstants.WEBSOCKET_TOPIC_PREFIX + "${websocket.server.id}",
            containerFactory = "containerFactory")
    public void handlerKafkaMessage(String data){
        BaseMessage message = JsonUtils.deserialize(data, BaseMessage.class);
        handleDataToUser(message);
    }

    public void handleDataToUser(BaseMessage message) {

        if(StringUtils.isEmpty(message.getName())){
            return;
        }

        String sessionMessage = cacheClient.getValue(AppConstants.WEBSOCKET_SESSION_ID_REDIS_PREFIX + message.getName());
        if(StringUtils.isEmpty(sessionMessage)){
            processAfterUserIsLogout(message);
            return;
        }

        WebSocketSessionInfo sessionInfo = JsonUtils.deserialize(sessionMessage, WebSocketSessionInfo.class);

        // 如果接收方连接在本机，直接发送
        // 否则发送至kafka broker
        if(Objects.equals(sessionInfo.getServerId(), serverId)) {
            messagingTemplate.convertAndSendToUser(message.getName(), message.getDestination(), message.getMessageBody());
        }else {
            kafkaTemplate.send(AppConstants.WEBSOCKET_TOPIC_PREFIX + sessionInfo.getServerId(), JsonUtils.toJson(message));
        }
    }




    private void processAfterUserIsLogout(BaseMessage message) {
        logger.warn("用户[{}]已退出，未接收到消息[{}]", message.getName(), message.getMessageBody());
    }
}
