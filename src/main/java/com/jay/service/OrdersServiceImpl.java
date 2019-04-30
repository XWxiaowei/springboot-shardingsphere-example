package com.jay.service;

import com.jay.mapper.OrdersMapper;
import com.jay.model.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/30 10:18
 */
@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Override
    public boolean saveOrders(Orders orders) {
        if (orders==null) {
            return false;
        }
        int result = ordersMapper.insertOrders(orders);
        return result == 1 ? true : false;
    }

    @Override
    public Orders getOrderById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return ordersMapper.selectById(id);
    }
}
