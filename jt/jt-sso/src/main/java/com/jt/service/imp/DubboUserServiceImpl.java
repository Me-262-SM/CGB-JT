package com.jt.service.imp;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.UUID;

@Service
public class DubboUserServiceImpl implements DubboUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public void save(User user) {
        String password = user.getPassword();
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(md5DigestAsHex)
                .setEmail(user.getPhone())
                .setCreated(new Date())
                .setUpdated(user.getCreated());

        userMapper.insert(user);
    }


    @Override
    public String findUserByUP(User user) {
        String md5psw = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5psw);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User userDB = userMapper.selectOne(queryWrapper);
        if (userDB == null)
            return null;
        //用户输入信息正确
        String ticket = UUID.randomUUID().toString();
        //防止敏感数据泄露，一般会对数据进行脱密处理
        userDB.setPassword("想知道吗？不告诉你！！！");
        String json = JacksonUtils.toJson(userDB);
        jedisCluster.setex(ticket, 7 * 24 * 3600, json);
        return ticket;
    }
}
