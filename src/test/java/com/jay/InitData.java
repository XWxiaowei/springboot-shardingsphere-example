package com.jay;

import com.jay.model.Orders;
import com.jay.model.OrdersDetail;
import com.jay.service.OrdersDetailService;
import com.jay.service.OrdersService;
import com.jay.util.SnowFlake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jay.xiang
 * @create 2019/5/5 13:17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class InitData {
    static int nThreads = 100;
    private static ExecutorService pool = Executors.newFixedThreadPool(nThreads);
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersDetailService ordersDetailService;
    private SnowFlake snowFlake = new SnowFlake(0, 0);
    @Test
    public void batchInitData() {
        for (int i = 0; i < 200000; i++) {
            Orders orders = new Orders();
            String orderId = String.valueOf(snowFlake.nextId());
            orders.setId(orderId);
            orders.setAdddate(new Date());
            orders.setOrderType("1");
            orders.setOrderOrigin("2");
            orders.setParentOrdersId("222211" + (new Random().nextInt(1000)));
            orders.setParentOrdersUuid("333333");
            ordersService.saveOrders(orders);

            OrdersDetail ordersDetail = new OrdersDetail();
            ordersDetail.setId(String.valueOf(snowFlake.nextId()));
            ordersDetail.setOrdersId(orderId);
            ordersDetail.setGoodsId((new Random().nextInt(1000) + "3333"));
            ordersDetail.setGoodsName("测试商品" + (new Random().nextInt(1000)));
            ordersDetailService.saveOrderDetail(ordersDetail);
        }
    }

    @Test
    public void initData() {
        for (int i = 0; i < nThreads; i++) {
            MyRunnable myRunnable = new MyRunnable("task" + i);
            System.out.println("---------->当前线程是：" + i + " is running");
            pool.execute(myRunnable);
        }
//        关闭线程池
        pool.shutdown();
    }

    class MyRunnable implements Runnable {
        private String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        @Override
        public void run() {

            Orders orders = new Orders();
            String orderId = String.valueOf(snowFlake.nextId());
            orders.setId(orderId);
            orders.setAdddate(new Date());
            orders.setOrderType("1");
            orders.setOrderOrigin("2");
            orders.setParentOrdersId("222211" + (new Random().nextInt(1000)));
            orders.setParentOrdersUuid("333333");
            ordersService.saveOrders(orders);

            OrdersDetail ordersDetail = new OrdersDetail();
            ordersDetail.setId(String.valueOf(snowFlake.nextId()));
            ordersDetail.setOrdersId(orderId);
            ordersDetail.setGoodsId((new Random().nextInt(1000) + "3333"));
            ordersDetail.setGoodsName("测试商品" + (new Random().nextInt(1000)));
            ordersDetailService.saveOrderDetail(ordersDetail);
        }

    }

}
