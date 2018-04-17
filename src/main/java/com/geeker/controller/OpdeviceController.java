package com.geeker.controller;

import com.geeker.response.Response;
import com.geeker.service.OpDeviceService;
import com.geeker.vo.OpDeviceVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
* @Author TangZhen
* @Date 2018/4/13 0013 16:18
* @Description  注册手机业务
*/
@RestController
@RequestMapping("opDevice")
public class OpdeviceController {

    @Resource
    private OpDeviceService opDeviceService;

    /**
     * 注册手机列表
     * @param vo
     * @return
     */
    @RequestMapping("/getList")
    public Response getList(OpDeviceVo vo){
        return opDeviceService.getList(vo);
    }
    /**
     * 绑定手机
     * @param vo
     * @return
     */
    @RequestMapping("/boundDevice")
    public Response boundDevice(OpDeviceVo vo) throws Exception {
        return opDeviceService.boundDevice(vo);
    }
    /**
     * 解除绑定
     * @param deviceId
     * @return
     */
    @RequestMapping("/removeBound")
    public Response removeBound(String deviceId) throws Exception {
        return opDeviceService.removeBound(deviceId);
    }
}
