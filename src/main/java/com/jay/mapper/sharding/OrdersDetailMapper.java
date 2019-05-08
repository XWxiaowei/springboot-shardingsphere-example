package com.jay.mapper.sharding;

import com.jay.model.OrdersDetail;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:02
 */
public interface OrdersDetailMapper {

    /**
     * @param ordersDetail
     * @return
     */
    int insertDetail(OrdersDetail ordersDetail);

    /**
     * 根据订单ID查询订单明细
     * @param orderId
     * @return
     */
    List<OrdersDetail> selectDetailByOrderId(String orderId);

    /**
     * @param name
     * @return
     */
    List<OrdersDetail> selectDetailByName(String name);

    /**
     * @param id
     * @return
     */
    List<OrdersDetail> selectDetailById(String id);
}
