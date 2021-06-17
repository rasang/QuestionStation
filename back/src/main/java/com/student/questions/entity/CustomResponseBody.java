package com.student.questions.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义响应体
 */
public class CustomResponseBody {
    private Map response; // Map响应体

    public CustomResponseBody(Integer code, String msg){
        this.response = new HashMap();
        response.put("code", code); // 响应码
        response.put("msg", msg); // 提示消息
    }

    public void addData(Object object){
        response.put("data", object); // 数据
    }

    public Map getMap(){
        return response;
    }

}
