package com.geeker.service.impl;

import com.geeker.mapper.geeker.UserMapper;
import com.geeker.model.User;
import com.geeker.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据登录名查询用户
     * @param loginName
     * @return
     */
    @Override
    public User getByLoginName(String loginName) {
        return userMapper.selectByLoginName(loginName);
    }
}
