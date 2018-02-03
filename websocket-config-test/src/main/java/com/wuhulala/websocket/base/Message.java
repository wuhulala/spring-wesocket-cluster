package com.wuhulala.websocket.base;

/**
 * 作甚的
 *
 * @author wuhulala
 * @version 1.0
 * @since 2018/1/21
 */
public interface Message {

    String getName();

    String getDestination();

    String getMessageBody();

}
