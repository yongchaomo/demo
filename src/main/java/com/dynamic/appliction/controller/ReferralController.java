package com.dynamic.appliction.controller;

import com.dynamic.appliction.pojo.bean.Resp;
import com.dynamic.appliction.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @program: demo
 * @description: 分享管理
 * @author: Mr.MO
 * @create: 2018-07-09 22:33
 **/
@Controller
public class ReferralController {
    @Autowired
    ReferralService referralService;

    @ResponseBody
    @GetMapping("getReferralUrl")
    public Resp getReferralUrl() {
        return Resp.httpStatus(HttpStatus.ACCEPTED, "获取个人分享验证码", referralService.getReferralUrl());
    }

    @ResponseBody
    @GetMapping(value = "queryReferralList")
    public Map<String, Object> queryReferralList(int pageSize, int pageNumber) {
        return referralService.getQueryReferralList(pageSize, pageNumber);
    }
}
