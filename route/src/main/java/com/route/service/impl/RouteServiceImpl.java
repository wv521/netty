package com.route.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.common.bean.ChatInfo;
import com.route.service.RouteService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RouteServiceImpl implements RouteService {

    private MediaType mediaType = MediaType.parse("application/json");

    @Autowired
    private OkHttpClient okHttpClient;

    public void sendMessage(String url, ChatInfo chat) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command",chat.getCommand());
        jsonObject.put("time",chat.getTime());
        jsonObject.put("userId",chat.getUserId());
        jsonObject.put("content",chat.getContent());

        RequestBody requestBody = RequestBody.create(mediaType,jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute() ;
        if (!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }
    }

}
