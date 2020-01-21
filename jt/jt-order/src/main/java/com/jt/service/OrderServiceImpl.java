package com.jt.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements DubboOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrder(Order order) {
        String orderId = "" + order.getUserId() + System.currentTimeMillis();
        Date date = new Date();
        //入库订单
        order.setOrderId(orderId)
                .setStatus(1)
                .setCreated(date)
                .setUpdated(date);
        orderMapper.insert(order);
        System.out.println("订单入库成功");
        //入库订单物流
        OrderShipping orderShipping = order.getOrderShipping();
        orderShipping.setOrderId(orderId)
                .setCreated(date)
                .setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        System.out.println("订单物流入库成功");
        //订单商品入库
        List<OrderItem> items = order.getOrderItems();
        for (OrderItem item : items) {
            item.setOrderId(orderId)
                    .setCreated(date)
                    .setUpdated(date);
            orderItemMapper.insert(item);
        }
        System.out.println("订单商品入库成功");

        return orderId;
    }


    @Override
    public Order findOrderById(String id) {
        Order order = orderMapper.selectById(id);
        OrderShipping orderShipping = orderShippingMapper.selectById(id);
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", id);
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        order.setOrderShipping(orderShipping)
                .setOrderItems(items);
        return order;
    }
}
