package com.hyyn.netty.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/6 15:33
 * @Version 1.0
 */
@Component
public class HttpServer {
    private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);
    @Resource
    private HttpChildChannelHandler httpChildChannelHandler;

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        logger.info("准备运行端口：" + port);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(httpChildChannelHandler);
            //绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                logger.info("http server启动成功");
            } else {
                logger.info("http server启动失败");
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
        }
    }
}
