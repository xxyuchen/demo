package com.geeker.service;

import com.geeker.model.OpDevice;
import com.geeker.response.Response;
import com.geeker.vo.OpDeviceVo;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
public interface OpDeviceService {

    Response getList(OpDeviceVo vo);

    Response boundDevice(OpDeviceVo vo) throws Exception;

    Response removeBound(String id) throws Exception;

    Response call(Integer custId) throws Exception;

    void phoneBook(Date synTime, Integer id, String deviceId, Integer companyId);

    void group(Date synTime, Integer id, String deviceId, Integer companyId);

    Response sendSms(Integer custId,String parm) throws Exception;

    OpDevice selectByBoundUserId(Integer userId);

    OpDevice selectByPrimaryKey(String id);
}
