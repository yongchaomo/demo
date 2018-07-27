package com.dynamic.appliction.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dynamic.appliction.pojo.bean.Share;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Share record);

    int insertSelective(Share record);

    Share selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Share record);

    int updateByPrimaryKey(Share record);

    List<Share> shareList(@Param("email") String email);
}