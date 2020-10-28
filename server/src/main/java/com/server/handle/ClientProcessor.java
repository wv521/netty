package com.server.handle;
import com.common.constant.BasicConstant;
import com.server.config.InitConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestBody;
import okhttp3.*;
import com.alibaba.fastjson.JSONObject;


import java.io.IOException;

@Component
public class ClientProcessor {

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private InitConfiguration conf;

    public void down(String userId){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",userId);
            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE,jsonObject.toString());

            Request request = new Request.Builder()
                    .url(conf.getRouteLogoutUrl())
                    .post(requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute() ;
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
