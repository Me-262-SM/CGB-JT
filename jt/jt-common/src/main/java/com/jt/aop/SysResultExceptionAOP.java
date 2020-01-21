package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class SysResultExceptionAOP {
    /**
     * 统一返回数据 SysResult对象 status=201
     */
    @ExceptionHandler(RuntimeException.class)
    public Object fail(Exception exception, HttpServletRequest request) {
        String callback = request.getParameter("callback");
        if(!StringUtils.isEmpty(callback)){
            //说明用户发起的是跨域请求
            exception.printStackTrace();
            return new JSONPObject(callback,SysResult.fail());
        }else{
            exception.printStackTrace();
            return SysResult.fail();
        }
    }
}
