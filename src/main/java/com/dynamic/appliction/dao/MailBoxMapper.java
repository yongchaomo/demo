package com.dynamic.appliction.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dynamic.appliction.pojo.bean.MailBox;

@Mapper
public interface MailBoxMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(MailBox record);

	int insertSelective(MailBox record);

	MailBox selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(MailBox record);

	int updateByPrimaryKey(MailBox record);
}