package com.dynamic.appliction.service.impl;

import com.dynamic.appliction.dao.LoginsMapper;
import com.dynamic.appliction.pojo.bean.*;
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
import org.dom4j.Attribute;
import org.dom4j.Element;
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
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    LoginInformation loginInformation;


    /*
    @Override
    public boolean register(User user) {
        boolean falg = false;
        try {
            user.setState("-1");
            user.setCreationTime(TimeUtil.getDate());
            user.setSharingToken(RC4.getSharingcode());
            user.setConfirmationToken(RC4.getSharingcode());
            if (user.getPassword() != null) {
                String password = PasswdMd5.EncoderByMd5(user.getPassword());
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
            String sharingToken = null;
            try {
                sharingToken = CookiesUtil.getCookie(request, "referral");
            } catch (Exception e) {
                logger.error("注册码为空：{}", e.getMessage());
            }
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
    */

    @Override
    public Map<String, Object> register(User user, Auths auths) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            if (!user.getEmail().equals("")) {
                if (user.getPassword().equals("") || auths == null) {

                } else {
                    String password = PasswdMd5.EncoderByMd5(user.getPassword());
                    user.setPassword(password);
                    String email = redisUtils.get(user.getCode());
                    if (user.getEmail().equals(email)) {
                        user.setState("0");
                        user.setSharingToken(RC4.getSharingcode());
                        user.setConfirmationToken(RC4.getSharingcode());
                        user.setCreationTime(TimeUtil.getDate());
                        if (userMapper.insertSelective(user) > 0) {
                            this.getBinding(user.getId());
                            User user1 = new User();
                            user1.setEmail(user.getEmail());
                            user1.setSharingToken(user.getSharingToken());
                            returnMap.put("msg", user1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("注册错误：{}", e.getMessage());
            returnMap.put("error", e.getMessage());
        }
        return returnMap;
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
        String _session_id = null;
        try {
            _session_id = CookiesUtil.getCookie(request, "_session_id");

        } catch (Exception e) {
            logger.error("Cookies:{}", e.getMessage());
        }
        if (_session_id != null) {
            redisUtils.removePattern(_session_id);
            CookiesUtil.removeCookie(response, "_session_id");
        }
        Map<String, Object> returnMap = new HashMap<>();
        try {
            User user = userMapper.login(email, PasswdMd5.EncoderByMd5(password));
            if (user == null) {
                returnMap.put("error", "密码有误");
            } else if (user.getState().equals("-1")) {
                returnMap.put("error", "请进行邮箱验证");
            } else if (user.getState().equals("0")) {
                String ipaddress = loginInformation.getIpAddress(request);
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
                    String sessionID = PasswdMd5.EncoderByMd5(RC4.getSharingcode());
                    CookiesUtil.setCookie(response, "_session_id", sessionID, 60 * 60 * 24 * 7);
                    long time = 60 * 60 * 24 * 7;
                    redisUtils.set(sessionID, user.getEmail(), time);
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
        try {
            user.setEmail(loginInformation.getEmain(request));
            if (userMapper.updateByPrimaryKeySelective(user) > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean sendEmail(String email, int type) {
        boolean falg = false;
        User user = userMapper.queryEmail(email);
        String readAddress, method, WriteAddress, title, resetPasswordToken;
        try {

            if (type == 0) {
                resetPasswordToken = user.getConfirmationToken();
                readAddress = "static/email/activationMail.html";
                method = "confirmation?confirmation_token=";
                WriteAddress = "activationMail.html";
                title = "激活您的账户";
                loginInformation.getEmailSend(email, resetPasswordToken, readAddress, method, WriteAddress, title, type);
            } else if (type == 1) {
                resetPasswordToken = RC4.getSharingcode();
                long time = 600;
                redisUtils.set(resetPasswordToken, email, time);
                readAddress = "static/email/modifyMail.html";
                method = "password-reset?reset_password_token=";
                WriteAddress = "modifyMail.html";
                title = "修改密码";
                loginInformation.getEmailSend(email, resetPasswordToken, readAddress, method, WriteAddress, title, type);
            } else if (type == 2) {
                resetPasswordToken = RC4.getSharingcode();
                long time = 600;
                redisUtils.set(resetPasswordToken, email, time);
                readAddress = "static/email/verificationMail.html";
                method = "password-reset?reset_password_token=";
                WriteAddress = "verificationMail.html";
                title = "验证码";
                loginInformation.getEmailSend(email, resetPasswordToken, readAddress, method, WriteAddress, title, type);
            } else if (type == 3) {
                String content = "<html><head></head><body>http://daifengkeji.com:8080/?referral=" + null + "</body></html>";
                MailBox mailBox = new MailBox();
                mailBox.setTitle("推荐链接");
                mailBox.setContent(content);
                new Thread(new MailUtil(email, mailBox)).start();
            }
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
        PageInfo<User> userPage = new PageInfo<>(userList);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("rows", userList);
        returnMap.put("total", userPage.getTotal());
        return returnMap;
    }

    @Override
    public boolean groupMail(List<String> mails, MailBox mailBox) {
        try {
            mailBox.setSystemid(1);
            mailBox.setCreationtime(TimeUtil.getDate());
            mailboxInfo.insertSelective(mailBox);
            if (null == mails || mails.size() == 0) {
                User user = new User();
                user.setCountry(mailBox.getCountry());
                user.setUsdRange(mailBox.getUsdRange());
                mails = userMapper.userConditionList(user);
                verificationMail.groupMail(mails, mailBox);
                return true;
            } else {
                verificationMail.groupMail(mails, mailBox);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
        } else if (user.getState().equals("0")) {
            return true;
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
    public boolean modifyPassword(String password, String resetPasswordToken, String current_password) {

        try {
            int count = 0;
            if (resetPasswordToken != null) {
                String email = redisUtils.get(resetPasswordToken);
                redisUtils.removePattern(resetPasswordToken);
                long time = 180;
                String newPassword = PasswdMd5.EncoderByMd5(password);
                count = userMapper.modifyPassword(email, newPassword);
            } else if (current_password != null) {
                String email = loginInformation.getEmain(request);
                User user = userMapper.login(email, PasswdMd5.EncoderByMd5(current_password));
                if (user != null) {
                    String newPassword = PasswdMd5.EncoderByMd5(password);
                    count = userMapper.modifyPassword(email, newPassword);
                }
            }
            if (count > 0) {
                String sessionID = CookiesUtil.getCookie(request, "_session_id");
                if (sessionID != null) {
                    redisUtils.removePattern(sessionID);
                    CookiesUtil.removeCookie(response, "_session_id");
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User queryUser() {
        try {
            String email = loginInformation.getEmain(request);
            return userMapper.queryUser(email);
        } catch (Exception e) {
            logger.error("获取邮箱失败:{}", e.getMessage());
        }
        return null;
    }

    public boolean getBinding(int userId) {
        try {
            String sharingToken = CookiesUtil.getCookie(request, "referral");
            User referralUser = userMapper.referral(sharingToken);
            Share share = new Share();
            share.setReferralid(referralUser.getId());
            share.setBeintroducedid(userId);
            share.setCreationTime(TimeUtil.getDate());
            shareMapper.insertSelective(share);
            return true;
        } catch (Exception e) {
            logger.error("绑定推荐人失败:{}", e.getMessage());
            return false;
        }
    }
}
