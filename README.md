

![这里写图片描述](http://img.blog.csdn.net/20180122232447461?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzA3NjA0NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

redis 用来存用户与服务器的关系（redis可以任意替换为一种存储形式）
服务器 A 会订阅topic 为 WebSocket-A 的消息
服务器 B 会订阅topic 为 WebSocket-B 的消息
kafka 用来接收推送消息（替换为任意一种mq）

如果用户1需要给用户3发送信息，如果在单机情况下，由于用户3没有与server-A建立链接，所以推送不到。但是现在因为在redis存储了用户3和server-B的关联关系，所以直接推送到WebSocket-B主题之中即可。由server-B接受消息并推送给用户3。

优点：
1. 解决了集群情况下跨机器通讯问题。

缺点：
1. 没有达到随意增减节点的需求，新加机器需要配置server-id。
2. 编程复杂，需要手动注册kafka-listener消息节点。

再宕机的情况下，假如A服务器宕机了，WEBSOCKET-A的消息就没有消费者了，所以只要在能重启一台服务器名继续叫做A的机器即可，因为在代码中多了一次判断，调用逻辑如下
```
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
```

即即便是A服务器接收到了这条消息，我们也不确定客户是否在此中间重连了，所以需要去redis拿到当前客户真实的服务器，但是如果客户不断的重练，可能会导致消息一直发送不出去。但是这种也是能被接收的。

实现：https://github.com/wuhulala/spring-wesocket-cluster.git
