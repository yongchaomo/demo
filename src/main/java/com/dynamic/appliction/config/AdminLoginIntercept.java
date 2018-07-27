package com.dynamic.appliction.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dynamic.appliction.util.CookiesUtil;

/**
 * @program: demo
 * @description: 拦截器
 * @author: Mr.MO
 * @create: 2018-07-04 21:56
 **/
@Component
public class AdminLoginIntercept implements HandlerInterceptor {

    /*
     * 进入controller层之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果已经登录返回true。
        // 如果没有登录没有登录，可以使用 reponse.send() 跳转页面。后面要跟return false,否则无法结束;
        String referral = request.getParameter("referral");
        if (referral != null) {
            CookiesUtil.setCookie(response, "referral", referral, 60 * 60);
        }
        return true;
    }

    /*
     * 处理请求完成后视图渲染之前的处理操作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /*
     * 视图渲染之后的操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
