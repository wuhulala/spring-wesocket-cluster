package com.wuhulala.websocket.controller;

import com.wuhulala.websocket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class GreetingController {


    @Autowired
    private MessageService messageService;
//    public GreetingController(SimpMessagingTemplate template) {
//        this.template = template;
//    }


    public GreetingController() {
        System.out.println("GreetingController create it" + messageService);
    }

    @RequestMapping(path = "/greetings", method = RequestMethod.POST)
    @ResponseBody
    public String greet(String greeting) {
        String text = "[" + LocalDateTime.now() + "]:" + greeting;
        messageService.handleData(text);
        return "success";
    }

    /**
     * 客户端向（/app/greeting）发送消息，服务器广播到（topic/greetings）
     *
     *   <p>因为MessageMapping的处理程序是SimpAnnotationMethodMessageHandler，
     *   所以需要添加app前缀，当然也是在setApplicationDestinationPrefixes配置得我</p>
     * @param greeting
     * @return
     */
    @MessageMapping("/greeting")
    @SendTo("/topic/greetings")
    public String handle(String greeting) {
        return "[" + LocalDateTime.now() + ": " + greeting;
    }


    @MessageMapping("/time")
    @SendToUser("/time")
    public String handleToUser(String greeting) {
        return LocalDateTime.now() + "";
    }


}