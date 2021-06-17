package com.student.questions.entity;

import java.util.Date;

/**
 * 问题
 */
public class Answer {
    private Integer answerId; // 回答ID
    private Integer questionId; // 问题ID
    private Integer userId; // 用户ID
    private String answerDetails; // 回答详情
    private Date answerDate; // 问题发表日期
    private Integer blocked; //

    public Answer(Integer answerId, Integer questionId, Integer userId, String answerDetails, Date answerDate, Integer blocked) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.userId = userId;
        this.answerDetails = answerDetails;
        this.answerDate = answerDate;
        this.blocked = blocked;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
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

    public String getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(String answerDetails) {
        this.answerDetails = answerDetails;
    }

    public Date getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = answerDate;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }
}
