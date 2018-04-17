package com.geeker.mapper.micro;

import com.geeker.model.OpDevice;
import com.geeker.vo.OpDeviceVo;

import java.util.List;

public interface OpDeviceMapper {
    int deleteByPrimaryKey(String id);

    int insert(OpDevice record);

    int insertSelective(OpDevice record);

    OpDevice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OpDevice record);

    int updateByPrimaryKey(OpDevice record);

    List<OpDevice> getList(OpDeviceVo vo);

    int removeBound(OpDevice record);

    OpDevice selectByBoundUserId(Integer id);
}