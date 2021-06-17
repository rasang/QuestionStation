package com.student.questions.mapper;

import com.student.questions.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param userName 用户名
     * @param userTele 用户电话
     * @param intro 用户介绍
     * @param userSite 用户地址
     * @param userIndustry 用户行业
     * @return 更新结果
     */
    Integer updateUserInfo(@Param("userId") Integer userId, @Param("userName") String userName, @Param("userTele") String userTele, @Param("userIntro") String intro, @Param("userSite") String userSite, @Param("userIndustry") String userIndustry);

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息对象
     */
    UserInfo selectUserInfoById(@Param("userId") Integer userId);

    /**
     * 设置用户新头像
     * @param avatar 头像url
     * @param userId 用户ID
     * @return 设置结果
     */
    Integer setNewAvatar(@Param("userAvatar") String avatar, @Param("userId") Integer userId);

    /**
     * 根据角色查询用户信息
     * @param roleName 角色名
     * @param offset 偏移
     * @param count 数量
     * @return 用户信息列表
     */
    List<UserInfo> selectUserInfoByRole(@Param("roleName") String roleName, @Param("offset") Integer offset, @Param("count") Integer count);

    /**
     * 根据角色名查询用户信息数量
     * @param roleName 角色名
     * @return 数量
     */
    Integer selectUserInfoByRolePageCount(@Param("roleName") String roleName);

    /**
     * 根据用户名和角色名查询用户信息
     * @param roleName 角色名
     * @param offset 偏移
     * @param count 数量
     * @param userName 用户名
     * @return 用户信息列表
     */
    List<UserInfo> selectUserInfoByRoleWithUserName(@Param("roleName") String roleName, @Param("offset") Integer offset, @Param("count") Integer count, @Param("userName") String userName);

    /**
     * 根据用户名和角色名查询用户信息的数量
     * @param roleName 角色名
     * @param userName 用户名
     * @return 数量
     */
    Integer selectUserInfoByRoleWithUserNamePageCount(@Param("roleName") String roleName, @Param("userName") String userName);
}
