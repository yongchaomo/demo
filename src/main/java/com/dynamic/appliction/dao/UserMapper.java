package com.dynamic.appliction.dao;

import com.dynamic.appliction.pojo.bean.User;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User queryEmail(@Param("email") String email);

    User login(@Param("email") String email, @Param("password") String password);

    int updateByPrimaryKeySelective(User record);

    int modifyPassword(@Param("email") String email, @Param("password") String password);

    List<User> userList(@Param("country") String country, @Param("usdRange") String usdRange);

    List<String> userConditionList(User record);

    User confirmation(@Param("confirmationToken") String confirmationToken);

    User referral(@Param("sharingToken") String sharingToken);
}