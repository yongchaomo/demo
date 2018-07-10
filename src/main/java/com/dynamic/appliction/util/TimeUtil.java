package com.dynamic.appliction.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: demo
 * @description: 判断邮箱验证码失效时间
 * @author: Mr.MO
 * @create: 2018-06-22 15:44
 **/
@Component
public class TimeUtil {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String CUSTOM_FORMAT = "yyyy-MM-dd";

    // 获取时间 返回毫秒级时间
    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        Long date = calendar.getTime().getTime(); // 获取毫秒时间
        return date.toString();
    }


    public boolean cmpTime(String time) {
        long tempTime = Long.parseLong(time);
        // 在获取现在的时间
        Calendar calendar = Calendar.getInstance();
        Long date = calendar.getTime().getTime(); // 获取毫秒时间
        if (date - tempTime > 600000) { // 10分钟
            return false;
        } else {
            return true;
        }
    }

    public static String getDate() {
        return new SimpleDateFormat(DEFAULT_FORMAT).format(new Date());
    }

    public static String getSpecificDate() {
        return new SimpleDateFormat(CUSTOM_FORMAT).format(new Date());
    }


    public static void main(String[] args) {
        System.out.println(TimeUtil.getSpecificDate());
    }
}
