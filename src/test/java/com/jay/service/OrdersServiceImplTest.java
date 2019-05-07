package com.jay.service;

import com.jay.model.Orders;
import com.jay.util.UUIDutil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author jay.xiang
 * @create 2019/5/5 19:00
 */
public class OrdersServiceImplTest extends BaseServiceTest {
    @Autowired
    private OrdersService ordersService;
    @Test
    public void saveOrders() {
        Orders orders = new Orders();
        String orderId = "201905071222";
        orders.setId(orderId);
        orders.setAdddate(new Date());
        orders.setOrderType("1");
        orders.setOrderOrigin("2");
        orders.setParentOrdersId("222211"+(new Random().nextInt(1000)));
        orders.setParentOrdersUuid(UUIDutil.getUUID());
        ordersService.saveOrders(orders);
    }

    @Test
    public void getOrderById() {
        String id = "201905071211";
        Orders orderById = ordersService.getOrderById(id);
        System.out.println("getOrderById返回结果："+orderById);
    }

    @Test
    public void queryOrdersPage() {
        String id ="322452513775157249";
        List<Orders> orders = ordersService.queryOrdersPage(id, 0, 10);
        System.out.println("queryOrdersPage返回结果："+orders);
    }

    @Test
    public void queryByIds() {
        List<String> ids = new ArrayList<>();
        ids.add("322452513775157249");
        ids.add("322454449685528577");
        ids.add("322427920679448576");
        List<Orders> ordersList = ordersService.queryByIds(ids);
        System.out.println("queryByIds返回结果："+ordersList);
    }
}