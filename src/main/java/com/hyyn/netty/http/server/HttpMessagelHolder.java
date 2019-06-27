package com.hyyn.netty.http.server;

import com.hyyn.netty.crossfire.domain.GameRecord;

import java.util.Hashtable;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/23 9:55
 * @Version 1.0
 */
public class HttpMessagelHolder {
    private static Hashtable<String, GameRecord> messageHashtable = new Hashtable<>();

    /**
     * add channel
     *
     * @param identity 标识
     * @param gameRecord GameRecord
     */
    public static synchronized void addChannel(String identity, GameRecord gameRecord) {
        messageHashtable.put(identity,gameRecord);
    }

    /**
     * get channel
     *
     * @param identity 标识
     * @return channel
     */
    public static GameRecord getGameReoord(String identity) {
        return messageHashtable.get(identity);
    }

}
