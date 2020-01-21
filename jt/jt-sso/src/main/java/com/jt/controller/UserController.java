package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.service.UserService;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }


    @RequestMapping("check/{param}/{type}")
    public JSONPObject checkUser(@PathVariable String param,
                                 @PathVariable Integer type,
                                 String callback) {
        JSONPObject jsonpObject;
        boolean flag = userService.checkUser(param, type);
        SysResult result = SysResult.success(flag);
        jsonpObject = new JSONPObject(callback, result);
        return jsonpObject;
    }


    @RequestMapping("/query/{ticket}")
    public JSONPObject findUserByTicket(@PathVariable String ticket,
                                        HttpServletResponse response,
                                        String callback){
        JSONPObject jsonpObject = null;
        String userJSON = jedisCluster.get(ticket);
        if(StringUtils.isEmpty(userJSON)){
            //如果从缓存中获得的用户信息为空，则“删除”cookie
            Cookie cookie = new Cookie("JT_TICKET", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setDomain("jt.com");
            response.addCookie(cookie);
            jsonpObject = new JSONPObject(callback,SysResult.fail());
        }else{
            SysResult sysResult = SysResult.success(userJSON);
            jsonpObject = new JSONPObject(callback,sysResult);
        }
        return jsonpObject;
    }




}
