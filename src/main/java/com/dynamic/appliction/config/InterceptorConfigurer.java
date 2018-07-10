package com.dynamic.appliction.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: demo
 * @description: 拦截器
 * @author: Mr.MO
 * @create: 2018-07-04 21:56
 **/
@Configuration
public class InterceptorConfigurer implements WebMvcConfigurer {
    @Resource
    AdminLoginIntercept adminLoginIntercept;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，
        registry.addInterceptor(adminLoginIntercept).addPathPatterns("/**");
    }
}
