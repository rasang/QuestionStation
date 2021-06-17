package com.student.questions.mapper;

import com.student.questions.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRolesMapper {
    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 角色列表
     */
    List<Role> selectByUserName(@Param("userName") String userName);

    /**
     * 更新用户角色
     * @param userId
     * @param roleId
     * @return
     */
    Integer updateUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}
