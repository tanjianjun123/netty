package com.hyyn.netty.crossfire.service;


import com.hyyn.netty.crossfire.domain.GameRecord;

import java.util.List;

/**
 * @author tanjianjun
 * @create 2018-12-27 17:43
 */
public interface GameRecordService {

    public GameRecord insert(GameRecord activiti);

    /**
     * 查询
     * @return
     */
    public List<GameRecord> queryList();

    /**
     * 查询根据userId
     * @return
     */
    public List<GameRecord> queryByUserId(Integer userId);

    /**
     * 查询根据userId
     * @return
     */
    public List<GameRecord> queryByMerchantIdForRank(Integer merchantId, Integer userId);


    /**
     * 查询根据Id
     * @return
     */
    public GameRecord queryById(Integer id);

    /**
     * 查询根据Id
     * @return
     */
    public GameRecord queryByIdForPrize(Integer id);

    public Integer update(GameRecord activiti);

    public Integer delete(Integer id);

}
