package com.dynamic.appliction.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dynamic.appliction.pojo.bean.Auths;

@Mapper
public interface AuthsMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Auths record);

	int insertSelective(Auths record);

	Auths selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Auths record);

	int updateByPrimaryKey(Auths record);
}