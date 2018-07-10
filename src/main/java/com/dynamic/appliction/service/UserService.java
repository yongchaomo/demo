package com.dynamic.appliction.service;

import java.util.List;
import java.util.Map;

import com.dynamic.appliction.pojo.bean.MailBox;
import com.dynamic.appliction.pojo.bean.User;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: demo
 * @description:User类
 * @author: Mr.MO
 * @create: 2018-06-21 13:50
 **/
public interface UserService {
    // 注册
    boolean register(User user);

    // 查询邮箱是否被注册
    Map<String, Object> queryEmail(String email);

    // 登录
    Map<String, Object> login(String email, String password);

    // 修改用户信息
    boolean modifyUser(User user);

    // 发送邮箱验证码
    boolean sendEmail(String email, int type);

    // 分页查询用户信息
    Map<String, Object> queryUserList(int pageSize, int pageNumber, String country, String usdRange);

    // 批量发送邮箱
    boolean groupMail(List<String> mails, MailBox mailBox);

    // 邮箱链接验证注册
    boolean confirmation(String confirmation_token);

    //验证修改密码注册码
    boolean verificationToken(String resetPasswordToken, ModelMap map);

    //修改密码
    boolean modifyPassword(String password, String resetPasswordToken);
}
