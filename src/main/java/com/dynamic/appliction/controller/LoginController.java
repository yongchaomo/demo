package com.dynamic.appliction.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dynamic.appliction.util.HttpClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

/**
 * @program: demo
 * @description:谷歌第三方登录
 * @author: Mr.MO
 * @create: 2018-06-21 14:12
 **/
@Controller
public class LoginController {

    private static String client_id = "850028403875-fu79qusp020ht0idsootgov81dh88957.apps.googleusercontent.com";
    private static String client_secret = "21KZbYe455-b9MWhKwi9IdpU";
    private static String scope = "https://www.googleapis.com/auth/drive.metadata.readonly";
    private static String redirect_url = "http://www.daifengkeji.com/GoogleUserInfo";
    private static String code_url = "https://accounts.google.com/o/oauth2/v2/auth";
    private static String token_url = "https://www.googleapis.com/oauth2/v4/token";
    private static String user_url = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static String verify_url = "https://www.googleapis.com/oauth2/v3/tokeninfo";


    /**
     * @return Object
     * @throws
     * @Title: Login
     * @Description: google登录验证后会重定向到此地址，并附带访问授权码，不能为公开的ip地址
     * @author 此方法是用带回的code换取accessToken，然后用accessToken换取用户信息，这个地址就是在创建应用时定义的重定向地址
     * @date Mar 23, 2017 10:37:38 AM
     */
    @RequestMapping(value = "/GoogleUserInfo")
    @ResponseBody
    public static Object Login(HttpServletRequest request) {
        String code = request.getParameter("code");
        System.out.println("=============================================");
        System.out.println("GoogleUserInfo:code" + code);
        System.out.println("=============================================");
//        String idToken = getGoogleAccessToken(code);
//        System.out.println(idToken);
//        JSONObject verifyToken = verifyToken(idToken);
//        System.out.println(verifyToken);

        String accessToken = getGoogleAccessToken(code);
        System.out.println("======================================" + accessToken);
        JSONObject userInfo = getUserInfo(accessToken);
        System.out.println(userInfo);
        return userInfo;
    }

    /**
     * @return String
     * @throws Exception
     * @throws Exception
     * @throws
     * @Title: sendRedirect
     * @Description:发送授权请求
     * @author 第二步，在google.jsp中用户登录成功以后回跳转到这个路径，发送请求让用户授权，授权成功后重定向到/GoogleUserInfo，也就是创建应用时定义的重定向地址
     * @date Mar 24, 2017 3:11:36 PM
     */
    @RequestMapping(value = "/sendRedirect")
    public void sendRedirect(HttpServletResponse response) throws Exception {
        // 随机字符串，防止csrf攻击
        String state = UUID.randomUUID() + "";
        Map<String, String> params = new HashMap<>();
        params.put("client_id", client_id);
        params.put("redirect_uri", redirect_url);
        params.put("response_type", "code");
        params.put("scope", scope);
        params.put("access_type", "offline");
        params.put("state", state);
        params.put("include_granted_scopes", "true");
        String url = HttpClientUtil.getUrl(code_url, params);
        System.out.println("==========================================");
        System.out.println(url);
        System.out.println("==========================================");
        response.sendRedirect(url);
    }

    /**
     * @return String
     * @throws
     * @Title: getGoogleAccessToken
     * @Description: 获取accessToken
     * @author 第三步，用重定向带回来的code换取accessToken
     * @date Mar 25, 2017 10:25:00 AM
     */
    public static String getGoogleAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", client_id);
        params.put("redirect_uri", redirect_url);
        params.put("client_secret", client_secret);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        String accessToken = null;
        JSONObject jsonObject = JSONObject.fromObject(HttpClientUtil.getStringByPost(token_url, params));
        accessToken = jsonObject.getString("access_token");
        return accessToken;
    }

    /**
     * @return String
     * @throws
     * @Title: getUserInfo
     * @Description: 获取用户信息
     * @author第四步，用accessToken获取用户信息
     * @date Mar 25, 2017 11:50:23 AM
     */
    public static JSONObject getUserInfo(String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        String[] responseResult = null;
        JSONObject userInfo = null;
        String geturl = HttpClientUtil.getStringByGet(user_url, params);
        System.out.println("===========================================");
        System.out.println(geturl);
        System.out.println("===========================================");
//        try {
//            responseResult = HttpClientUtil.getStringByGet(user_url, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (null != responseResult && responseResult[0].equals("200")) {
//            String result = responseResult[1];
//            userInfo = JSONObject.fromObject(result);
//        }
        return userInfo;
    }

    /**
     * @return String
     * @throws
     * @Title: verifyToken
     * @Description:验证用户token是否是来自本应用的请求，校验aud和clientID是否相同
     * @author第五步，验证用户是否来自你的应用，防刷，根据需要加到逻辑里
     * @date Mar 25, 2017 7:36:33 PM
     */
//    public static JSONObject verifyToken(String idToken) {
//        Map<String, String> params = new HashMap<>();
//        params.put("id_token", idToken);
//        String[] responseResult = null;
//        JSONObject verifyInfo = null;
//        try {
//            responseResult = HttpClientUtil.getStringByGet(verify_url, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (null != responseResult && responseResult[0].equals("200")) {
//            String result = responseResult[1];
//            verifyInfo = JSONObject.fromObject(result);
//        }
//        return verifyInfo;
//    }
}
