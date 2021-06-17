package com.student.questions.controller;

import com.student.questions.entity.CustomResponseBody;
import com.student.questions.service.AnswerService;
import com.student.questions.service.QuestionService;
import com.student.questions.service.UserInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8081", allowCredentials = "true")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    AnswerService answerService;

    /**
     * 检查登录状态
     * @return 自定义响应体
     */
    @RequestMapping("/checkLogin")
    public Object checkLogin(){
        return new CustomResponseBody(200, "OK").getMap();
    }

    /**
     * 发表问题
     * @param request http请求
     * @param title 标题
     * @param details 内容
     * @return 自定义响应体
     */
    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    public Object postQuestion(HttpServletRequest request, @RequestParam("title") String title, @RequestParam("details") String details){
        return questionService.postQuestion(request, title, details);
    }

    /**
     * 获得最近的问题
     * @param request http请求
     * @param page 页数
     * @param count 每页的条数
     * @return 自定义响应体
     */
    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public Object getRecentQuestions(HttpServletRequest request, @RequestParam("page") Integer page, @RequestParam("count") Integer count){
        return questionService.getRecentQuestionByUserId(request, page, count);
    }

    /**
     * 修改问题
     * @param request http请求
     * @param questionId 问题ID
     * @param title 标题
     * @param details 详情
     * @return 自定义响应体
     */
    @RequestMapping(value = "/questions", method = RequestMethod.PUT)
    public Object updateQuestion(HttpServletRequest request, @RequestParam("questionId") Integer questionId, @RequestParam("title") String title, @RequestParam("details") String details){
        return questionService.updateQuestion(request, questionId, title, details);
    }

    /**
     * 终结问题
     * @param request http请求
     * @param questionId 问题ID
     * @return 响应体
     */
    @RequestMapping(value = "/questions/solved", method = RequestMethod.GET)
    public Object solvedQuestion(HttpServletRequest request, @RequestParam("questionId") Integer questionId){
        return questionService.solvedQuestion(request, questionId);
    }

    /**
     * 获得用户信息
     * @param request http请求
     * @return 自定义响应体
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public Object getUserInfo(HttpServletRequest request){
        return userInfoService.getUserInfo(request);
    }

    /**
     * 更新用户信息
     * @param request http请求
     * @param userName 用户名
     * @param userTele 电话
     * @param userIntro 用户自我介绍
     * @param userSite 用户地址
     * @param userIndustry 用户行业
     * @return 自定义响应体
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public Object updateUserInfo(HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("userTele") String userTele, @RequestParam("userIntro") String userIntro, @Param("userSite") String userSite, @Param("userIndustry") String userIndustry){
        return userInfoService.updateUserInfo(request, userName, userTele, userIntro, userSite, userIndustry);
    }

    /**
     * 上传用户头像
     * @param request http请求
     * @param file 文件
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadAvatar(HttpServletRequest request, MultipartFile file){
        return userInfoService.uploadAvatar(file, request);
    }

    /**
     * 获得我的发表问题
     * @param request http请求
     * @return 自定义响应体
     */
    @RequestMapping(value = "/questions/my")
    public Object getMyQuestions(HttpServletRequest request){
        return  questionService.getQuestionsByUserId(request);
    }

    /**
     * 获得我发表的回答
     * @param request http请求
     * @return 自定义响应体
     */
    @RequestMapping(value = "/answers/my")
    public Object getMyAnswers(HttpServletRequest request){
        return answerService.getMyAnswers(request);
    }

    /**
     * 发表回答
     * @param request http请求
     * @param questionId 问题ID
     * @param details 问题详情
     * @return 自定义响应体
     */
    @RequestMapping(value = "/answer")
    public Object postAnswer(HttpServletRequest request, @RequestParam("questionId") Integer questionId, @RequestParam("details") String details){
        return answerService.postAnswer(request, questionId, details);
    }

    /**
     * 删除回答
     * @param request http请求
     * @param answerId 回答ID
     * @return 自定义响应体
     */
    @RequestMapping("/answer/delete")
    public Object deleteAnswer(HttpServletRequest request, @RequestParam("answerId") Integer answerId){
        return answerService.deleteAnswer(request, answerId);
    }

    /**
     * 更新回答
     * @param request http请求
     * @param answerId 回答ID
     * @param details 回答详情
     * @return
     */
    @RequestMapping("/answer/update")
    public Object updateAnswer(HttpServletRequest request, @RequestParam("answerId") Integer answerId, @RequestParam("details") String details){
        return answerService.updateAnswer(request, details, answerId);
    }

}
