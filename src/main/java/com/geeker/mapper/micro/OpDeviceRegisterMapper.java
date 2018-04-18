package com.geeker.mapper.micro;

import com.geeker.model.OpDeviceRegister;
import com.geeker.model.OpDeviceRegisterKey;

public interface OpDeviceRegisterMapper {
    int deleteByPrimaryKey(OpDeviceRegisterKey key);

    int insert(OpDeviceRegister record);

    int insertSelective(OpDeviceRegister record);

    OpDeviceRegister selectByPrimaryKey(String key);

    int updateByPrimaryKeySelective(OpDeviceRegister record);

    int updateByPrimaryKey(OpDeviceRegister record);
}