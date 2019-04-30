package com.jay.model;

import lombok.Data;

import java.util.Date;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:05
 */
@Data
public class Orders {
    /**
     * 订单id
     */
    private String id;

    /**
     *  业务平台的订单id
     */
    private String parentOrdersUuid;
    /**
     * 业务平台的订单编号
     */
    private String parentOrdersId;
    /**
     * 订单来源
     */
    private String orderOrigin;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 创建时间
     */
    private Date adddate;

}
