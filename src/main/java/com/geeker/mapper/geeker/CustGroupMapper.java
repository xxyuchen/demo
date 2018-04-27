package com.geeker.mapper.geeker;

import com.geeker.model.CustGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustGroup record);

    int insertSelective(CustGroup record);

    CustGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustGroup record);

    int updateByPrimaryKey(CustGroup record);

    List<Map> selectForMarket(@Param("id") Integer id, @Param("synTime")Date synTime,@Param("comId") Integer comId);

    List<String> selectCustForMarket(Integer id);

    List<Integer> selectByCustId(Integer custId);
}