package com.jay.mapper;

import com.jay.model.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/29 19:01
 */
public interface OrdersMapper {

    /**
     * @param orders
     * @return
     */
    int insertOrders(Orders orders);

    /**
     * @param id
     * @return
     */
    Orders selectById(String id);

    /**
     * 分页查询
     *
     * @param id
     * @param current
     * @param pageSize
     * @return
     */
    List<Orders> queryOrdersPage(
            @Param("id") String id,
            @Param("current") int current,
            @Param("pageSize") int pageSize);
}
