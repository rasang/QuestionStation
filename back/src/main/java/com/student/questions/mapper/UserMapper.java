package com.student.questions.mapper;

import com.student.questions.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DuplicateKeyException;

import java.sql.SQLIntegrityConstraintViolationException;

@Mapper
public interface UserMapper {
    /**
     * 查询用户
     * @param userName 用户名
     * @param password 密码
     * @return 用户对象
     */
    User selectUser(@Param("userName") String userName,@Param("password") String password);

    /**
     * 插入新用户
     * @param userName 用户名
     * @param password 用户密码
     * @return 插入结果
     * @throws DuplicateKeyException
     */
    Integer insertUser(@Param("userName") String userName, @Param("password") String password) throws DuplicateKeyException;

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户对象
     */
    User selectUserById(@Param("userId") Integer userId);
}
