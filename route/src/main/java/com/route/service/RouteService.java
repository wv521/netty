package com.route.service;


import com.common.bean.ChatInfo;

import java.io.IOException;

public interface RouteService {

    public void sendMessage(String url, ChatInfo chat) throws IOException;

}
