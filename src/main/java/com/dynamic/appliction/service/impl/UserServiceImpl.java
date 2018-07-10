package com.dynamic.appliction.service.impl;

import com.dynamic.appliction.dao.LoginsMapper;
import com.dynamic.appliction.pojo.bean.Logins;
import com.dynamic.appliction.pojo.bean.MailBox;
import com.dynamic.appliction.pojo.bean.Share;
import com.dynamic.appliction.pojo.bean.User;
import com.dynamic.appliction.dao.MailBoxMapper;
import com.dynamic.appliction.dao.ShareMapper;
import com.dynamic.appliction.dao.UserMapper;
import com.dynamic.appliction.util.*;
import com.dynamic.appliction.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: demo
 * @description:
 * @author: Mr.MO
 * @create: 2018-06-21 13:53
 **/
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UserMapper userMapper;
    @Resource
    MailBoxMapper mailboxInfo;
    @Resource
    ShareMapper shareMapper;
    @Resource
    LoginsMapper loginsMapper;
    @Autowired
    VerificationMail verificationMail;
    @Autowired
    PasswdMd5 passwdMd5;
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Autowired
    RedisUtils redisUtils;


    @Override
    public boolean register(User user) {
        boolean falg = false;
        try {
            user.setState("-1");
            user.setCreationTime(TimeUtil.getDate());
            user.setSharingToken(RC4.getSharingcode());
            user.setConfirmationToken(RC4.getSharingcode());
            if (user.getPassword() != null) {
                String password = passwdMd5.EncoderByMd5(user.getPassword());
                user.setPassword(password);
                if (userMapper.insertSelective(user) > 0) {
                    this.sendConfirmation(user.getEmail(), user.getConfirmationToken(), 0);
                    falg = true;
                }
            } else if (user.getPassword() == null) {
                if (userMapper.insertSelective(user) > 0) {
                    this.sendConfirmation(user.getEmail(), user.getSharingToken(), 2);
                    falg = true;
                }
            }
            String sharingToken = CookiesUtil.getCookie(request, "referral");
            if (sharingToken != null) {
                User referralUser = userMapper.referral(sharingToken);
                Share share = new Share();
                share.setReferralid(referralUser.getId());
                share.setBeintroducedid(user.getId());
                share.setCreationTime(TimeUtil.getDate());
                shareMapper.insertSelective(share);
            }

        } catch (Exception e) {
            logger.error("注册错误：{}", e.getMessage());
            return false;
        }

        return falg;
    }

    @Override
    public Map<String, Object> queryEmail(String email) {
        Map<String, Object> returnMap = new HashMap<>();
        User user = userMapper.queryEmail(email);
        if (user != null) {
            returnMap.put("msg", "该用户已注册");
            if (user.getState().equals("-1")) {
                returnMap.put("msg", "请进行邮箱验证");
            }
            return returnMap;
        } else {
            returnMap.put("msg", "该用户未注册");
            return returnMap;
        }

    }

    @Override
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            User user = userMapper.login(email, passwdMd5.EncoderByMd5(password));
            if (user == null) {
                returnMap.put("error", "密码有误");
            } else if (user.getState().equals("-1")) {
                returnMap.put("error", "请进行邮箱验证");
            } else if (user.getState().equals("0")) {
                String ipaddress = LoginInformation.getIpAddress(request);
                //获取浏览器信息
                // 转成UserAgent对象
                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                //获取浏览器信息
                Browser browser = userAgent.getBrowser();
                //获取系统信息
                OperatingSystem os = userAgent.getOperatingSystem();
                //系统名称
                String system = os.getName();
                //浏览器名称
                String browserName = browser.getName();
                Logins logins = new Logins();
                logins.setIpaddress(ipaddress);
                logins.setBrowser(browser.getName());
                logins.setOs(os.getName());
                logins.setUserId(user.getId());
                logins.setCreationTime(TimeUtil.getDate());
                if (loginsMapper.insertSelective(logins) > 0) {
                    returnMap.put("msg", user);
                    String sessionID = passwdMd5.EncoderByMd5(RC4.getSharingcode());
                    CookiesUtil.setCookie(response, "_session_id", sessionID, 60 * 60 * 24 * 7);
                    long time = 60 * 24 * 7;
                    redisUtils.set(sessionID, email, time);
                }
            }
        } catch (Exception e) {
            logger.error("error:{}", e.getMessage());
            returnMap.put("error", e.getMessage());
        }

        return returnMap;
    }

    @Override
    public boolean modifyUser(User user) {
        boolean falg = false;
        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            falg = true;
        }
        return falg;
    }

    @Override
    public boolean sendEmail(String email, int type) {
        boolean falg = false;
        User user = userMapper.queryEmail(email);
        try {
            String resetPasswordToken = "";
            if (type == 1) {
                resetPasswordToken = RC4.getSharingcode();
                long time = 600;
                redisUtils.set(resetPasswordToken, email, time);
            } else if (type == 0) {
                resetPasswordToken = user.getConfirmationToken();
            }
            this.sendConfirmation(email, resetPasswordToken, type);
            falg = true;
        } catch (Exception e) {
            logger.error("密码修改验证邮箱：失败原因：{}", e.getMessage());
        }

        return falg;
    }

    @Override
    public Map<String, Object> queryUserList(int pageSize, int pageNumber, String country, String usdRange) {
        PageHelper.startPage(pageNumber, pageSize);
        List<User> userList = userMapper.userList(country, usdRange);
        PageInfo<User> userPage = new PageInfo<User>(userList);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("rows", userList);
        returnMap.put("total", userPage.getTotal());
        return returnMap;
    }

    @Override
    public boolean groupMail(List<String> mails, MailBox mailBox) {
        boolean falg = false;
        mailBox.setSystemid(1);
        mailBox.setCreationtime(TimeUtil.getDate());
        mailboxInfo.insertSelective(mailBox);
        if (null == mails || mails.size() == 0) {
            User user = new User();
            user.setCountry(mailBox.getCountry());
            user.setUsdRange(mailBox.getUsdRange());
            mails = userMapper.userConditionList(user);
            verificationMail.groupMail(mails, mailBox);
            falg = true;
        } else {
            verificationMail.groupMail(mails, mailBox);
            falg = true;
        }
        return falg;
    }

    @Override
    public boolean confirmation(String confirmation_token) {
        User user = userMapper.confirmation(confirmation_token);
        if (user != null && !user.getState().equals("0")) {
            user.setState("0");
            if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean verificationToken(String resetPasswordToken, ModelMap map) {
        try {
            String email = redisUtils.get(resetPasswordToken);
            if (email != null) {
                redisUtils.removePattern(resetPasswordToken);
                resetPasswordToken = RC4.getSharingcode();
                long time = 180;
                redisUtils.set(resetPasswordToken, email, time);
                map.put("resetPasswordToken", resetPasswordToken);
                return true;
            }
        } catch (Exception e) {
            logger.error("修改密码错误，验证码失效", e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public boolean modifyPassword(String password, String resetPasswordToken) {
        String email = redisUtils.get(resetPasswordToken);
        try {
            if (email != null) {
                redisUtils.removePattern(resetPasswordToken);
                long time = 180;
                password = passwdMd5.EncoderByMd5(password);
                int count = userMapper.modifyPassword(email, password);
                if (count > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendConfirmation(String email, String token, int type) {
        if (type == 0) {
            String content = "http://localhost/confirmation?confirmation_token=" + token;
            MailBox mailBox = new MailBox();
            mailBox.setTitle("激活您的账户");
            mailBox.setContent(content);
            new Thread(new MailUtil(email, mailBox)).start();
        } else if (type == 1) {
            String content = "http://localhost/password-reset?reset_password_token=" + token;
            MailBox mailBox = new MailBox();
            mailBox.setTitle("修改密码");
            mailBox.setContent(content);
            new Thread(new MailUtil(email, mailBox)).start();
        } else if (type == 2) {
            String content = "http://localhost/?referral=" + token + "";
            MailBox mailBox = new MailBox();
            mailBox.setTitle("推荐链接");
            mailBox.setContent(content);
            new Thread(new MailUtil(email, mailBox)).start();
        }
    }
}
