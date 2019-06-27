package com.hyyn.netty.crossfire.dao;


import com.hyyn.netty.crossfire.domain.GameRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tanjianjun
 * @create 2018-11-15 16:28
 */
@Mapper
public interface GameRecordMapper {
    /**
     * 新增
     * @param gameRecord
     * @return
     */
    int insert(GameRecord gameRecord);

    /**
     * 查询所有
     * @return
     */
    List<GameRecord> queryList();
    /**
     * 查询所有根据id
     * @return
     */
    GameRecord queryById(Integer id);
    /**
     * 查询所有根据id
     * @return
     */
    GameRecord queryByIdForPrize(Integer id);
    /**
     * 查询所有根据user_id
     * @return
     */
    List<GameRecord> queryByUserId(Integer userId);

    /**
     * 查询所有根据merchant_id
     * @return
     */
    List<GameRecord> queryByMerchantIdForRank(@Param("merchantId") Integer merchantId, @Param("userId") Integer userId);

    /**
     * 根据id
     * @param gameRecord
     */
    Integer update(GameRecord gameRecord);

    /**
     * 根据id
     * @param id
     */
    Integer delete(Integer id);

}