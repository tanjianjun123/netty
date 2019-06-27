package com.hyyn.netty.tcp.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyyn.netty.crossfire.domain.GameRecord;
import com.hyyn.netty.crossfire.service.GameRecordService;
import com.hyyn.netty.crossfire.utils.HttpUtil;
import com.hyyn.netty.http.server.HttpMessagelHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/6 15:35
 * @Version 1.0
 */
@Component
@ChannelHandler.Sharable
public class TcpServerHandler extends SimpleChannelInboundHandler<String> {

    private final static Logger logger = LoggerFactory.getLogger("tcp-info");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private GameRecordService gameRecordService;
    @Value("${service_address}")
    private String serviceAddress;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        logger.info("tcp:客户端连接上了+"+ctx.channel().remoteAddress());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("tcp:客户端断开连接了"+ctx.channel().remoteAddress());

        ctx.close();
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        /**
         * SN:5908475A,
         * NUM:0,
         * PLAY:0,
         * TIME:00'00"00,
         * BAT:00;
         */
        //METHOD:auth （发送认证）
        //METHOD:beat （发送心跳）
        //METHOD:resu （返回闯关结果)
        //METHOD:info(返回NUM的查询结果)
        logger.info("接收tcp信息："+(String)msg);
        if(msg!=null&&msg.length()!=0){
            //遍历封装
            msg=msg.trim();
            Map<String, Object> map = new HashMap<>();
            String[] strArray = msg.split(",");
            for (int i = 0; i < strArray.length; i++) {
                String[] split = strArray[i].split(":");
                map.put(split[0].trim(),split[1].trim());
            }
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//               logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//            }

            String method = String.valueOf(map.get("METHOD"));
            if(method==null||method.length()==0){
                logger.error("method参数错误");
                ctx.channel().writeAndFlush("fail"+";");
                return;
            }
            if(!method.equals("beat")){
                //保存连接
                String sn = String.valueOf(map.get("SN"));
                if(sn!=null){
                    TcpChannelHolder.addChannel(sn,ctx.channel());
                }else{
                    logger.error("sn参数错误");
                    ctx.channel().writeAndFlush("fail"+";");
                    return;
                }
            }
            if(method.equals("beat")){
                //心跳包
                return;
            }else if(method.equals("info")||method.equals("auth")){
                //状态信息查询
                String sn = String.valueOf(map.get("SN"));
                String num = String.valueOf(map.get("NUM"));
                String play = String.valueOf(map.get("PLAY"));
                String bat = String.valueOf(map.get("BAT"));
                if(play==null||play.length()==0||sn==null||sn.length()==0||bat==null||bat.length()==0){
                    logger.error("play/sn/bat参数错误");
                    ctx.channel().writeAndFlush("fail"+";");
                    return;
                }
                //状态信息查询
                Map<String, String> param = new HashMap<String, String>();
                param.put("identity",sn);
                param.put("num",num);
                param.put("play",play);
                param.put("bat",bat);
                String res = HttpUtil.postJson(serviceAddress + "/wx/machine/getStatus.action", JSON.toJSONString(param));
                logger.info("发送当前机器状态信息 返回结果："+res);
                JSONObject jsonObject = JSONObject.parseObject(res);
                Integer code = jsonObject.getInteger("code");
                if(code==200){
                    ctx.channel().writeAndFlush("ok"+";");
                }else{
                    ctx.channel().writeAndFlush("fail"+";");
                }
            }else if(method.equals("resu")){
                //闯关结果反馈
                String play = String.valueOf(map.get("PLAY"));
                String time=String.valueOf(map.get("TIME"));
                String timeStr = time.replaceAll("^(0+)", "");
                if(timeStr==null||"".equals(timeStr)){
                    timeStr="0";
                }
                time = timeStr;
                String sn = String.valueOf(map.get("SN"));
                if(play==null||play.length()==0||time==null||time.length()==0||sn==null||sn.length()==0){
                    logger.error("play/time/sn参数错误");
                    ctx.channel().writeAndFlush("fail"+";");
                    return;
                }
                GameRecord gameReoord = HttpMessagelHolder.getGameReoord(sn);
                if(gameReoord==null||gameReoord.getUser_id()==null){
                    logger.error("获取内存中gameRecord对象错误");
                    ctx.channel().writeAndFlush("fail"+";");
                    return;
                }
                gameReoord.setCost_time(Integer.valueOf(time));
                gameReoord.setCreate_time( dateFormat.format(new Date()));
                //如果是失败
                if(play.equals("3")){
                    logger.info("闯关失败:sn:"+sn+"/time:"+time+"/play:"+play);
                    gameReoord.setIs_success(0);
                    GameRecord insert= gameRecordService.insert(gameReoord);
                    logger.info("新增闯关记录成功：id"+insert.getId());
                    ctx.channel().writeAndFlush("ok"+";");
                }
                if(play.equals("2")){
                    logger.info("闯关成功:sn:"+sn+"/time:"+time+"/play:"+play);
                    gameReoord.setIs_success(1);
                    GameRecord insert=gameRecordService.insert(gameReoord);
                    if(insert==null){
                        ctx.channel().writeAndFlush("fail"+";");
                        logger.error("新增闯关记录失败:"+gameReoord.toString());
                        return;
                    }
                    logger.info("新增闯关记录成功：id"+insert.getId());
                    //新增完闯关记录后，开始新增奖品记录
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("gameRecordId",String.valueOf(insert.getId()));
                    String post = HttpUtil.post(serviceAddress + "/wx/prize/order/insert.action", param);
                    logger.info("闯关成功后新增奖品券 返回结果："+post);
                    JSONObject jsonObject = JSONObject.parseObject(post);
                    if(jsonObject==null){
                        //如果奖品新增失败，就删除刚刚新增的闯关记录信息，免得重复新增
                        gameRecordService.delete(insert.getId());
                        logger.error("奖品新增失败，删除刚刚新增的闯关记录信息id:"+insert.getId());
                        ctx.channel().writeAndFlush("fail"+";");
                    }else{
                        Integer code = jsonObject.getInteger("code");
                        if(code==200){
                            ctx.channel().writeAndFlush("ok"+";");
                        }else{
                            gameRecordService.delete(insert.getId());
                            logger.error("奖品新增失败，删除刚刚新增的闯关记录信息id:"+insert.getId());
                            ctx.channel().writeAndFlush("fail"+";");
                        }
                    }
                }
            }else{
                logger.error("tcp信息接收错误");
                ctx.channel().writeAndFlush("fail"+";");
            }

        }else{
            logger.error("tcp信息接收错误");
            ctx.channel().writeAndFlush("fail"+";");
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接(时间设置在配置类中)
                logger.info("tcp:在规定时间内没有收到客户端的上行数据, 主动断开连接");
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("tcp:channel registered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("tcp:channel unregistered");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.info("tcp:ERROR:" + cause.getMessage());
    }
}