package com.jay.model;

import lombok.Data;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:05
 */
@Data
public class OrdersDetail {
    /**
     * 订单明细id
     */
    private String id;

    /**
     * 订单id
     */
    private String ordersId;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品属性
     */
    private String goodsKindname;
}
