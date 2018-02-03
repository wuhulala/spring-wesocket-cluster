package com.wuhulala.websocket.base;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
public class BaseMessage implements Message {
    private String name;
    private String destination;
    private String messageBody;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
