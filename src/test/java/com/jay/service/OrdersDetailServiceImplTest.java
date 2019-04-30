package com.jay.service;

import com.jay.model.OrdersDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/30 15:22
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrdersDetailServiceImplTest {
    @Autowired
    private OrdersDetailService ordersDetailService;
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
    public void getDetailByOrderId() {
        String orderId = "3333";
        List<OrdersDetail> detailByOrderId = ordersDetailService.getDetailByOrderId(orderId);
        System.out.println("--->返回的数据是"+detailByOrderId);
    }
}