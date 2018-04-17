package com.geeker.mapper.geeker;

import com.geeker.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByLoginName(String loginName);

    Map<String,String> selectByUserId(@Param("id") Integer id, @Param("comId") Integer comId);

    User selectByWhere(User user);
}