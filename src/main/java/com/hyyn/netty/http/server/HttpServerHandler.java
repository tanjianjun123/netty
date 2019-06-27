package com.hyyn.netty.http.server;

import com.alibaba.fastjson.JSONObject;
import com.hyyn.netty.crossfire.domain.GameRecord;
import com.hyyn.netty.tcp.server.TcpChannelHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/6 15:35
 * @Version 1.0
 */
@Component
@ChannelHandler.Sharable
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final static Logger logger = LoggerFactory.getLogger("http-info");
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        logger.info("http:客户端连接上了:"+ctx.channel().remoteAddress());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("http:客户端断开连接了");
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,  FullHttpRequest request) throws Exception {
        //获取http信息
        HttpMethod httpMethod = request.method();
        logger.info("httpMethod:"+httpMethod);
        String method = new URI(request.uri()).getPath().replaceAll("^/", "");
        logger.info("method:"+method);
        if(method==null){
            logger.error("http/method参数错误");
            send(ctx,"fail",HttpResponseStatus.OK);
            return;
        }
        JSONObject json = JSONObject.parseObject(request.content().toString(CharsetUtil.UTF_8));
        if(json==null){
            logger.error("http/json参数错误");
            send(ctx,"fail",HttpResponseStatus.OK);
            return;
        }
        String identity = json.getString("identity");
        String count = json.getString("count");
        if(method.equals("status")){
            logger.info("查询机器状态");
        }else if(method.equals("count")){
            logger.info("发送游戏次数");
            Integer timeout = json.getInteger("timeout");
            Integer merchantId = json.getInteger("merchantId");
            Integer userId = json.getInteger("userId");
            Integer machineId = json.getInteger("machineId");
            //保存http信息
            GameRecord gameRecord = new GameRecord();
            gameRecord.setUser_id(userId);
            gameRecord.setMachine_id(machineId);
            gameRecord.setMerchant_id(merchantId);
            HttpMessagelHolder.addChannel(identity,gameRecord);
        }else{
            send(ctx,"fail",HttpResponseStatus.OK);
            return;
        }
        //发送tcp
        Channel channel = TcpChannelHolder.getChannel(identity);
        if(channel==null){
            logger.error("http/channel参数错误");
            send(ctx,"fail",HttpResponseStatus.OK);
            return;
        }else{
            if(channel.isActive()){

            }else {
                TcpChannelHolder.removeChannel(identity);
                logger.error("channel已断开连接");
                send(ctx,"fail",HttpResponseStatus.OK);
                return;
            }
        }
        channel.writeAndFlush("NUM="+count+";").addListener(new ChannelFutureListener()
        {
            public void operationComplete(final ChannelFuture future)
                    throws Exception
            {
                logger.info("查询机器状态或者发送次数信息成功，返回ok：NUM="+count);
                send(ctx,"ok",HttpResponseStatus.OK);
            }
        });
    }
    /**
     * 获取body参数
     * @param request
     * @return
     */
    private String getBody(FullHttpRequest request){
        ByteBuf buf = request.content();
        return buf.toString(CharsetUtil.UTF_8);
    }

    /**
     * 发送的返回值
     * @param ctx     返回
     * @param context 消息
     * @param status 状态
     */
    private void send(ChannelHandlerContext ctx, String context, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(context, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                logger.info("http:在规定时间内没有收到客户端的上行数据, 主动断开连接");
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("http:channel registered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("http:channel unregistered");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.info("http:ERROR:" + cause.getMessage());
    }
}