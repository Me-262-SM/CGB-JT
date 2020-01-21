package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Reference(timeout = 3000, check = false)
    private DubboCartService dubboCartService;


    @RequestMapping("show")
    public String show(Model model) {
        Long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = dubboCartService.findCartListBuUserId(userId);
        model.addAttribute("cartList", cartList);
        return "cart";
    }


    @ResponseBody
    @RequestMapping("update/num/{itemId}/{num}")
    public SysResult updateCartNum(Cart cart) {
        cart.setUserId(UserThreadLocal.get().getId());
        dubboCartService.updateCartNum(cart);
        return SysResult.success();
    }


    @RequestMapping("add/{itemId}")
    public String saveCart(Cart cart) {
        cart.setUserId(UserThreadLocal.get().getId());
        dubboCartService.saveCart(cart);
        return "redirect:/cart/show.html";
    }

}

