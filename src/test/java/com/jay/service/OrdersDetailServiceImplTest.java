package com.jay.service;

import com.jay.mapper.sharding.OrdersDetailMapper;
import com.jay.model.OrdersDetail;
import com.jay.util.SnowFlake;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jay.xiang
 * @create 2019/4/30 15:22
 */
public class OrdersDetailServiceImplTest extends BaseServiceTest{
    @Autowired
    private OrdersDetailService ordersDetailService;
    private SnowFlake snowFlake = new SnowFlake(2,3);
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersDetailMapper ordersDetailMapper;
    @Test
    public void saveOrderDetail() {
        OrdersDetail ordersDetail = new OrdersDetail();
        ordersDetail.setId("111");
        ordersDetail.setGoodsId("2222");
        ordersDetail.setOrdersId("201905071222");
        ordersDetail.setGoodsName("测试商品");
        ordersDetail.setGoodsKindname("测试类目");
        ordersDetailService.saveOrderDetail(ordersDetail);
    }

    @Test
    public void getDetailByOrderId() {
        String orderId = "2222";
        List<OrdersDetail> detailByOrderId = ordersDetailService.getDetailByOrderId(orderId);
        System.out.println("--->返回的数据是"+detailByOrderId);
    }
    @Test
    public void getDetailById() {
        String id = "3333";
        List<OrdersDetail> detailByOrderId = ordersDetailMapper.selectDetailById(id);
        System.out.println("--->返回的数据是"+detailByOrderId);
    }
}