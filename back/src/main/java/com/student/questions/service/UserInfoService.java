package com.student.questions.service;

import com.student.questions.entity.CustomResponseBody;
import com.student.questions.entity.User;
import com.student.questions.entity.UserInfo;
import com.student.questions.mapper.UserInfoMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 更新用户信息
     * @param request http请求
     * @param userName 用户名
     * @param userTele 用户电话
     * @param userIntro 用户自我介绍
     * @param userSite 用户地址
     * @param userIndustry 用户行业
     * @return 自定义响应体
     */
    public Map updateUserInfo(HttpServletRequest request, String userName, String userTele, String userIntro, String userSite, String userIndustry){
        Object object = request.getAttribute("user");
        User user = null;
        // 将object实例化为user对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user!=null){
            // 调用Mapper更新数据
            Integer result = userInfoMapper.updateUserInfo(user.getUserId(), userName, userTele, userIntro, userSite, userIndustry);
            if(result == 1){
                return new CustomResponseBody(200, "修改成功！").getMap();
            }
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 获得用户信息
     * @param request http请求
     * @return 自定义响应体
     */
    public Map getUserInfo(HttpServletRequest request){
        Object object = request.getAttribute("user");
        User user = null;
        // 将object实例化为user对象
        if(object instanceof User){
            user = (User)object;
        }
        if(user!=null){
            // 调用Mapper查询数据
            UserInfo userInfo = userInfoMapper.selectUserInfoById(user.getUserId());
            if(userInfo != null){
                CustomResponseBody responseBody = new CustomResponseBody(200, "请求成功！");
                responseBody.addData(userInfo);
                return responseBody.getMap();
            }
            return new CustomResponseBody(404, "无用户信息！").getMap();
        }
        return new CustomResponseBody(500, "未知错误！").getMap();
    }

    /**
     * 上传用户头像
     * @param file 文件
     * @param request http请求
     * @return 自定义响应体
     */
    public Map uploadAvatar(MultipartFile file, HttpServletRequest request){
        try {
            Object object = request.getAttribute("user");
            User user = null;
            // 将object实例化为user对象
            if(object instanceof User){
                user = (User)object;
            }
            if(user != null){
                // 获得原始用户名
                String fileName = file.getOriginalFilename();
                // 获得文件名后缀
                String[] tempArray = fileName.split("\\.");
                String fileType = tempArray[tempArray.length - 1];
                // 拼接新的文件名
                String newFileName = DigestUtils.sha1Hex(user.getUserName() + System.currentTimeMillis())  + "." + fileType;
                File savePath = new File("D://questions//avatar//" + newFileName);
                if(!savePath.getParentFile().exists()){
                    savePath.getParentFile().mkdirs();
                }
                // 保存文件
                file.transferTo(savePath);
                userInfoMapper.setNewAvatar("http://127.0.0.1:8080/public/images?fileName=" + newFileName, user.getUserId());
                // 创建自定义响应体
                CustomResponseBody responseBody =  new CustomResponseBody(200, "头像上传成功！");
                Map<String, Object> tempMap = new HashMap();
                // 返回文件路径
                tempMap.put("userAvatar", "http://127.0.0.1:8080/public/images?fileName=" + newFileName);
                responseBody.addData(tempMap);
                return responseBody.getMap();
            }
        } catch (IOException e){
            e.printStackTrace();
            return new CustomResponseBody(404, " 请联系管理员！").getMap();
        }
        return new CustomResponseBody(409, "未知错误！").getMap();
    }

    /**
     * 根据角色获得用户信息
     * @param roleName 角色名
     * @param page 页数
     * @param count 条数
     * @return 自定义响应体
     */
    public Map getUserInfoByRoleName(String roleName, Integer page, Integer count){
        Integer offset = page * count;
        // 通过Mapper查询数据
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoByRole(roleName, offset, count);
        Integer pageCount = userInfoMapper.selectUserInfoByRolePageCount(roleName);
        CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("users", userInfos);
        tempMap.put("pageCount", pageCount);
        responseBody.addData(tempMap);
        return responseBody.getMap();
    }

    /**
     * 根据用户名查询用户信息
     * @param roleName 角色名
     * @param page 页数
     * @param count 数量
     * @param userName 用户名
     * @return 自定义响应体
     */
    public Map getUserInfoByRoleNameWithUserName(String roleName, Integer page, Integer count, String userName){
        // 拼接用户名和通配符
        userName = "%" + userName + "%";
        Integer offset = page * count;
        // 调用Mapper查询数据
        List<UserInfo> userInfos = userInfoMapper.selectUserInfoByRoleWithUserName(roleName, offset, count, userName);
        Integer pageCount = userInfoMapper.selectUserInfoByRoleWithUserNamePageCount(roleName, userName);
        CustomResponseBody responseBody = new CustomResponseBody(200, "请求数据成功！");
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("users", userInfos);
        tempMap.put("pageCount", pageCount);
        responseBody.addData(tempMap);
        return responseBody.getMap();
    }
}
