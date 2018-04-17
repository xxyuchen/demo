package com.geeker.mapper.geeker;

import com.geeker.model.Cust;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cust record);

    int insertSelective(Cust record);

    Cust selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cust record);

    int updateByPrimaryKey(Cust record);

    List<Map> selectForPhoneBook(@Param("userId")Integer userId, @Param("createTime")Date createTime);

    String selectById(@Param("userId")Integer userId, @Param("id")Integer id);
}