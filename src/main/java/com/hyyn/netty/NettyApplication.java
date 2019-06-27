package com.hyyn.netty;

import com.hyyn.netty.crossfire.domain.GameRecord;
import com.hyyn.netty.http.server.HttpMessagelHolder;
import com.hyyn.netty.http.server.HttpServer;
import com.hyyn.netty.tcp.server.NettyConfig;
import com.hyyn.netty.tcp.server.TcpServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class NettyApplication implements CommandLineRunner {

    @Resource
    private TcpServer tcpServer;
    @Resource
    private HttpServer httpServer;
    @Resource
    private NettyConfig nettyConfig;

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        tcpServer.run(nettyConfig.getTcp_port());
        httpServer.run(nettyConfig.getHttp_port());
        Integer timeout =9;
        Integer merchantId = 1;
        Integer userId =1;
        Integer machineId = 1;
        //保存http信息
        GameRecord gameRecord = new GameRecord();
        gameRecord.setUser_id(userId);
        gameRecord.setMachine_id(machineId);
        gameRecord.setMerchant_id(merchantId);
        HttpMessagelHolder.addChannel("C0BBF729",gameRecord);
    }
}
