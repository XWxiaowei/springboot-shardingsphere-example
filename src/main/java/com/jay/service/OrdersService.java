package com.jay.service;

import com.jay.model.Orders;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/30 10:18
 */
public interface OrdersService {

    /**
     * @param orders
     * @return
     */
    boolean saveOrders(Orders orders);

    /**
     * @param id
     * @return
     */
    Orders getOrderById(String id);
}
