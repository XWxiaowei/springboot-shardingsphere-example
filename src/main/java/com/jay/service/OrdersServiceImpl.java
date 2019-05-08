package com.jay.service;

import com.jay.mapper.sharding.OrdersMapper;
import com.jay.model.Orders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Transactional(rollbackFor = Exception.class)
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

    /**
     * @param id
     * @param current
     * @param pageSize
     * @return
     */
    @Override
    public List<Orders> queryOrdersPage(String id,int current, int pageSize) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return ordersMapper.queryOrdersPage(id, current, 10);
    }

    @Override
    public List<Orders> queryByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return ordersMapper.queryInById(ids);
    }

    @Override
    public List<Orders> queryBetweenDate(String startTime, String endTime) {
        if (StringUtils.isAnyBlank(startTime, endTime)) {
            return null;
        }
        return ordersMapper.queryBetweenDate(startTime, endTime);
    }

}
