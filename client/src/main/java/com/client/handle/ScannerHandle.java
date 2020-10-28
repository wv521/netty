package com.client.handle;

import com.alibaba.fastjson.JSONObject;
import com.client.config.InitConfiguration;
import com.client.init.IMClientInit;
import com.common.bean.ChatInfo;
import com.common.constant.MessageConstant;
import com.common.utils.SpringBeanFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Scanner;

public class ScannerHandle implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(ScannerHandle.class);
    private IMClientInit client;
    private InitConfiguration conf;

    public ScannerHandle () {
        client = SpringBeanFactory.getBean(IMClientInit.class);
        conf = SpringBeanFactory.getBean(InitConfiguration.class);
    }

    // 项目启动后，在控制台上输入发送内容（在启动类中触发）
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                if (StringUtils.isEmpty(msg)) {
                    LOGGER.info("---不允许发送空消息");
                    continue;
                }

                if(MessageConstant.LOGOUT.equals(msg)){
                    // 登出指令
                    client.clear();
                    LOGGER.info("---客户端主动下线，如需重新进入，请输入LOGIN");
                    continue;
                } else if(MessageConstant.LOGIN.equals(msg)){
                    client.start();
                    LOGGER.info("--重新登录");
                    continue;
                }

                ChatInfo chat = new ChatInfo(MessageConstant.CHAT, System.currentTimeMillis(), conf.getUserId(), msg);
                client.sendMessage(chat);

//                byte[] content = msg.getBytes();
//                ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(content.length);
//                buf.writeBytes(content);

//                MessageProto.MessageProtocol message = MessageProto.MessageProtocol.newBuilder()
//                        .setCommand(MessageConstant.CHAT)
//                        .setTime(System.currentTimeMillis())
//                        .setUserId(conf.getUserId())
//                        .setContent(msg)
//                        .build();
//                client.getChannel().writeAndFlush(message);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
