package com.dynamic.appliction.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dynamic.appliction.pojo.bean.Operation;

@Mapper
public interface OperationMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Operation record);

	int insertSelective(Operation record);

	Operation selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Operation record);

	int updateByPrimaryKey(Operation record);
}