package com.geeker.controller;

import com.geeker.response.Response;
import com.geeker.response.ResponseUtils;
import com.geeker.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
@RestController
@RequestMapping("user")
public class userController {
    @Resource
    private UserService userService;
    @RequestMapping("getById")
    public Response demo(Integer id){
        return ResponseUtils.success(userService.getById(id));
    }
}
