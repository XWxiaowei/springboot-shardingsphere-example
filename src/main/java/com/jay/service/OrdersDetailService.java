package com.jay.service;

import com.jay.model.OrdersDetail;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/30 10:19
 */
public interface OrdersDetailService {

    /**
     * @param ordersDetail
     * @return
     */
    boolean saveOrderDetail(OrdersDetail ordersDetail);

    /**
     * @param orderId
     * @return
     */
    List<OrdersDetail> getDetailByOrderId(String orderId);

    /**
     * @param name
     * @return
     */
    List<OrdersDetail> getDetailByName(String name);

}
