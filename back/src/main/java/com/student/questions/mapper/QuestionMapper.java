package com.student.questions.mapper;

import com.student.questions.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    /**
     * 查询问题
     * @param offset 偏移
     * @param count 数量
     * @return 问题的列表
     */
    List<Question> selectQuestion(@Param("offset") Integer offset,@Param("count") Integer count);

    /**
     * 带关键字查询问题
     * @param offset 偏移
     * @param count 数量
     * @param key 关键字
     * @return 问题的列表
     */
    List<Question> selectQuestionWithKey(@Param("offset") Integer offset,@Param("count") Integer count, @Param("key") String key);

    /**
     * 查询问题的数量
     * @return 数量
     */
    Integer selectQuestionCount();

    /**
     * 带关键字查询问题的数量
     * @param key 关键字
     * @return 数量
     */
    Integer selectQuestionWithKeyCount(@Param("key") String key);

    /**
     * 带用户ID查询额外难题
     * @param userId 用户ID
     * @param offset 偏移
     * @param count 数量
     * @return 问题列表
     */
    List<Question> selectQuestionWithUserId(@Param("userId") Integer userId, @Param("offset") Integer offset,@Param("count") Integer count);

    /**
     * 根据用户ID查询问题的数量
     * @param userId 用户ID
     * @return 数量
     */
    Integer selectQuestionWithUserIdCount(@Param("userId") Integer userId);

    /**
     * 根据ID查询问题
     * @param questionId 问题ID
     * @return 问题示例
     */
    Question selectQuestionById(@Param("questionId") Integer questionId);

    /**
     * 根据用户ID查询问题
     * @param userId 用户ID
     * @return 问题列表
     */
    List<Question> selectQuestionByUserId(@Param("userId") Integer userId);

    /**
     * 查询拉黑的问题
     * @param offset 偏移
     * @param count 数量
     * @return 问题列表
     */
    List<Question> selectBlockedQuestion(@Param("offset") Integer offset,@Param("count") Integer count);

    /**
     * 带关键字查询问题
     * @param offset 偏移
     * @param count 数量
     * @param key 关键字
     * @return 问题列表
     */
    List<Question> selectBlockedQuestionWithKey(@Param("offset") Integer offset,@Param("count") Integer count, @Param("key") String key);

    /**
     * 查询拉黑的问题数量
     * @return 数量
     */
    Integer selectBlockedQuestionCount();

    /**
     * 带关键字查询拉黑的问题的数量
     * @param key 关键字
     * @return 数量
     */
    Integer selectBlockedQuestionWithKeyCount(@Param("key") String key);

    /**
     * 插入新的问题
     * @param userId 用户ID
     * @param questionTitle 问题标题
     * @param questionDetails 问题详情
     * @return 插入结果
     */
    Integer insertQuestion(@Param("userId") Integer userId, @Param("questionTitle") String questionTitle, @Param("questionDetails") String questionDetails);

    /**
     * 更新问题
     * @param questionId 问题ID
     * @param userId 用户ID
     * @param title 问题标题
     * @param details 问题详情
     * @return
     */
    Integer updateQuestion(@Param("questionId") Integer questionId, @Param("userId") Integer userId, @Param("title") String title, @Param("details") String details);

    /**
     * 更新问题是否已解决
     * @param solved 是否解决
     * @param questionId 问题ID
     * @param userId 用户ID
     * @return 更新结果
     */
    Integer updateQuestionSolved(@Param("solved") Integer solved, @Param("questionId") Integer questionId, @Param("userId") Integer userId);

    /**
     * 更新问题是否被拉黑
     * @param blocked 是否被拉黑
     * @param questionId 问题ID
     * @return 更新结果
     */
    Integer updateQuestionBlocked(@Param("blocked") Integer blocked, @Param("questionId") Integer questionId);
}
