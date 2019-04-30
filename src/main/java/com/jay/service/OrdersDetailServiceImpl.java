package com.jay.service;

import com.jay.mapper.OrdersDetailMapper;
import com.jay.model.OrdersDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/30 10:19
 */
@Service
@Slf4j
public class OrdersDetailServiceImpl implements OrdersDetailService {
    @Autowired
    private OrdersDetailMapper ordersDetailMapper;

    @Override
    public boolean saveOrderDetail(OrdersDetail ordersDetail) {
        if (ordersDetail == null) {
            return false;
        }
        int result = ordersDetailMapper.insertDetail(ordersDetail);
        return result == 1 ? true : false;
    }

    @Override
    public List<OrdersDetail> getDetailByOrderId(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            return null;
        }
        return ordersDetailMapper.selectDetailByOrderId(orderId);
    }

}
