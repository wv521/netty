package com.server.handle;

import com.common.constant.MessageConstant;
import com.common.protocol.MessageProto;
import com.common.utils.SpringBeanFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IMServerHandle extends ChannelInboundHandlerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(IMServerHandle.class);

    private ChannelMap CHANNELMAP = ChannelMap.newInstance();

    private AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");

    private ClientProcessor clientProcessor;

    public IMServerHandle() {
        clientProcessor = SpringBeanFactory.getBean(ClientProcessor.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String content = new String(req, "UTF-8");
        MessageProto.MessageProtocol message = (MessageProto.MessageProtocol) msg;

        if(MessageConstant.LOGIN.equals(message.getCommand())){
            // 登录， 保存channel
            // 直接在channel设置了userId属性值
            ctx.channel().attr(userIdKey).set(message.getUserId());
            CHANNELMAP.putClient(message.getUserId(), ctx.channel());
            LOGGER.info("----客户端登录成功， userId:{}",message.getUserId());
        } else if(MessageConstant.LOGOUT.equals(message.getCommand())){
            // 登出
            CHANNELMAP.getCHANNEL_MAP().remove(message.getUserId());
            LOGGER.info("----客户端下线了， userId:{}",message.getUserId());
        }

        // 如果是CHAT类型的消息， 轮训客户端  channel  发送消息
        // 并不能够解决问题

        LOGGER.info("---服务端收到消息[{}]", message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String userId = ctx.channel().attr(userIdKey).get();
        //服务端ChannelMap删掉
        CHANNELMAP.getCHANNEL_MAP().remove(userId);
        // 调用route 删除redis数据
        clientProcessor.down(userId);
        LOGGER.info("---客户端强制下线了,userId:{}", userId);
    }
}
