package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order/")
public class OrderController {

    @Reference(check = false)
    private DubboOrderService dubboOrderService;
    @Reference(check = false)
    private DubboCartService dubboCartService;


    //跳转到页面确认页
    @RequestMapping("create")
    public String create(Model model) {
        Long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = dubboCartService.findCartListBuUserId(userId);
        model.addAttribute("carts", cartList);
        return "order-cart";
    }


    @ResponseBody
    @RequestMapping("submit")
    public SysResult saveOrder(Order order) {
        order.setUserId(UserThreadLocal.get().getId());
        String ordeId = dubboOrderService.saveOrder(order);
        return SysResult.success(ordeId);
    }


    @RequestMapping("success")
    public String findOrderById(String id, Model model) {
        Order order = dubboOrderService.findOrderById(id);
        model.addAttribute("order", order);
        return "success";
    }


}
