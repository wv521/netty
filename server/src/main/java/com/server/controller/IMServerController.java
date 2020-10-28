package com.server.controller;

import com.common.bean.ChatInfo;
import com.common.constant.MessageConstant;
import com.common.protocol.MessageProto;
import com.server.handle.ChannelMap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class IMServerController {
    private final static Logger LOGGER = LoggerFactory.getLogger(IMServerController.class);

    private ChannelMap CHANNELMAP = ChannelMap.newInstance();


    @PostMapping("/pushMessage")
    public void pushMessage(@RequestBody ChatInfo chat){
        // 1.获取消息对象
        MessageProto.MessageProtocol message = MessageProto.MessageProtocol.newBuilder()
                .setCommand(chat.getCommand())
                .setTime(chat.getTime())
                .setUserId(chat.getUserId())
                .setContent(chat.getContent()).build();

        // 2.发给指定的client（私聊、群聊）
        if (MessageConstant.CHAT.equals(message.getCommand())){
            for (Map.Entry<String, Channel> entry : CHANNELMAP.getCHANNEL_MAP().entrySet()) {
                // 过滤掉当前发送消息的用户本身
                if(!entry.getKey().equals(message.getUserId())){
                    LOGGER.info("---服务端向{}用户发送了消息，来自于userId:{}",entry.getKey(), chat.getUserId());
                    entry.getValue().writeAndFlush(message);
                }
            }
        }
    }
}
