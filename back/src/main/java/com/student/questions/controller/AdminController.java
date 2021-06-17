package com.student.questions.controller;

import com.student.questions.entity.CustomResponseBody;
import com.student.questions.service.AnswerService;
import com.student.questions.service.QuestionService;
import com.student.questions.service.UserInfoService;
import com.student.questions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserService userService;

    @Autowired
    AnswerService answerService;

    /**
     * 用于测试用户是否admin且可正常访问功能
     * @return 自定义响应体
     */
    @RequestMapping(value = "/checkLogin")
    public Object checkLogin(){
        return new CustomResponseBody(200, "OK").getMap();
    }

    /**
     * 获得申请注册的用户列别
     * @param page 第几页，从0开始
     * @param count 每页的条数
     * @param userName 用户名
     * @return 自定义的响应体
     */
    @RequestMapping(value = "/user/applying")
    public Object getApplyingUser(@RequestParam("page") Integer page, @RequestParam("count") Integer count, @RequestParam("userName") String userName){
        // 根据传入的userName是否为空判断是否要根据姓名搜索
        if(userName.equals("")){
            return userInfoService.getUserInfoByRoleName("applying", page, count);
        }
        else{
            return userInfoService.getUserInfoByRoleNameWithUserName("applying", page, count, userName);
        }
    }

    /**
     * 获得已注册的用户列表
     * @param page 第几页，从0开始
     * @param count 每页条数
     * @param userName 用户名
     * @return 自定义响应体
     */
    @RequestMapping(value = "/user")
    public Object getUser(@RequestParam("page") Integer page, @RequestParam("count") Integer count, @RequestParam("userName") String userName){
        // 根据传入的用户名是否为空判断是否进行搜索
        if(userName.equals("")){
            return userInfoService.getUserInfoByRoleName("user", page, count);
        }
        else{
            return userInfoService.getUserInfoByRoleNameWithUserName("user", page, count, userName);
        }
    }

    /**
     * 获得被拉黑的用户
     * @param page 页数
     * @param count 每页的条数
     * @param userName 用户名
     * @return 自定义响应体
     */
    @RequestMapping(value = "/user/blocked")
    public Object getBlockedUser(@RequestParam("page") Integer page, @RequestParam("count") Integer count, @RequestParam("userName") String userName){
        // 根据传入的用户名是否为空判断是否进行搜索
        if(userName.equals("")){
            return userInfoService.getUserInfoByRoleName("blocked", page, count);
        }
        else{
            return userInfoService.getUserInfoByRoleNameWithUserName("blocked", page, count, userName);
        }
    }

    /**
     * 拒绝注册申请
     * @param userId 用户ID
     * @return 自定义响应体
     */
    @RequestMapping(value = "/user/applying/refuse")
    public Object refuseApplying(@RequestParam("userId") Integer userId){
        return userService.refuseApplying(userId);
    }

    /**
     * 通过用户注册申请
     * @param userId 用户ID
     * @return
     */
    @RequestMapping(value = "/user/applying/pass")
    public Object passApplying(@RequestParam("userId") Integer userId){
        return userService.passApplying(userId);
    }

    /**
     * 拉黑用户
     * @param userId 用户ID
     * @return 自定义相应体
     */
    @RequestMapping(value = "/block/user")
    public Object blockUser(@RequestParam("userId") Integer userId){
        return userService.blockUser(userId);
    }

    /**
     * 拉黑问题
     * @param questionId 问题ID
     * @return 自定义响应体
     */
    @RequestMapping(value = "/block/questions")
        public Object blockedQuestion(@RequestParam("questionId") Integer questionId){
        return questionService.blockedQuestion(questionId);
    }

    /**
     * 取消拉黑
     * @param userId 用户ID
     * @return 自定响应体
     */
    @RequestMapping(value = "/cancelBlock/user")
    public Object cancelBlockUser(@RequestParam("userId") Integer userId){
        return userService.cancelBlock(userId);
    }

    /**
     * 取消拉黑问题
     * @param questionId 问题ID
     * @return 自定义响应体
     */
    @RequestMapping(value = "/cancelBlock/questions")
    public Object cancelBlockQuestion(@RequestParam("questionId") Integer questionId){
        return questionService.cancelBlockedQuestion(questionId);
    }

    /**
     * 获得拉黑的问题列表
     * @param page 页数
     * @param count 每页的条数
     * @param key 关键词
     * @return 自定义响应体
     */
    @RequestMapping(value = "/question/blocked")
    public Object getBlockedQuestion(@RequestParam("page") Integer page, @RequestParam("count") Integer count, @RequestParam("searchKey") String key){
        return questionService.getBlockedQuestion(page, count, key);
    }

    /**
     * 获得回答列表
     * @param page 页数
     * @param count 每页条数
     * @param key 关键词
     * @param blocked 是否被拉黑
     * @return 自定义响应体
     */
    @RequestMapping(value = "/answers")
    public Object getAnswer(@RequestParam("page") Integer page, @RequestParam("count") Integer count, @RequestParam("key") String key, @RequestParam("blocked") Integer blocked){
        return answerService.getAnswer(page, count, key, blocked);
    }

    /**
     * 拉黑问题
     * @param answerId 回答ID
     * @return 自定义响应体
     */
    @RequestMapping(value = "/block/answer")
    public Object blockedAnswer(@RequestParam("answerId") Integer answerId){
        return answerService.blockedAnswer(answerId);
    }

    /**
     * 取消回答拉黑
     * @param answerId 回答ID
     * @return 自定义响应体
     */
    @RequestMapping(value = "/cancelBlock/answer")
    public Object cancelBlockAnswer(@RequestParam("answerId") Integer answerId){
        return answerService.cancelBlockedAnswer(answerId);
    }
}
