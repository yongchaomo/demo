package com.dynamic.appliction.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dynamic.appliction.pojo.bean.Logins;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Logins record);

    int insertSelective(Logins record);

    Logins selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Logins record);

    int updateByPrimaryKey(Logins record);

    List<Logins> loginsList(@Param("userId") int userId);
}