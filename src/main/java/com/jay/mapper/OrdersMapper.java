package com.jay.mapper;

import com.jay.model.Orders;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:01
 */
public interface OrdersMapper {

    /**
     *
     * @param orders
     * @return
     */
    int insertOrders(Orders orders);

    /**
     * @param id
     * @return
     */
    Orders selectById(String id);
}
