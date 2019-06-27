package com.hyyn.netty.tcp.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取yml配置文件中的信息
 * @Author: tanjianjun
 * @Date: 2019/5/6 16:01
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {
    private int tcp_port;
    private int http_port;

    public NettyConfig() {
    }

    public int getTcp_port() {
        return tcp_port;
    }

    public void setTcp_port(int tcp_port) {
        this.tcp_port = tcp_port;
    }

    public int getHttp_port() {
        return http_port;
    }

    public void setHttp_port(int http_port) {
        this.http_port = http_port;
    }
}