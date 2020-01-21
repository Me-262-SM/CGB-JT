package com.jt;

import com.jt.util.HttpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringBootRunTest {
    @Autowired
    private HttpService httpService;

    @Test
    void httpTest(){
        String s = httpService.doGet("http://manage.jt.com/");
        System.out.println(s);
    }
}