package com.client.handle;

import com.client.init.IMClientInit;
import com.common.protocol.MessageProto;
import com.common.utils.SpringBeanFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IMClientHandle extends ChannelInboundHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(IMClientHandle.class);

//    private IMClientInit client;
//
//    public IMClientHandle() {
//        client = SpringBeanFactory.getBean(IMClientInit.class);
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProto.MessageProtocol message = (MessageProto.MessageProtocol) msg;
        LOGGER.info("----客户端收到消息： {}",message.getContent());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("-----当前连接的服务断开连接，重新连接其他服务");
        try {
            IMClientInit client = SpringBeanFactory.getBean(IMClientInit.class);
            client.reStart();
            LOGGER.info("--连接成功");
        } catch (Exception e) {
            LOGGER.warn("--连接失败，"+e.getMessage());
        }
    }
}
