package com.dynamic.appliction.service.impl;

import com.dynamic.appliction.dao.LoginsMapper;
import com.dynamic.appliction.dao.UserMapper;
import com.dynamic.appliction.pojo.bean.Logins;
import com.dynamic.appliction.pojo.bean.User;
import com.dynamic.appliction.service.HistoryService;
import com.dynamic.appliction.util.CookiesUtil;
import com.dynamic.appliction.util.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Resource
    LoginsMapper loginsMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    HttpServletRequest request;
    @Autowired
    RedisUtils redisUtils;


    @Override
    public Map<String, Object> queryLoginRecordList(int pageSize, int pageNumber) {
        String _session_id = CookiesUtil.getCookie(request, "_session_id");
        String email = redisUtils.get(_session_id);
        User user = userMapper.queryEmail(email);
        PageHelper.startPage(pageNumber, pageSize);
        List<Logins> logins = loginsMapper.loginsList(user.getId());
        PageInfo<Logins> loginsPage = new PageInfo<Logins>(logins);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("rows", logins);
        returnMap.put("total", loginsPage.getTotal());
        return returnMap;
    }
}
