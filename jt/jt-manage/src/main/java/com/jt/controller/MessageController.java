package com.jt.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @Value("${server.port}")
    private String port;

    //动态获取当前端口号
    @RequestMapping("/getPort")
    public String getPort(){
        return "当前服务的端口号为:"+port;
    }
}
