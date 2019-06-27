package com.hyyn.netty.crossfire.service.impl;

import com.hyyn.netty.crossfire.dao.GameRecordMapper;
import com.hyyn.netty.crossfire.domain.GameRecord;
import com.hyyn.netty.crossfire.service.GameRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tanjianjun
 * @create 2018-12-27 17:35
 */
@Service
public class GameRecordServiceImpl implements GameRecordService {
    @Autowired
    GameRecordMapper gameRecordMapper;

    @Override
    public GameRecord queryById(Integer id) {
        return gameRecordMapper.queryById(id);
    }
    @Override
    public GameRecord queryByIdForPrize(Integer id) {
        return gameRecordMapper.queryByIdForPrize(id);
    }
    @Override
    public List<GameRecord> queryByUserId(Integer userId) {
        return gameRecordMapper.queryByUserId(userId);
    }
    @Override
    public List<GameRecord> queryByMerchantIdForRank(Integer merchantId,Integer userId) {
        return gameRecordMapper.queryByMerchantIdForRank(merchantId,userId);
    }


    @Override
    public GameRecord insert(GameRecord activiti){
        int insert = gameRecordMapper.insert(activiti);
        return activiti;
    };

    /**
     * 查询
     * @return
     */
    @Override
    public List<GameRecord> queryList(){
        List<GameRecord> activitis = gameRecordMapper.queryList();
        return activitis;
    };

    @Override
    public Integer update(GameRecord activiti){
        Integer update = gameRecordMapper.update(activiti);
        return update;
    };

    @Override
    public Integer delete(Integer id){
        Integer delete = gameRecordMapper.delete(id);
        return delete;
    };
}
