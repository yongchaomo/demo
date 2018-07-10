package com.dynamic.appliction.controller;

import com.dynamic.appliction.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: demo
 * @description: 历史记录
 * @author: Mr.MO
 * @create: 2018-06-21 14:12
 **/
@RestController
public class HistoryController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    HistoryService historyService;

    @GetMapping(value = "queryLoginRecordList")
    public Map<String, Object> queryLoginRecordList(int pageSize, int pageNumber) {
        return historyService.queryLoginRecordList(pageSize, pageNumber);
    }
}
