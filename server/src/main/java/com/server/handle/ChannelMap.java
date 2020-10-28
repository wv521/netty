package com.server.handle;


import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelMap {

    private static ChannelMap instance;
    // 存储了UserId—>Channel
    private final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<String, Channel>();

    private ChannelMap() {
    }
    public static ChannelMap newInstance(){
        if(instance == null){
            instance = new ChannelMap();
        }
        return instance;
    }

    public Map<String, Channel> getCHANNEL_MAP() {
        return CHANNEL_MAP;
    }

    public void putClient(String userId, Channel channel){
        CHANNEL_MAP.put(userId, channel);
    }

    public Channel getClient(String userId){
        return CHANNEL_MAP.get(userId);
    }
}
