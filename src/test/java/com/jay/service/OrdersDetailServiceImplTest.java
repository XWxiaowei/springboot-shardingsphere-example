package com.jay.service;

import com.jay.model.Orders;
import com.jay.model.OrdersDetail;
import com.jay.util.SnowFlake;
import com.jay.util.UUIDutil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author jay.xiang
 * @create 2019/4/30 15:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrdersDetailServiceImplTest {
    @Autowired
    private OrdersDetailService ordersDetailService;
    private SnowFlake snowFlake = new SnowFlake(2,3);
    @Autowired
    private OrdersService ordersService;
    @Test
    public void saveOrderDetail() {
        OrdersDetail ordersDetail = new OrdersDetail();
        ordersDetail.setId("111");
        ordersDetail.setGoodsId("2222");
        ordersDetail.setOrdersId("3333");
        ordersDetail.setGoodsName("测试商品");
        ordersDetailService.saveOrderDetail(ordersDetail);
    }

    @Test
    public void saveOrders() {
        Orders orders = new Orders();
        String orderId = String.valueOf(snowFlake.nextId());
        orders.setId(orderId);
        orders.setAdddate(new Date());
        orders.setOrderType("1");
        orders.setOrderOrigin("2");
        orders.setParentOrdersId("222211"+(new Random().nextInt(1000)));
        orders.setParentOrdersUuid(UUIDutil.getUUID());
        ordersService.saveOrders(orders);
    }

    @Test
    public void getDetailByOrderId() {
        String orderId = "3333";
        List<OrdersDetail> detailByOrderId = ordersDetailService.getDetailByOrderId(orderId);
        System.out.println("--->返回的数据是"+detailByOrderId);
    }
}