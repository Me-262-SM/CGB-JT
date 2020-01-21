package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/")
public class UserController {
    /*
     * check表示请求是否检验服务提供者
     * loadbalance  负载均衡
     * random   随机（默认）  RandomLoadBalance
     * roundrobin   轮询  RoundRobinLoadBalance
     * consistenthash    Iphash策略  ConsistentHashLoadBalance
     * leastactive    最小活跃数  LeastActiveLoadBalance
     */
    @Reference(timeout = 3000,check = false)
    private DubboUserService dubboUserService;
    @Autowired
    private JedisCluster jedisCluster;

    //通用页面跳转
    @RequestMapping("{moduleName}")
    public String module(@PathVariable String moduleName) {
        return moduleName;
    }


    @ResponseBody
    @RequestMapping("doRegister")
    public SysResult doRegister(User user) {
        dubboUserService.save(user);
        return SysResult.success();
    }


    @ResponseBody
    @RequestMapping("doLogin")
    public SysResult doLogin(User user, HttpServletResponse response) {
        String ticket = dubboUserService.findUserByUP(user);
        if (StringUtils.isEmpty(ticket)) {
            return SysResult.fail();
        }
        //校验成功后写入ccokie
        Cookie ticketCookie = new Cookie("JT_TICKET", ticket);
        ticketCookie.setMaxAge(7 * 24 * 60 * 60);
        ticketCookie.setPath("/");  //cookie的权限设置
        ticketCookie.setDomain("jt.com");
        response.addCookie(ticketCookie);
        return SysResult.success();
    }


    //重定向到系统首页
    @RequestMapping("logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        String ticket="";
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length==0){
            return "redirect:/";
        }
        for (Cookie cookie : cookies) {
            if ("JT_TICKET".equals(cookie.getName())) {
                ticket = cookie.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(ticket)) {
            return "redirect:/";
        }
        //删除redis和cookie中的数据
        jedisCluster.del(ticket);
        Cookie ticketCookie = new Cookie("JT_TICKET", "");
        ticketCookie.setMaxAge(0);
        ticketCookie.setPath("/");  //cookie的权限设置
        ticketCookie.setDomain("jt.com");
        response.addCookie(ticketCookie);
        return "redirect:/";
    }
}
