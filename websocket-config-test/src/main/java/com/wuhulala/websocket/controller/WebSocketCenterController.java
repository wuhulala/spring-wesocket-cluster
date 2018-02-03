package com.wuhulala.websocket.controller;

import com.wuhulala.websocket.base.BaseMessage;
import com.wuhulala.websocket.service.MessageService;
import com.wuhulala.websocket.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
@Controller
public class WebSocketCenterController {
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat")
    private void centerHandle(String messageContent){
        BaseMessage message = (BaseMessage) JsonUtils.json2Object(messageContent, BaseMessage.class);
        messageService.handleDataToUser(message);
    }

    public static void main(String[] args) {
        BaseMessage message = new BaseMessage();
        message.setName("wuhulala");
        message.setDestination("/chat");
        message.setMessageBody("hello, wuhulala !!!");

        JsonUtils.printJson(message, true);
    }
}
