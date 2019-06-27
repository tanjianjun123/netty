package com.hyyn.netty.tcp.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/6 15:34
 * @Version 1.0
 */
@Component
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    private static final int ALL_IDLE_TIME_SECONDS = 30;
    private static final int MAX_LENGTH = 1024;
    @Resource
    private TcpServerHandler tcpServerHandler;

    public void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                //tcp拆包粘包
                new DelimiterBasedFrameDecoder(MAX_LENGTH, Unpooled.wrappedBuffer(
                        ";".getBytes())),
                new StringDecoder(),
                new StringEncoder(),
                //多长时间内未接收心跳包，断开连接或者采取相应措施
                new IdleStateHandler(30, 30, ALL_IDLE_TIME_SECONDS),
                tcpServerHandler
        );
    }
}
