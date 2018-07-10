package com.dynamic.appliction.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dynamic.appliction.pojo.bean.TransactionRecord;

@Mapper
public interface TransactionRecordMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(TransactionRecord record);

	int insertSelective(TransactionRecord record);

	TransactionRecord selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(TransactionRecord record);

	int updateByPrimaryKey(TransactionRecord record);
}