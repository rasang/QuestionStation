package com.student.questions.entity;

import lombok.Data;

/**
 * 用户信息
 */
@Data
public class UserInfo {
    private Integer userId; // 用户ID
    private String userName; // 用户名
    private String userAvatar; // 用户头像
    private String userTele; // 用户电话
    private String userIntro; // 用户介绍
    private String userSite; // 用户地址
    private String userIndustry; // 用户行业
}
