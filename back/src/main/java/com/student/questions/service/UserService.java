package com.student.questions.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.student.questions.entity.CustomResponseBody;
import com.student.questions.entity.Role;
import com.student.questions.entity.User;
import com.student.questions.mapper.UserMapper;
import com.student.questions.mapper.UserRolesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRolesMapper userRolesMapper;

    /**
     * 调用Mapper查询用户，存在则登陆成功，否则进行判断
     * @param userName 用户名
     * @param password 密码
     * @return 响应体
     */
    public Map login(String userName, String password){
        // MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        try{
            User user = userMapper.selectUser(userName, password);
            // 查无此用户
            if(user == null){
                return new CustomResponseBody(401, "登录失败！").getMap();
            }
            // 查到
            else{
                CustomResponseBody responseBody = new CustomResponseBody(200, "登录成功！");
                List<Role> roles= userRolesMapper.selectByUserName(userName);
                String token = getToken(user);
                Map<String, Object> tempMap = new HashMap();
                tempMap.put("token", token);
                tempMap.put("userId", user.getUserId());
                responseBody.addData(tempMap);
                return responseBody.getMap();
            }
        } catch (Exception e){
            // 其他错误
            e.printStackTrace();
            return new CustomResponseBody(500, "未知错误").getMap();
        }
    }

    /**
     * 调用Mapper插入用户，返回1说明插入成功，否则为未知错误，或用户名存在
     * @param userName 用户名
     * @param password 密码
     * @return 响应体
     */
    public Map register(String userName, String password){
        // MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        try{
            Integer result =  userMapper.insertUser(userName, password);
            // 插入成功
            if(result == 1){
                CustomResponseBody responseBody = new CustomResponseBody(200, "注册成功！");
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("userName", userName);
                responseBody.addData(tempMap);
                return responseBody.getMap();
            }
            // 未知错误
            else{
                return new CustomResponseBody(500, "未知错误！").getMap();
            }
        } catch (DuplicateKeyException e) {
            // 用户名重复
            return new CustomResponseBody(409, "用户名已存在！").getMap();
        }
    }

    /**
     * 获得token
     * @param user 用户对象
     * @return 自定义响应体
     */
    public String getToken(User user){
        List<Role> roles= userRolesMapper.selectByUserName(user.getUserName());
        // 设置过期时间
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE,60);
        Date expiresDate = nowTime.getTime();
        String token="";
        // 设置用户角色
        Boolean isUser = false;
        Boolean isAdmin = false;
        Boolean isApplying = false;
        for(Role role : roles){
            if(role.getRoleName().equals("admin")){
                isAdmin = true;
            }
            if(role.getRoleName().equals("user")){
                isUser = true;
            }
            if(role.getRoleName().equals("applying")){
                isApplying = true;
            }
        }
        // 生成token
        token= JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate)
                .withAudience(String.valueOf(user.getUserId()))
                .withClaim("isUser", isUser)
                .withClaim("isAdmin", isAdmin)
                .withClaim("isApplying", isApplying)
                .sign(Algorithm.HMAC256(user.getUserPassword()));
        return token;
    }

    /**
     * 拒绝用户注册申请
     * @param userId 用户ID
     * @return 自定义响应体
     */
    public Map refuseApplying(Integer userId){
        try{
            // 调用Mapper更新数据
            Integer result = userRolesMapper.updateUserRole(userId, 4);
            if(result == 1){
                CustomResponseBody responseBody = new CustomResponseBody(200, "拒绝注册申请成功！");
                return responseBody.getMap();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new CustomResponseBody(500, "未知错误").getMap();
    }

    /**
     * 通过用户注册申请
     * @param userId 用户ID
     * @return 自定义响应体
     */
    public Map passApplying(Integer userId){
        try{
            // 调用Mapper更新数据
            Integer result = userRolesMapper.updateUserRole(userId, 2);
            if(result == 1){
                CustomResponseBody responseBody = new CustomResponseBody(200, "通过注册申请成功！");
                return responseBody.getMap();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new CustomResponseBody(500, "未知错误").getMap();
    }

    /**
     * 拉黑用户
     * @param userId 用户ID
     * @return 自定义响应体
     */
    public Map blockUser(Integer userId){
        try{
            // 调用Mapper更新数据
            Integer result = userRolesMapper.updateUserRole(userId, 5);
            if(result == 1){
                CustomResponseBody responseBody = new CustomResponseBody(200, "拉黑用户成功！");
                return responseBody.getMap();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new CustomResponseBody(500, "未知错误").getMap();
    }

    /**
     * 取消拉黑用户
     * @param userId 用户ID
     * @return 自定义响应体
     */
    public Map cancelBlock(Integer userId){
        try{
            // 调用Mapper更新数据
            Integer result = userRolesMapper.updateUserRole(userId, 2);
            if(result == 1){
                CustomResponseBody responseBody = new CustomResponseBody(200, "取消拉黑成功！");
                return responseBody.getMap();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new CustomResponseBody(500, "未知错误").getMap();
    }
}