package com.geeker.service;

import com.geeker.model.User;
import com.geeker.response.Response;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
public interface UserService {

    User getById(Integer id);

    User getByLoginName(String loginName);

    Response loginGeeker(String deviceId);
}
