package com.student.questions.service;

import com.student.questions.entity.CustomResponseBody;
import com.student.questions.entity.Question;
import com.student.questions.entity.User;
import com.student.questions.entity.UserInfo;
import com.student.questions.mapper.QuestionMapper;
import com.student.questions.mapper.UserInfoMapper;
import com.student.questions.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    // 过滤HTML标签的正则表达式
    private final String regExHtml = "<[^>]+>|\\s*|\t|\r|\n";

    /**
     * 获得最近发布的问题
     * @param page 页数
     * @param count 数量
     * @param key 关键字
     * @return
     */
    public Map getRecentQuestion(Integer page, Integer count, String key){
        // 编译正则
        Pattern htmlPattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
        // 页数转化为偏移量
        Integer offset = page * count;
        List<Question> questions;
        Integer total;
        // 判断关键词是否存在
        if(key.equals("")){
            questions = questionMapper.selectQuestion(offset, count);
            total = questionMapper.selectQuestionCount();
        }
        else{
            // 在关键字左右加上通配符
            key = "%" + key + "%";
            questions = questionMapper.selectQuestionWithKey(offset, count, key);
            total = questionMapper.selectQuestionWithKeyCount(key);
        }
        // 创建自定义响应体
        CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
        List<Map> questionWithUserInfo = new ArrayList<>();
        // 绑定问题和问题发布者信息
        for(Question question : questions){
            Map tempMap = new HashMap();
            question.setQuestionDetails(htmlPattern.matcher(question.getQuestionDetails()).replaceAll(""));
            tempMap.put("question", question);
            UserInfo userInfo = userInfoMapper.selectUserInfoById(question.getUserId());
            tempMap.put("userInfo", userInfo);
            questionWithUserInfo.add(tempMap);
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("questions", questionWithUserInfo.toArray());
        tempMap.put("total", total);
        responseBody.addData(tempMap);
        return responseBody.getMap();
    }

    /**
     * 根据用户ID获得最近的问题
     * @param request http请求
     * @param page 页数
     * @param count 数量
     * @return 自定义响应体
     */
    public Map getRecentQuestionByUserId(HttpServletRequest request, Integer page, Integer count){
        Object object = request.getAttribute("user");
        User user = null;
        // 转化object为User对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user != null){
            // 编译正则
            Pattern htmlPattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            Integer offset = page * count;
            // 根据用户ID查询问题
            List<Question> questions = questionMapper.selectQuestionWithUserId(user.getUserId(), offset, count);
            // 查询总的问题数量
            Integer total = questionMapper.selectQuestionWithUserIdCount(user.getUserId());
            // 创建响应体
            CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
            List<Map> questionWithUserInfo = new ArrayList<>();
            // 绑定问题和发表问题的用户的信息
            for(Question question : questions){
                Map tempMap = new HashMap();
                question.setQuestionDetails(htmlPattern.matcher(question.getQuestionDetails()).replaceAll(""));
                tempMap.put("question", question);
                UserInfo userInfo = userInfoMapper.selectUserInfoById(question.getUserId());
                tempMap.put("userInfo", userInfo);
                questionWithUserInfo.add(tempMap);
            }
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("questions", questionWithUserInfo.toArray());
            tempMap.put("total", total);
            responseBody.addData(tempMap);
            return responseBody.getMap();
        }
        else{
            return getRecentQuestion(page, count, "");
        }
    }

    /**
     * 发表问题
     * @param request http请求
     * @param questionTitle 问题标题
     * @param questionDetails 问题详情
     * @return 自定义响应体
     */
    public Map postQuestion(HttpServletRequest request, String questionTitle, String questionDetails){
        Object object = request.getAttribute("user");
        User user = null;
        // 转化object为User对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user != null){
            // 调用Mapper查询数据
            Integer result = questionMapper.insertQuestion(user.getUserId(), questionTitle, questionDetails);
            if(result == 1){
                return new CustomResponseBody(200, "发表问题成功！").getMap();
            }
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 更新问题
     * @param request http请求
     * @param questionId 问题ID
     * @param questionTitle 问题标题
     * @param questionDetails 问题详情
     * @return 自定义响应体
     */
    public Map updateQuestion(HttpServletRequest request, Integer questionId, String questionTitle, String questionDetails){
        Object object = request.getAttribute("user");
        User user = null;
        // 转化object为User对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user!=null){
            // 调用Mapper查询信息
            Integer result = questionMapper.updateQuestion(questionId, user.getUserId(), questionTitle, questionDetails);
            if(result == 1){
                return new CustomResponseBody(200, "修改问题成功！").getMap();
            }
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 设置问题已解决
     * @param request http请求
     * @param questionId 问题ID
     * @return 自定义响应体
     */
    public Map solvedQuestion(HttpServletRequest request, Integer questionId){
        Object object = request.getAttribute("user");
        User user = null;
        // 转化object为User对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user!=null){
            // 调用Mapper更新数据
            Integer result = questionMapper.updateQuestionSolved(1, questionId, user.getUserId());
            if(result == 1){
                return new CustomResponseBody(200, "终结问题成功！").getMap();
            }
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 拉黑问题
     * @param questionId
     * @return
     */
    public Map blockedQuestion(Integer questionId) {
        // 调用Mapper更新拉黑状态
        Integer result = questionMapper.updateQuestionBlocked(1, questionId);
        if (result == 1) {
            return new CustomResponseBody(200, "屏蔽问题成功！").getMap();
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 取消拉黑问题
     * @param questionId 问题ID
     * @return 自定义响应体
     */
    public Map cancelBlockedQuestion(Integer questionId) {
        // 调用Mapper更新拉黑状态
        Integer result = questionMapper.updateQuestionBlocked(0, questionId);
        if (result == 1) {
            return new CustomResponseBody(200, "取消屏蔽问题成功！").getMap();
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 根据用户ID获得问题
     * @param request http请求
     * @return 自定义响应体
     */
    public Map getQuestionsByUserId(HttpServletRequest request){
        Object object = request.getAttribute("user");
        User user = null;
        // 转化object为User对象
        if(object instanceof User){
            user = (User)object;
        }
        if (user != null){
            // 编译正则
            Pattern htmlPattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            List<Question> questions = questionMapper.selectQuestionByUserId(user.getUserId());
            // 创建自定义响应体
            CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
            List<Map> questionWithUserInfo = new ArrayList<>();
            // 绑定问题和发布者信息
            for(Question question : questions){
                Map tempMap = new HashMap();
                question.setQuestionDetails(htmlPattern.matcher(question.getQuestionDetails()).replaceAll(""));
                tempMap.put("question", question);
                UserInfo userInfo = userInfoMapper.selectUserInfoById(question.getUserId());
                tempMap.put("userInfo", userInfo);
                questionWithUserInfo.add(tempMap);
            }
            responseBody.addData(questionWithUserInfo.toArray());
            return responseBody.getMap();
        }
        return new CustomResponseBody(500, "请求数据失败！").getMap();
    }

    /**
     * 根据问题ID获得问题详情
     * @param questionId
     * @return
     */
    public Map getQuestionById(Integer questionId){
        // 调用Mapper查询
        Question question = questionMapper.selectQuestionById(questionId);
        UserInfo userInfo = userInfoMapper.selectUserInfoById(question.getUserId());
        CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
        Map<String, Object> tempMap = new HashMap();
        tempMap.put("question", question);
        tempMap.put("userInfo", userInfo);
        responseBody.addData(tempMap);
        return responseBody.getMap();
    }

    /**
     * 获得拉黑的问题
     * @param page 页数
     * @param count 每页数量
     * @param key 关键词
     * @return 自定义响应体
     */
    public Map getBlockedQuestion(Integer page, Integer count, String key){
        // 编译正则
        Pattern htmlPattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
        // 转化页数为偏移
        Integer offset = page * count;
        List<Question> questions;
        Integer total = 0;
        // 根据是否传入关键词是否根据关键词查询
        if(key.equals("")){
            questions = questionMapper.selectBlockedQuestion(offset, count);
            total = questionMapper.selectBlockedQuestionCount();
        }
        else{
            key = "%" + key + "%";
            questions = questionMapper.selectBlockedQuestionWithKey(offset, count, key);
            total = questionMapper.selectBlockedQuestionWithKeyCount(key);
        }
        // 创建自定义响应体
        CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
        List<Map> questionWithUserInfo = new ArrayList<>();
        // 绑定问题和发布者信息
        for(Question question : questions){
            Map tempMap = new HashMap();
            question.setQuestionDetails(htmlPattern.matcher(question.getQuestionDetails()).replaceAll(""));
            tempMap.put("question", question);
            UserInfo userInfo = userInfoMapper.selectUserInfoById(question.getUserId());
            tempMap.put("userInfo", userInfo);
            questionWithUserInfo.add(tempMap);
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("questionAndUserInfo", questionWithUserInfo.toArray());
        tempMap.put("total", total);
        responseBody.addData(tempMap);
        return responseBody.getMap();
    }
}
