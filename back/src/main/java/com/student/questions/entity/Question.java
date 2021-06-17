package com.student.questions.entity;

import java.util.Date;

/**
 * 问题
 */
public class Question {
    private Integer questionId; // 问题ID
    private Integer userId; // 用户ID
    private String questionTitle; // 问题标题
    private String questionDetails; // 问题详情
    private Date lastAnswerDate; // 上次回答的时间
    private Integer solved; // 是否已解决
    private Integer blocked; // 是否被拉黑

    public Question(Integer questionId, Integer userId, String questionTitle, String questionDetails, Date lastAnswerDate, Integer solved, Integer blocked) {
        this.questionId = questionId;
        this.userId = userId;
        this.questionTitle = questionTitle;
        this.questionDetails = questionDetails;
        this.lastAnswerDate = lastAnswerDate;
        this.solved = solved;
        this.blocked = blocked;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(String questionDetails) {
        this.questionDetails = questionDetails;
    }

    public Date getLastAnswerDate() {
        return lastAnswerDate;
    }

    public void setLastAnswerDate(Date lastAnswerDate) {
        this.lastAnswerDate = lastAnswerDate;
    }

    public Integer getSolved() {
        return solved;
    }

    public void setSolved(Integer solved) {
        this.solved = solved;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }
}
