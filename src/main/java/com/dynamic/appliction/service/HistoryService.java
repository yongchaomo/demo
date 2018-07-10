package com.dynamic.appliction.service;

import java.util.Map;

public interface HistoryService {
    //分页查询历史记录
    Map<String, Object> queryLoginRecordList(int pageSize, int pageNumber);
}
