package com.student.questions.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.student.questions.entity.*;
import com.student.questions.mapper.AnswerMapper;
import com.student.questions.mapper.UserInfoMapper;
import com.student.questions.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AnswerService {

    // 使用正则表达式过滤HTML标签s
    private final String regExHtml = "<[^>]+>|\\s*|\t|\r|\n";

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserMapper userMapper;

    /**
     * 获得我的回答
     * @param request http请求
     * @return 自定义响应体
     */
    public Map getMyAnswers(HttpServletRequest request){
        Object object = request.getAttribute("user");
        User user = null;
        // 转化object为user对象
        if(object instanceof User){
            user = (User)object;
        }
        if (user != null){
            // 编译正则表达式
            Pattern htmlPattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            // 调用Mapper查询数据库
            List<Answer> answers = answerMapper.selectAnswerByUserId(user.getUserId());
            // 创建响应体
            CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
            // 绑定回答以及用户信息
            List<Map> answerWithUserInfo = new ArrayList<>();
            for(Answer answer : answers){
                Map tempMap = new HashMap();
                answer.setAnswerDetails(htmlPattern.matcher(answer.getAnswerDetails()).replaceAll(""));
                tempMap.put("answer", answer);
                UserInfo userInfo = userInfoMapper.selectUserInfoById(answer.getUserId());
                tempMap.put("userInfo", userInfo);
                answerWithUserInfo.add(tempMap);
            }
            responseBody.addData(answerWithUserInfo.toArray());
            return responseBody.getMap();
        }
        return new CustomResponseBody(500, "请求数据失败！").getMap();
    }

    /**
     * 根据问题ID获得回答
     * @param request http请求
     * @param questionId 问题ID
     * @return 自定义响应体
     */
    public Map getAnswersByQuestionId(HttpServletRequest request, Integer questionId){
        Integer id;
        try{
            String token = request.getHeader("token");// 从 http 请求头中取出 token
            Integer userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));
            User user = userMapper.selectUserById(userId);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUserPassword())).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            // 验证并解码的到用户ID
            id = userId;
        }
        catch (Exception e){
            id = 0;
        }
        // 调用Mapper查询数据库
        List<Answer> answers = answerMapper.selectAnswerByQuestionId(id, questionId);
        // 创建响应体
        CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
        List<Map> answersWithUserInfo = new ArrayList<>();
        // 绑定回答与用户信息
        for(Answer answer : answers){
            Map tempMap = new HashMap();
            tempMap.put("answer", answer);
            UserInfo userInfo = userInfoMapper.selectUserInfoById(answer.getUserId());
            tempMap.put("userInfo", userInfo);
            answersWithUserInfo.add(tempMap);

        }
        responseBody.addData(answersWithUserInfo.toArray());
        return responseBody.getMap();
    }

    /**
     * 发布回答
     * @param request http请求
     * @param questionId 问题ID
     * @param answerDetails 问题详情
     * @return 自定义响应体
     */
    public Map postAnswer(HttpServletRequest request, Integer questionId, String answerDetails){
        Object object = request.getAttribute("user");
        User user = null;
        // 获得用户对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user != null){
            // 调用Mapper插入数据
            Integer result;
            try{
                 result = answerMapper.insertAnswer(questionId, user.getUserId(), answerDetails);
            } catch (Exception e){
                return new CustomResponseBody(403, "问题已终结！").getMap();
            }
            if(result == 1){
                return new CustomResponseBody(200, "发表回答成功！").getMap();
            }
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 删除回答
     * @param request http请求
     * @param answerId 回答ID
     * @return 自定义响应体
     */
    public Map deleteAnswer(HttpServletRequest request, Integer answerId){
        Object object = request.getAttribute("user");
        User user = null;
        // 获得用户对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user != null){
            // 调用Mapper删除数据
            Integer result = answerMapper.deleteAnswerByAnswerId(user.getUserId(), answerId);
            if(result == 1){
                return new CustomResponseBody(200, "删除回答成功！").getMap();
            }
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 更新回答
     * @param request http请求
     * @param details 问题详情
     * @param answerId 回答ID
     * @return 自定义响应体
     */
    public Map updateAnswer(HttpServletRequest request, String details, Integer answerId){
        Object object = request.getAttribute("user");
        User user = null;
        // 获得用户对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user != null){
            // 调用Mapper查询数据
            Integer result = answerMapper.updateAnswer(details, user.getUserId(), answerId);
            if(result == 1){
                return new CustomResponseBody(200, "更新回答成功！").getMap();
            }
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 根据关键词获得回答
     * @param page 页数
     * @param count 数量
     * @param key 关键词
     * @param blocked 是否被拉黑
     * @return
     */
    public Map getAnswer(Integer page, Integer count, String key, Integer blocked){
        Pattern htmlPattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
        // 页数转换为偏移
        Integer offset = page * count;
        List<Answer> answers;
        Integer total;
        // 判断是否有传入关键词
        if(key.equals("")){
            answers = answerMapper.selectAnswer(offset, count, blocked);
            total = answerMapper.selectAnswerCount(blocked);
        }
        else{
            // 在关键词两边加上通配符
            key = "%" + key + "%";
            answers = answerMapper.selectAnswerWithKey(offset, count, key, blocked);
            total = answerMapper.selectAnswerWithKeyCount(key, blocked);
        }
        // 创建自定义响应体
        CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
        List<Map> answerWithUserInfo = new ArrayList<>();
        // 绑定问题和发布者信息
        for(Answer answer : answers){
            Map tempMap = new HashMap();
            answer.setAnswerDetails(htmlPattern.matcher(answer.getAnswerDetails()).replaceAll(""));
            tempMap.put("answer", answer);
            UserInfo userInfo = userInfoMapper.selectUserInfoById(answer.getUserId());
            tempMap.put("userInfo", userInfo);
            answerWithUserInfo.add(tempMap);
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("answers", answerWithUserInfo.toArray());
        tempMap.put("total", total);
        responseBody.addData(tempMap);
        return responseBody.getMap();
    }

    /**
     * 拉黑回答
     * @param answerId 回答ID
     * @return 自定义响应体
     */
    public Map blockedAnswer(Integer answerId) {
        // 调用Mapper更新数据
        Integer result = answerMapper.updateAnswerBlock(1, answerId);
        if (result == 1) {
            return new CustomResponseBody(200, "屏蔽回答成功！").getMap();
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 取消拉黑问题
     * @param answerId 问题ID
     * @return 自定义响应体
     */
    public Map cancelBlockedAnswer(Integer answerId) {
        // 调用Mapper更新数据
        Integer result = answerMapper.updateAnswerBlock(0, answerId);
        if (result == 1) {
            return new CustomResponseBody(200, "取消屏蔽回答成功！").getMap();
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }
}
