package com.student.questions.config;

import ch.qos.logback.core.util.ContentTypeUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.student.questions.entity.CustomResponseBody;
import com.student.questions.entity.User;
import com.student.questions.mapper.UserMapper;
import com.student.questions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 权限认证拦截器，根据构造方式传入的需要的角色requireRole，以及用户访问所带的token，判断用户是否拥有对应的权限
 */
class AuthInterceptor implements HandlerInterceptor {

    private String requireRole = "";

    public AuthInterceptor(String requireRole){
        this.requireRole = requireRole;
    }

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    /**
     * 验证是否拥有所需要的权限
     * @param request 请求
     * @param response 响应
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = request.getSession().getId();
        response.setHeader("id", sessionId);
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8081");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "token");
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        // 检查是否需要User权限
        response.setContentType("application/json; charset=utf-8");
        // 执行认证
        if (token == null) {
            // 未登录
            PrintWriter writer = response.getWriter();
            CustomResponseBody responseBody = new CustomResponseBody(403, "未登录！");
            writer.write(JSONObject.toJSONString(responseBody.getMap()));
            writer.close();
            return false;
        }
        Integer userId = null;
        try {
            userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));
        } catch (JWTDecodeException j) {
            // 解码token出错
            PrintWriter writer = response.getWriter();
            CustomResponseBody responseBody = new CustomResponseBody(500, "服务端错误！");
            writer.write(JSONObject.toJSONString(responseBody.getMap()));
            writer.close();
            return false;
        }
        // 先查找用户
        User user = userMapper.selectUserById(userId);
        if (user == null) {
            // 用户不存在
            PrintWriter writer = response.getWriter();
            CustomResponseBody responseBody = new CustomResponseBody(404, "用户不存在！");
            writer.write(JSONObject.toJSONString(responseBody.getMap()));
            writer.close();
            return false;
        }
        // 用户存在 利用密码验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUserPassword())).build();
        // 返回新token
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            Boolean role = decodedJWT.getClaim(requireRole).asBoolean();
            role = role || decodedJWT.getClaim("isAdmin").asBoolean();
            if(role){
                request.setAttribute("user", user);
                String newToken = userService.getToken(user);
                response.setHeader("token", token);
                return true;
            }
            else{
                // 非User也非Admin，表示非法用户，不返回新token
                CustomResponseBody responseBody = new CustomResponseBody(403, "权限不足！");
                PrintWriter writer = response.getWriter();
                writer.write(JSONObject.toJSONString(responseBody.getMap()));
                writer.close();
                return false;
            }
        } catch (TokenExpiredException e){
            // token过期
            PrintWriter writer = response.getWriter();
            CustomResponseBody responseBody = new CustomResponseBody(302, "登录信息过期！");
            writer.write(JSONObject.toJSONString(responseBody.getMap()));
            writer.close();
            return false;
        }
        catch (JWTVerificationException e) {
            // 解码出错
            PrintWriter writer = response.getWriter();
            CustomResponseBody responseBody = new CustomResponseBody(403, "登录信息错误！");
            writer.write(JSONObject.toJSONString(responseBody.getMap()));
            writer.close();
            return false;
        }
    }
}
