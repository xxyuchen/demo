package com.geeker.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geeker.config.RestUrlConfig;
import com.geeker.mapper.geeker.UserMapper;
import com.geeker.mapper.micro.OpDeviceMapper;
import com.geeker.model.OpDevice;
import com.geeker.model.User;
import com.geeker.response.CamelResponse;
import com.geeker.response.Response;
import com.geeker.response.ResponseUtils;
import com.geeker.service.UserService;
import com.geeker.utils.LoginUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RestUrlConfig restUrlConfig;

    private RestTemplate restTemplate = new RestTemplate();

    @Resource
    private OpDeviceMapper opDeviceMapper;

    @Override
    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据登录名查询用户
     *
     * @param loginName
     * @return
     */
    @Override
    public User getByLoginName(String loginName) {
        return userMapper.selectByLoginName(loginName);
    }

    /**
     * 登录数聚客
     *
     * @param deviceId
     * @return
     */
    @Override
    public Response loginGeeker(String deviceId) {
        //指令上报
        OpDevice opDevice = opDeviceMapper.selectByPrimaryKey(deviceId);
        if (null == opDevice) {
            return ResponseUtils.error(5005, "设备不存在！");
        }
        if (null == opDevice.getBoundUserId()) {
            return ResponseUtils.error(5006, "该设备未绑定任何用户！");
        }
        User user = userMapper.selectByPrimaryKey(opDevice.getBoundUserId());
        if (null == user) {
            return ResponseUtils.error(5006, "绑定用户异常！");
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("deviceId", deviceId);
        map.put("comId", user.getCompanyId());
        map.put("userId", user.getId());
        CamelResponse camelResponse = restTemplate.postForObject(restUrlConfig.getReportLogin(), map, CamelResponse.class);
        //登录数聚客
        Map<String,Object> loginMap = new HashMap<>();
        loginMap.put("username", user.getLoginName());
        /*loginMap.put("loginPwd","111111");
        loginMap.put("type",1);*/
        CamelResponse response = restTemplate.postForObject(restUrlConfig.getLoginGeeker(), loginMap, CamelResponse.class);
        if(null != response){
            if(response.getCode().equals(200)){
                JSONObject json =  JSONObject.parseObject(JSON.toJSONString(response.getData()));
                Map<String,Object> data = new HashMap<>(2);
                data.put("token",json.getString("token"));
                data.put("expire",json.getLong("expire"));
                return ResponseUtils.success(data);
            }else {
                return response;
            }
        }else {
            return ResponseUtils.error(500,"登录失败！");
        }

    }

    /**
     * 查询已绑定用户
     * @return
     */
    @Override
    public Response boundUser() {
        return ResponseUtils.success(opDeviceMapper.boundUser(LoginUserUtil.getUser().getCompanyId()));
    }
}
