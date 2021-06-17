package com.student.questions.mapper;

import com.student.questions.entity.Pass;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface PassMapper {
    Integer insertPass(@Param("userName") String user, @Param("verifyCode") String code, @Param("time") Long time) throws DuplicateKeyException;
    List<Pass> selectPass(@Param("userName") String user) throws DuplicateKeyException;
}
