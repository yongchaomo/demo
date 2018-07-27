package com.dynamic.appliction.controller.route;

import com.dynamic.appliction.pojo.bean.User;
import com.dynamic.appliction.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserRoute {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    /**
     * @Description: 跳转界面
     * @Param: User类
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @GetMapping("/password-reset-init")
    public String passwordResetInit() {
        logger.info("密码重置初始化-跳转界面");
        return "getpass";
    }

    /**
     * @Description: 验证修改密码时的Tokenn是否正确
     * @Param: User类
     * @return: Json（Boolean）
     * @Author: Mr.Mo
     * @Date: 2018/6/22
     */
    @GetMapping("/password-reset")
    public String edit(@RequestParam(value = "reset_password_token") String resetPasswordToken, ModelMap map) {
        logger.info("修改密码-验证码:{}", resetPasswordToken);
        if (userService.verificationToken(resetPasswordToken, map)) {
            return "setpass";
        }
        return "getpass";
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
            return "activation";
        }
        return "";
    }

}
