package com.dynamic.appliction.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dynamic.appliction.pojo.bean.Resp;
import com.dynamic.appliction.pojo.bean.User;
import com.dynamic.appliction.service.UserService;
import com.dynamic.appliction.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: demo
 * @description: 用户信息处理
 * @author: Mr.MO
 * @create: 2018-06-21 14:12
 **/
// @Slf4j

@Controller
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    /**
     * @Description: 注册
     * @Param: User user
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @ResponseBody
    @PostMapping("/register")
    public Resp register(@RequestParam("user") String user) {
        User user1 = JSONArray.toJavaObject((JSONObject) JSONArray.parse(user), User.class);
        logger.info("注册信息:{}", JsonUtil.toJSONString(user));
        return Resp.httpStatus(HttpStatus.ACCEPTED, "注册状态", userService.register(user1));
    }

    /**
     * @Description: 确认验证邮箱
     * @Param: User user
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @GetMapping("/confirmation")
    public String confirmation(@RequestParam String confirmation_token) {
        logger.info("确认验证邮箱信息-confirmation_token:{}", confirmation_token);
        if (userService.confirmation(confirmation_token)) {
            return "/index";
        }
        return "";
    }

    /**
     * @Description: 登录
     * @Param: String email, String password
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @ResponseBody
    @PostMapping("/login")
    public Resp login(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
        logger.info("用户登录信息：邮件：{},,密码：{}", email, password);
        return Resp.httpStatus(HttpStatus.ACCEPTED, "login", userService.login(email, password));
    }

    /**
     * @Description: 修改密码
     * @Param: User类
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @ResponseBody
    @GetMapping("/modifyPassword")
    public Resp modifyPassword(@RequestParam(value = "password") String password, @RequestParam(value = "reset_password_token") String resetPasswordToken) {
        logger.info("修改密码-新密码:{}", password);
        if (userService.modifyPassword(password, resetPasswordToken)) {
            return Resp.httpStatus(HttpStatus.ACCEPTED, "修改密码状态", true);
        }
        return Resp.httpStatus(HttpStatus.ACCEPTED, "修改密码状态", false);
    }

    /**
     * @Description: 修改用户信息
     * @Param: User user
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @ResponseBody
    @PostMapping("/modifyUser")
    public Map<String, Object> modifyUser(@RequestParam String user) {
        User user1 = (User) JsonUtil.parse(user);
        Map<String, Object> returnMap = new HashMap<>();
        boolean falg = false;
        try {
            userService.modifyUser(user1);
            falg = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        returnMap.put("result", falg);
        return returnMap;
    }


    /**
     * @Description: 发送邮箱验证码
     * @Param:
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @ResponseBody
    @GetMapping("/sendEmail")
    public Resp sendEmail(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "type", required = false) int type) {
        if (type == 0) {
            logger.info("发送邮箱验证码-邮箱账号:{}", email);
            return Resp.httpStatus(HttpStatus.ACCEPTED, "发送邮箱验证码状态", userService.sendEmail(email, type));
        } else if (type == 1) {
            logger.info("密码重置初始化-邮箱账号:{}", email);
            return Resp.httpStatus(HttpStatus.ACCEPTED, "密码重置初始化状态", userService.sendEmail(email, type));
        } else if (type == 3) {
            logger.info("用户验证信息：邮件：{}", email);
            return Resp.httpStatus(HttpStatus.ACCEPTED, "用户存在状态", userService.queryEmail(email));
        }
        return null;
    }

}
