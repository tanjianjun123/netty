package com.hyyn.netty.tcp.server;

import io.netty.channel.Channel;

import java.util.Hashtable;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/23 9:55
 * @Version 1.0
 */
public class TcpChannelHolder {
    private static Hashtable<String, Channel> identityHashtable = new Hashtable<>();
    private static Hashtable<String, String> ipHashtable = new Hashtable<>();

    /**
     * add channel
     *
     * @param identity 标识
     * @param channel Channel
     */
    public static synchronized void addChannel(String identity, Channel channel) {
        Channel channelId = identityHashtable.get(identity);
        if(channelId==null){
            identityHashtable.put(identity, channel);
        }else{
            if(!channelId.remoteAddress().equals(channel.remoteAddress())){
                identityHashtable.put(identity, channel);
            }
        }
    }

    /**
     * get channel
     *
     * @param identity 标识
     * @return channel
     */
    public static Channel getChannel(String identity) {
        return identityHashtable.get(identity);
    }

    /**
     * remove channel
     *
     * @param identity identity
     */
    public static synchronized void removeChannel(String identity) {
        if (identity != null) {
            Channel channel = identityHashtable.remove(identity);
            if (channel != null) {
                channel.close();
            }
        }
    }

    /**
     * get identity
     *
     * @param ip ip
     * @return identity
     */
    public static String getIdentity(String ip) {
        return ipHashtable.get(ip);
    }
}
