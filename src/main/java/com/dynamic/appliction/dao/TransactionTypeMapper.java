package com.dynamic.appliction.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dynamic.appliction.pojo.bean.TransactionType;

@Mapper
public interface TransactionTypeMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(TransactionType record);

	int insertSelective(TransactionType record);

	TransactionType selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(TransactionType record);

	int updateByPrimaryKey(TransactionType record);
}