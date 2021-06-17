package com.student.questions.mapper;

import com.student.questions.entity.Answer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnswerMapper {
    /**
     * 查询回答
     * @param offset 偏移
     * @param count 条数
     * @param blocked 是否被拉黑
     * @return 回答的列表
     */
    List<Answer> selectAnswer(@Param("offset") Integer offset, @Param("count") Integer count, @Param("blocked") Integer blocked);

    /**
     * 带关键字查询回答
     * @param offset 偏移
     * @param count 条数
     * @param key 关键词
     * @param blocked 是否被拉黑
     * @return 回答的列表
     */
    List<Answer> selectAnswerWithKey(@Param("offset") Integer offset, @Param("count") Integer count, @Param("key") String key, @Param("blocked") Integer blocked);

    /**
     * 查询回答数量
     * @param blocked 是否拉黑
     * @return 数量
     */
    Integer selectAnswerCount(@Param("blocked") Integer blocked);

    /**
     * 带关键词查询回答数量
     * @param key 关键词
     * @param blocked 是否拉黑
     * @return 数量
     */
    Integer selectAnswerWithKeyCount(@Param("key") String key, @Param("blocked") Integer blocked);

    /**
     * 根据用户ID查询回答
     * @param userId 用户ID
     * @return 回答的列表
     */
    List<Answer> selectAnswerByUserId(@Param("userId") Integer userId);

    /**
     * 根据问题ID查询回答
     * @param userId 用户ID
     * @param questionId 问题ID
     * @return 回答的列表
     */
    List<Answer> selectAnswerByQuestionId(@Param("userId") Integer userId, @Param("questionId") Integer questionId);

    /**
     * 添加回答
     * @param questionId 问题ID
     * @param userId 用户ID
     * @param answerDetails 回答详情
     * @return 查询结果
     */
    Integer insertAnswer(@Param("questionId") Integer questionId, @Param("userId") Integer userId, @Param("answerDetails") String answerDetails);

    /**
     * 根据回答ID删除回答
     * @param userId 用户ID
     * @param answerId 回答ID
     * @return 删除结果
     */
    Integer deleteAnswerByAnswerId(@Param("userId") Integer userId, @Param("answerId") Integer answerId);

    /**
     * 更新回答
     * @param details 回答详情
     * @param userId 用户ID
     * @param AnswerId 回答ID
     * @return 更新结果
     */
    Integer updateAnswer(@Param("answerDetails") String details, @Param("userId") Integer userId, @Param("answerId") Integer AnswerId);

    /**
     * 更新回答是否拉黑
     * @param blocked 是否拉黑
     * @param answerId 回答ID
     * @return 更新结果
     */
    Integer updateAnswerBlock(@Param("blocked") Integer blocked, @Param("answerId") Integer answerId);
}
