package com.geeker.controller;

import com.geeker.model.OpDeviceRegister;
import com.geeker.response.Response;
import com.geeker.service.OpDeviceRegisterService;
import com.geeker.vo.OpDeviceRegisterVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/18 0018.
 */
@RestController
@RequestMapping("/register")
public class opDeviceRegisterController {

    @Resource
    private OpDeviceRegisterService opDeviceRegisterService;

    /**
     * 获取注册码
     * @param num
     * @return
     * @throws Exception
     */
    @RequestMapping("/getCodes")
    public Response getRegisterCodes(Integer num) throws Exception {
        return opDeviceRegisterService.getRegisterCodes(num);
    }

    /**
     * 注册设备
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping("/register")
    public Response register(OpDeviceRegisterVo vo) throws Exception {
        return opDeviceRegisterService.register(vo);
    }
}
