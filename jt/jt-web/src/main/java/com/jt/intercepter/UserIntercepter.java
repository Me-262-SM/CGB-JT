package com.jt.intercepter;

import com.jt.pojo.User;
import com.jt.util.JacksonUtils;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserIntercepter implements HandlerInterceptor {
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = "";
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            response.sendRedirect("/user/login.html");
            return false;
        }
        for (Cookie cookie : cookies) {
            if ("JT_TICKET".equals(cookie.getName())) {
                ticket = cookie.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(ticket)) {
            response.sendRedirect("/user/login.html");
            return false;
        }
        //ticket有值
        String userJSON = jedisCluster.get(ticket);
        if (StringUtils.isEmpty(userJSON)) {
            //删除redis和cookie中的数据
            jedisCluster.del(ticket);
            Cookie ticketCookie = new Cookie("JT_TICKET", "");
            ticketCookie.setMaxAge(0);
            ticketCookie.setPath("/");  //cookie的权限设置
            ticketCookie.setDomain("jt.com");
            response.addCookie(ticketCookie);
            response.sendRedirect("/user/login.html");
            return false;
        }
        //放行请求
        User user = JacksonUtils.toPojo(userJSON, User.class);
        UserThreadLocal.set(user);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
