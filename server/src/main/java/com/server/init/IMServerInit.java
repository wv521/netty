package com.server.init;

import com.common.protocol.MessageProto;
import com.server.config.InitConfiguration;
import com.server.handle.IMServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IMServerInit {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMServerInit.class);

    private EventLoopGroup acceptorGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Autowired
    private InitConfiguration conf;

    @PostConstruct
    public void start() {
        try {
            //Netty用于启动NIO服务器的辅助启动类
            ServerBootstrap sb = new ServerBootstrap();
            //将两个NIO线程组传入辅助启动类中
            sb.group(acceptorGroup, workerGroup)
                    //设置创建的Channel为NioServerSocketChannel类型
                    .channel(NioServerSocketChannel.class)
                    //保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //设置绑定IO事件的处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // google Protobuf 编解码
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(MessageProto.MessageProtocol.getDefaultInstance()));
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new IMServerHandle());
                        }
                    });
            ChannelFuture conn = sb.bind(conf.getNettyPort()).sync();
            if(conn.isSuccess()){
                LOGGER.info("---服务端启动成功，端口["+conf.getNettyPort()+"]");
            }
        } catch (Exception e) {
            LOGGER.error("---服务启动异常");
        } finally {
        }
    }
}
