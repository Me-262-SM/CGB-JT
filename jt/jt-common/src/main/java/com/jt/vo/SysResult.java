package com.jt.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
    private Integer status; //200成功  201表示失败
    private String msg;        //服务器提示信息
    private Object data;    //服务器返回值

    /**
     * 为了以后调用方便.可以重载工具API方法.
     * 实现代码的简化
     */
    public static SysResult fail() {
        return new SysResult(201, "服务器调用失败", null);
    }


    public static SysResult fail(String msg) {
        return new SysResult(201, msg, null);
    }


    public static SysResult success() {
        return new SysResult(200, "服务器调用成功", null);
    }


    public static SysResult success(Object data) {
        return new SysResult(200, "服务器调用成功", data);
    }


    public static SysResult success(String msg, Object data) {
        return new SysResult(200, msg, data);
    }


}
