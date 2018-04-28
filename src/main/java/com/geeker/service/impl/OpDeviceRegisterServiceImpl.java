package com.geeker.service.impl;

import com.geeker.enums.DeviceEnum;
import com.geeker.mapper.micro.OpDeviceMapper;
import com.geeker.mapper.micro.OpDeviceRegisterMapper;
import com.geeker.model.OpDevice;
import com.geeker.model.OpDeviceRegister;
import com.geeker.response.Response;
import com.geeker.response.ResponseUtils;
import com.geeker.service.OpDeviceRegisterService;
import com.geeker.utils.LoginUserUtil;
import com.geeker.utils.SecureNumberUtil;
import com.geeker.vo.OpDeviceRegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18 0018.
 */
@Service
@Slf4j
public class OpDeviceRegisterServiceImpl implements OpDeviceRegisterService {

    @Resource
    private OpDeviceRegisterMapper opDeviceRegisterMapper;

    @Resource
    private OpDeviceMapper opDeviceMapper;

    /**
     * 获取注册码
     *
     * @param num
     * @return
     */
    @Override
    public Response getRegisterCodes(Integer num) throws Exception {
        if (null == num) {
            num = 1;
        }
        if (num > 100) {
            num = 100;
        }
        String stringRandom = "";
        OpDeviceRegister opDeviceRegister;
        Integer comId = LoginUserUtil.getUser().getCompanyId();
        List<String> codes = new ArrayList<>(num);
        for (int i = 1; i <= num; i++) {
            stringRandom = SecureNumberUtil.getStringRandom(6);
            opDeviceRegister = new OpDeviceRegister();
            opDeviceRegister.setCreateTime(new Date());
            opDeviceRegister.setRegisterCode(stringRandom);
            opDeviceRegister.setComId(comId);
            opDeviceRegisterMapper.insert(opDeviceRegister);
            codes.add(stringRandom);
        }
        return ResponseUtils.success("codes", codes);
    }

    /**
     * 注册设备
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(value = "microTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Response register(OpDeviceRegisterVo vo) throws Exception {
        if (StringUtils.isEmpty(vo.getId())) {
            throw new Exception("设备id不能为空！");
        }
        //注册码校验
        OpDeviceRegister deviceRegister = opDeviceRegisterMapper.selectByPrimaryKey(vo.getRegisterCode());
        if (null == deviceRegister) {
            throw new Exception("注册码不存在！");
        }
        if (StringUtils.isNotEmpty(deviceRegister.getDeviceId())) {
            throw new Exception("该注册码已失效，请重新注册！");
        }
        /*if(DateUtils.getTwoDaySubByMinute(deviceRegister.getCreateTime(),new Date())>30){//注册码30分钟失效
            throw new Exception("注册码已失效，请重新获取！");
        }*/
        try {
            OpDeviceRegister opDeviceRegister = new OpDeviceRegister();
            opDeviceRegister.setRegisterCode(vo.getRegisterCode());
            opDeviceRegister.setDeviceId(vo.getId());
            opDeviceRegisterMapper.updateByPrimaryKeySelective(opDeviceRegister);

            OpDevice opDevice = new OpDevice();
            BeanUtils.copyProperties(vo, opDevice);
            opDevice.setComId(deviceRegister.getComId());
            opDevice.setCreateTime(new Date());
            opDevice.setStatus(0);
            opDeviceMapper.insert(opDevice);
        } catch (Exception e) {
            log.info("设备注册失败！", e);
            throw new RuntimeException(e);
        }
        return ResponseUtils.success();
    }
}
