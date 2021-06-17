package com.student.questions.controller;

import com.alibaba.fastjson.JSONObject;
import com.student.questions.service.AnswerService;
import com.student.questions.service.QuestionService;
import com.student.questions.service.UserInfoService;
import com.student.questions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8081", allowCredentials = "true")
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerService answerService;

    /**
     * 用户登录API
     * @param userName 用户名
     * @param password 密码
     * @return 自定义响应体
     */
    @RequestMapping("/login")
    public Object login(@RequestParam("userName") String userName, @RequestParam("password") String password){
        // 去空格
        userName = userName.replace(" ", "");
        return userService.login(userName, password);
    }

    /**
     * 用户注册API
     * @param userName 用户名
     * @param password 密码
     * @return 自定义响应体
     */
    @RequestMapping("/register")
    public Object register(@RequestParam("userName") String userName, @RequestParam("password") String password){
        // 去空格
        userName = userName.replace(" ", "");
        return userService.register(userName, password);
    }

    /**
     * 获得最近的问题
     * @param page 页数
     * @param count 每页的条数
     * @param key 关键词
     * @return 自定义响应体
     */
    @RequestMapping("/questions")
    public Object getRecentQuestions(@RequestParam("page") Integer page, @RequestParam("count") Integer count, @RequestParam("key") String key){
        return questionService.getRecentQuestion(page, count, key);
    }

    /**
     * 获取图片，需要要有路径D://questions//avatar//
     * @param fileName 文件名
     * @param request http请求
     * @param response http响应
     */
    @RequestMapping("/images")
    public void getImage(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response){
        try (FileInputStream file = new FileInputStream("D://questions//avatar//" + fileName)){
            int bytesSize = file.available();
            byte[] data = new byte[bytesSize];
            // 从文件中读取
            file.read(data);
            response.setContentType("image/*");
            OutputStream toClient = response.getOutputStream();
            // 写入响应体
            toClient.write(data);
            toClient.close();
        } catch (IOException e) {
        }
    }

    /**
     * 获得问题
     * @param questionId 问题ID
     * @return 自定义响应体
     */
    @RequestMapping("/question")
    public Object getQuestion(@RequestParam("id") Integer questionId){
        return questionService.getQuestionById(questionId);
    }

    /**
     * 获得回答
     * @param request http请求
     * @param questionId 问题ID
     * @return 自定义响应体
     */
    @RequestMapping("/answers")
    public Object getAnswers(HttpServletRequest request, @RequestParam("questionId") Integer questionId){
        return answerService.getAnswersByQuestionId(request, questionId);
    }

    /**
     * TODO: 测试功能，记得删除
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadAvatar(HttpServletRequest request, MultipartFile file){
        return userInfoService.uploadAvatar(file, request);
    }

    @RequestMapping("/test")
    public Object test(HttpServletRequest request, @RequestParam("test") String test){
        HttpSession session = request.getSession();
        JSONObject json = new JSONObject();
        json.put("code", test);
        json.put("createTime", System.currentTimeMillis());
        session.setAttribute("test", json);
        return "OK";
    }

    @RequestMapping("/get")
    public Object test(HttpServletRequest request){
        HttpSession session = request.getSession();
        JSONObject object = (JSONObject) session.getAttribute("test");
        System.out.println(object);
        return "index.html";
    }

}
