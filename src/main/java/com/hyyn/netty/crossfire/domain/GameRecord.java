package com.hyyn.netty.crossfire.domain;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/23 14:11
 * @Version 1.0
 * 游戏记录
 */
public class GameRecord {

    private Integer id;

    private Integer user_id;

    private Integer cost_time;

    private Integer is_success;

    private Integer merchant_id;

    private Integer machine_id;

    private String create_time;

    private String update_time;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCost_time() {
        return cost_time;
    }

    public void setCost_time(Integer cost_time) {
        this.cost_time = cost_time;
    }

    public Integer getIs_success() {
        return is_success;
    }

    public void setIs_success(Integer is_success) {
        this.is_success = is_success;
    }

    public Integer getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(Integer machine_id) {
        this.machine_id = machine_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Integer getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(Integer merchant_id) {
        this.merchant_id = merchant_id;
    }

}
