package com.dynamic.appliction.service.impl;

import com.dynamic.appliction.dao.SubscribeMapper;
import com.dynamic.appliction.pojo.bean.Subscribe;
import com.dynamic.appliction.service.SubscribeService;
import com.dynamic.appliction.util.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class SubscribeServiceImpl implements SubscribeService {
    @Resource
    SubscribeMapper subscribeMapper;

    @Override
    public Map<String, Object> queryEmail(String email) {
        Map<String, Object> returnMap = new HashMap<>();
        if (!subscribeMapper.queryEmail(email)) {
            Subscribe subscribe = new Subscribe();
            subscribe.setEmail(email);
            subscribe.setCreationTime(TimeUtil.getDate());
            if (subscribeMapper.insertSelective(subscribe) > 0) {
                returnMap.put("msg", "Saved successfully");
            } else {
                returnMap.put("error", "Save failed");
            }
        } else {
            returnMap.put("error", "The mailbox already exists");
        }
        return returnMap;
    }
}
