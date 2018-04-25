package com.geeker.controller;

import com.geeker.response.Response;
import com.geeker.response.ResponseUtils;
import com.geeker.service.UserService;
import com.geeker.utils.LoginUserUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/getById")
    public Response getById(Integer id){
        return ResponseUtils.success(userService.getById(id));
    }

    @RequestMapping("/demo")
    public String demo(Integer id){
        return userService.getById(id).toString();
    }

    @RequestMapping("/getLoginUser")
    public Response getLoginUser(){
        return ResponseUtils.success(LoginUserUtil.getUser());
    }

    @RequestMapping("/loginGeeker")
    public Response loginGeeker(String deviceId){
        return userService.loginGeeker(deviceId);
    }
    @RequestMapping("/boundUser")
    public Response boundUser(){
        return userService.boundUser();
    }
}
