package com.jt.service.imp;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 将上述操作利用.xml形式完成
 *
 * @author Administrator
 */
@Service
public class DubboCartServiceImpl implements DubboCartService {

    @Autowired
    private CartMapper cartMapper;


    @Override
    public List<Cart> findCartListBuUserId(Long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Cart> cartList = cartMapper.selectList(queryWrapper);
        return cartList;
    }


    @Override
    public void updateCartNum(Cart cart) {
        Cart cartTemp = new Cart();
        cartTemp.setNum(cart.getNum())
                .setUpdated(new Date());
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId())
                .eq("item_id", cart.getItemId());
        cartMapper.update(cartTemp, queryWrapper);
    }


    @Override
    public void saveCart(Cart cart) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", cart.getUserId())
                .eq("item_id", cart.getItemId());
        Cart cartTemp = cartMapper.selectOne(wrapper);
        if (cartTemp == null) {
            cart.setCreated(new Date())
                    .setUpdated(cart.getCreated());
            cartMapper.insert(cart);
        } else {
            int num = cart.getNum() + cartTemp.getNum();
            cartTemp.setNum(num)
                    .setUpdated(new Date());
            cartMapper.updateById(cartTemp);
        }
    }
}
