package com.dynamic.appliction.service.impl;

import com.dynamic.appliction.dao.LoginsMapper;
import com.dynamic.appliction.dao.UserMapper;
import com.dynamic.appliction.pojo.bean.Logins;
import com.dynamic.appliction.pojo.bean.User;
import com.dynamic.appliction.service.HistoryService;
import com.dynamic.appliction.util.LoginInformation;
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
    LoginInformation loginInformation;


    @Override
    public Map<String, Object> queryLoginRecordList(int pageSize, int pageNumber) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            String email = loginInformation.getEmain(request);
            User user = userMapper.queryEmail(email);
            PageHelper.startPage(pageNumber, pageSize);
            List<Logins> loginsList = loginsMapper.loginsList(user.getId());
            PageInfo<Logins> pageInfo = new PageInfo<>(loginsList);
            returnMap.put("rows", loginsList);
            returnMap.put("total", pageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }
}
