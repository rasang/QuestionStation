package com.student.questions.entity;
import java.util.Date;

public class Pass {
    //自增ID
    private Integer id;

    public Pass(Integer id, String user, String code, Long time) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.time = time;
    }

    // 用户邮箱或电话
    private String user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    // 验证码
    private String code;
    // 发送时间
    private Long time;
}
