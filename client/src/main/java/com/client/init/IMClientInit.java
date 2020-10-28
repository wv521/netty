package com.client.init;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.client.config.InitConfiguration;
import com.client.handle.IMClientHandle;
import com.common.bean.ChatInfo;
import com.common.bean.ServerInfo;
import com.common.constant.BasicConstant;
import com.common.constant.MessageConstant;
import com.common.protocol.MessageProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.concurrent.DefaultThreadFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class IMClientInit {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMClientInit.class);

    private Channel channel;

    @Autowired
    private InitConfiguration conf;
    @Autowired
    private OkHttpClient okHttpClient;
    private ServerInfo server;

    // @PostConstruct该注解被用来修饰一个非静态的void（）方法。
    //被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，
    // 并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。

    // Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
    @PostConstruct
    public void start() throws Exception{  // 服务器启动时会被加载
        if(server != null){
            LOGGER.warn("---已经连接了服务");
            return;
        }
        //1.获取服务端ip+port
        server = getServerInfo();
        System.out.println("client get server :"+server);
        //2.启动客户端
        startClient(server);
        //3.登录到服务端
        registerToServer();
    }

    /**
     * 与服务端通信
     */
    private void registerToServer() {
        MessageProto.MessageProtocol login = MessageProto.MessageProtocol.newBuilder()
                .setUserId(conf.getUserId())
                .setContent(conf.getUserName())
                .setCommand(MessageConstant.LOGIN)
                .setTime(System.currentTimeMillis())
                .build();
        channel.writeAndFlush(login);
    }

    /**
     * 启动客户端，建立连接
     */
    private void startClient(ServerInfo server) {
        EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("im-client-work"));
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // google Protobuf 编解码
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(MessageProto.MessageProtocol.getDefaultInstance()));
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new IMClientHandle());
                        }
                    });

            ChannelFuture future = bootstrap.connect(server.getIp(), server.getNettyPort()).sync();
            if (future.isSuccess()) {
                LOGGER.info("---客户端启动成功[nettyport:"+server.getNettyPort()+"]");
            }
            channel = future.channel();
        } catch (Exception e) {
            LOGGER.error("---客户端连接异常");
        } finally {
        }
    }

    /**
     * 向路由服务器获取服务端IP与端口
     */
    private ServerInfo getServerInfo() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",conf.getUserId());
            jsonObject.put("userName",conf.getUserName());
            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE,jsonObject.toString());

            Request request = new Request.Builder()
                    .url(conf.getRouteLoginUrl())
                    .post(requestBody) //im.route.login.url=http://localhost:8880/login
                    .build();
            Response response = okHttpClient.newCall(request).execute() ;
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
            ServerInfo service;
            ResponseBody body = response.body();
            try {
                String json = body.string();
                service = JSON.parseObject(json, ServerInfo.class);
            }finally {
                body.close();
            }
            return service;
        } catch (IOException e) {
            LOGGER.error("连接失败！");
        }
        return null;
    }


    public void sendMessage(ChatInfo chat){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command",chat.getCommand());
            jsonObject.put("time",chat.getTime());
            jsonObject.put("userId",chat.getUserId());
            jsonObject.put("content",chat.getContent());
            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE,jsonObject.toString());

            Request request = new Request.Builder()
                    .url(conf.getRouteChatUrl())
                    .post(requestBody) //im.route.chat.url=http://localhost:8880/chat
                    .build();
            Response response = okHttpClient.newCall(request).execute() ;
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Channel getChannel() {
        return this.channel;
    }

    // 处理登出指令
    public void clear() {
        logoutRoute();
        logoutServr();
        server = null;
    }

    /**
     * 调用server处理登出
     */
    private void logoutServr() {
        MessageProto.MessageProtocol message = MessageProto.MessageProtocol.newBuilder()
                .setUserId(conf.getUserId())
                .setContent(conf.getUserName())
                .setCommand(MessageConstant.LOGOUT)
                .setTime(System.currentTimeMillis()).build();
        channel.writeAndFlush(message);
    }

    /**
     * 调用路由端处理redis
     */
    private void logoutRoute() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",conf.getUserId());
            RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE,jsonObject.toString());

            Request request = new Request.Builder()
                    .url(conf.getRouteLogoutUrl())
                    .post(requestBody) //im.route.chat.url=http://localhost:8880/chat
                    .build();
            Response response = okHttpClient.newCall(request).execute() ;
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端重连
     */
    public void reStart() throws Exception {
        logoutRoute();
        server = null;
        start();
    }
}
