package com.dynamic.appliction.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dynamic.appliction.pojo.bean.MailBox;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: demo
 * @description: 验证邮箱匹配数据
 * @author: Mr.MO
 * @create: 2018-06-22 16:01
 **/
@Component
public class VerificationMail {
    @Autowired
    HttpServletRequest request;
    @Autowired
    TimeUtil timeUtil;
    BlockingQueue<Runnable> workQueue;// 任务队列
    ExecutorService es;// 线程池的接口

    public boolean getVCode(String email) {
        Integer mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
        boolean falg = false;
        new Thread(new MailUtil(email, null)).start();
        String nowTime = timeUtil.getTime();
        request.setAttribute("vcodeTime", nowTime + "#" + mobile_code);
        falg = true;
        return falg;
    }

    public boolean cmpVCode(String vcode) throws Exception {
        String vcodeTime = (String) request.getSession().getAttribute("vcodeTime");
        String vcodeTimeArray[] = vcodeTime.split("#");
        boolean falg = false;
        if (vcodeTimeArray[0].equals(vcode)) {
            falg = timeUtil.cmpTime(vcodeTimeArray[1]);
        }
        return falg;
    }

    public boolean groupMail(List<String> mails, MailBox mailBox) {
        workQueue = new LinkedBlockingQueue<>();// 构造无界的任务队列，资源足够，理论可以支持无线个任务
        es = new ThreadPoolExecutor(2, 4, // 2 core； 4 max
                30, TimeUnit.SECONDS, workQueue, // 30s keep alive
                new ThreadPoolExecutor.CallerRunsPolicy()); // 任务失败重试
        es.execute(new MailUtil(mails, mailBox));// 将任务放入线程池
        return true;
    }
}
