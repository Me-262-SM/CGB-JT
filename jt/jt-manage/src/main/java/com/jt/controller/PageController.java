package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    
    @RequestMapping("page/{moduleName}")
    public String pageJump(@PathVariable String moduleName) {
        return moduleName;
    }
    
    @PostMapping("/user")
    public void  addUser() {
        
    }
    
    @GetMapping("/user")
    public void  getUser() {
        
    }
    
    
}
