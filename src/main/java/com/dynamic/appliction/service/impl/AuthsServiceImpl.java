package com.dynamic.appliction.service.impl;

import com.dynamic.appliction.dao.AuthsMapper;
import com.dynamic.appliction.dao.LoginsMapper;
import com.dynamic.appliction.dao.UserMapper;
import com.dynamic.appliction.pojo.bean.Auths;
import com.dynamic.appliction.pojo.bean.Logins;
import com.dynamic.appliction.pojo.bean.User;
import com.dynamic.appliction.service.AuthsService;
import com.dynamic.appliction.util.*;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthsServiceImpl implements AuthsService {
    @Resource
    UserMapper userMapper;
    @Resource
    AuthsMapper authsMapper;
    @Resource
    LoginsMapper loginsMapper;
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    LoginInformation loginInformation;

    @Override
    public boolean authsLogin(User user, Auths auths) {
        class LoginClass {
            boolean getLogin(int userId) {
                try {
                    String ipaddress = loginInformation.getIpAddress(request);
                    //获取浏览器信息
                    // 转成UserAgent对象
                    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                    //获取浏览器信息
                    Browser browser = userAgent.getBrowser();
                    //获取系统信息
                    OperatingSystem os = userAgent.getOperatingSystem();
                    Logins logins = new Logins();
                    logins.setIpaddress(ipaddress);
                    //浏览器名称
                    logins.setBrowser(browser.getName());
                    //系统名称
                    logins.setOs(os.getName());
                    logins.setUserId(userId);
                    logins.setCreationTime(TimeUtil.getDate());
                    if (loginsMapper.insertSelective(logins) > 0) {
                        String sessionID = PasswdMd5.EncoderByMd5(RC4.getSharingcode());
                        CookiesUtil.setCookie(response, "_session_id", sessionID, 60 * 60 * 24 * 7);
                        long time = 60 * 60 * 24 * 7;
                        redisUtils.set(sessionID, auths.getIdentifier(), time);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        }
        boolean falg = false;
        try {
            List<Auths> authsList = authsMapper.queryAuthsList(auths.getIdentifier());
            if (authsList == null || authsList.size() == 0) {
                user.setEmail(auths.getIdentifier());
                user.setState("0");
                user.setConfirmationToken(RC4.getSharingcode());
                user.setCreationTime(TimeUtil.getDate());
                if (userMapper.insertSelective(user) > 0) {
                    auths.setUserId(user.getId());
                    auths.setCreationTime(TimeUtil.getDate());
                    if (authsMapper.insertSelective(auths) > 0) {
                        falg = new LoginClass().getLogin(user.getId());
                    }
                }
            } else {
                List<String> identityTypes = authsList.stream().map(Auths::getIdentityType).collect(Collectors.toList());
                if (identityTypes.contains(auths.getIdentityType())) {
                    System.out.println("进行登录，数据保存redis");
                    falg = new LoginClass().getLogin(authsList.get(0).getUserId());
                } else {
                    System.out.println("添加到第三方登录,并且进行登录，数据保存redis");
                    auths.setUserId(authsList.get(0).getUserId());
                    auths.setCreationTime(TimeUtil.getDate());
                    if (authsMapper.insertSelective(auths) > 0) {
                        falg = new LoginClass().getLogin(authsList.get(0).getUserId());
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return falg;
    }
}
