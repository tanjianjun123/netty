package com.hyyn.netty.http.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/6 15:34
 * @Version 1.0
 */
@Component
public class HttpChildChannelHandler extends ChannelInitializer<SocketChannel> {

    private static final int ALL_IDLE_TIME_SECONDS = 5;
    private static final int MAX_CONTENT_LENGTH = 512 * 1024;
    @Resource
    private HttpServerHandler httpServerHandler;

    public void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new HttpRequestDecoder(),
                //聚合http请求
                new HttpObjectAggregator(MAX_CONTENT_LENGTH),
                new HttpResponseEncoder(),
                //解决大码流的问题
                new ChunkedWriteHandler(),
                httpServerHandler
        );
    }
}
