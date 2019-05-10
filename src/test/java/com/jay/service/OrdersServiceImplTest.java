package com.jay.service;

import com.jay.model.Orders;
import com.jay.util.UUIDutil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author jay.xiang
 * @create 2019/5/5 19:00
 */
public class OrdersServiceImplTest extends BaseServiceTest {
    @Autowired
    private OrdersService ordersService;
    @Test
    public void saveOrders() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

        Orders orders = new Orders();
        String orderId = dateFormat.format(new Date())+new Random().nextInt(1000);
//        String orderId = "201905071222";
        orders.setId(orderId);
        orders.setAdddate(new Date());
        orders.setOrderType("1");
        orders.setOrderOrigin("2");
        orders.setParentOrdersId("222211"+(new Random().nextInt(1000)));
        orders.setParentOrdersUuid(UUIDutil.getUUID());
        ordersService.saveOrders(orders);

//        orders.setId("20200102111");
//        try {
//            dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//            Date parse = dateFormat.parse("2020-01-02 20:08:09");
//            orders.setAdddate(parse);
//            ordersService.saveOrders(orders);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void getOrderById() {
        String id = "201905071211";
        Orders orderById = ordersService.getOrderById(id);
        System.out.println("getOrderById返回结果："+orderById);
    }

    @Test
    public void queryOrdersPage() {
        String id ="201905071222";
        List<Orders> orders = ordersService.queryOrdersPage(id, 0, 10);
        System.out.println("queryOrdersPage返回结果："+orders);
    }

    @Test
    public void queryByIds() {
        List<String> ids = new ArrayList<>();
        ids.add("201905071222");
        ids.add("20200102111");
        List<Orders> ordersList = ordersService.queryByIds(ids);
        System.out.println("queryByIds返回结果："+ordersList);
    }

    @Test
    public void queryBetweenDate() {
        String startTime = "2019-09-01 00:00:00";
        String endTime = "2020-09-01 23:59:59";
        List<Orders> orders = ordersService.queryBetweenDate(startTime, endTime);
        System.out.println("----->queryBetweenDate返回的结果是={}" + orders);
    }
}