package com.geeker.service;

import com.geeker.response.Response;
import com.geeker.vo.OpDeviceRegisterVo;

/**
 * Created by Administrator on 2018/4/18 0018.
 */
public interface OpDeviceRegisterService {

    Response getRegisterCodes(Integer num) throws Exception;

    Response register(OpDeviceRegisterVo vo) throws Exception;
}
