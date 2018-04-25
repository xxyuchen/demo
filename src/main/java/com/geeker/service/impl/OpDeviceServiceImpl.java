package com.geeker.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geeker.config.RestUrlConfig;
import com.geeker.enums.DeviceEnum;
import com.geeker.mapper.geeker.CustGroupMapper;
import com.geeker.mapper.geeker.CustMapper;
import com.geeker.mapper.geeker.UserMapper;
import com.geeker.mapper.micro.OpDeviceMapper;
import com.geeker.model.OpDevice;
import com.geeker.model.User;
import com.geeker.response.CamelResponse;
import com.geeker.response.Response;
import com.geeker.response.ResponseUtils;
import com.geeker.service.OpDeviceService;
import com.geeker.utils.LoginUserUtil;
import com.geeker.utils.SecureNumberUtil;
import com.geeker.vo.OpDeviceVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Author TangZhen
 * @Date 2018/4/13 0013 11:24
 * @Description 注册手机
 */
@Service
@Slf4j
public class OpDeviceServiceImpl implements OpDeviceService {
    @Resource
    private OpDeviceMapper opDeviceMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CustMapper custMapper;

    @Resource
    private RestUrlConfig restUrlConfig;

    @Resource
    private CustGroupMapper custGroupMapper;

    private RestTemplate restTemplate = new RestTemplate();

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(20, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("phoneBook-pool");
        return t;
    });

    /**
     * 手机设备列表
     *
     * @param vo
     * @return
     */
    @Override
    public Response getList(OpDeviceVo vo) {
        PageHelper.startPage(null == vo.getPageIndex()?1:vo.getPageIndex(), null == vo.getPageSize()?20:vo.getPageSize());
        User user = LoginUserUtil.getUser();
        vo.setComId(vo.getComId());
        if(StringUtils.isNotEmpty(vo.getDimQusery())){
            vo.setDimQusery("%"+vo.getDimQusery()+"%");
        }
        List<OpDevice> list = opDeviceMapper.getList(vo);
        List<OpDeviceVo> voList = new ArrayList<>(20);
        for (OpDevice opDevice : list) {
            OpDeviceVo opDeviceVo = new OpDeviceVo();
            BeanUtils.copyProperties(opDevice, opDeviceVo);
            if (null != opDevice.getBoundUserId()) {
                //关联出用户及部门
                Map<String, String> map = userMapper.selectByUserId(user.getId(), user.getCompanyId());
                opDeviceVo.setUserName(map.get("userName"));
                opDeviceVo.setDepartName(map.get("departName"));
            }
            voList.add(opDeviceVo);
        }
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return ResponseUtils.successPage(pageInfo);
    }

    /**
     * 绑定微手机
     *
     * @param vo
     * @return
     */
    @Override
    public Response boundDevice(OpDeviceVo vo) throws Exception {
        if (StringUtils.isEmpty(vo.getId())) {
            throw new Exception("设备id不能为空！");
        }
        if (vo.getBoundUserId() == null) {
            throw new Exception("绑定人id不能为空！");
        }
        //先判断该设备是否已绑定用户
        OpDevice opDevice = opDeviceMapper.selectByPrimaryKey(vo.getId());
        if (null == opDevice) {
            throw new Exception("该设备不存在！");
        }
        //判断该绑定人是否已绑定设备
        if (null != opDeviceMapper.selectByBoundUserId(vo.getBoundUserId())) {
            throw new Exception("该用户已绑定设备！");
        }
        User user = new User();
        user.setId(vo.getBoundUserId());
        user.setCompanyId(LoginUserUtil.getUser().getCompanyId());
        User dto = userMapper.selectByWhere(user);
        if (null == dto) {
            throw new Exception("绑定人不存在！");
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("deviceId", vo.getId());
        map.put("comId", dto.getCompanyId());
        map.put("userId", LoginUserUtil.getUserId());
        map.put("userLoginName",dto.getLoginName());
        map.put("userName",dto.getUserName());
        if (null != opDevice.getBoundUserId()) {
            //先解绑再绑定
            CamelResponse camelResponse = restTemplate.postForObject(restUrlConfig.getRemoveBound(), map, CamelResponse.class);
            if (camelResponse.getCode() != 200) {
                return camelResponse;
            }
        }
        map.put("boundUserId", vo.getBoundUserId());
        CamelResponse camelResponse = restTemplate.postForObject(restUrlConfig.getBound(), map, CamelResponse.class);

        if (camelResponse.getCode() != 200) {
            return camelResponse;
        }
        OpDevice device = new OpDevice();
        device.setId(vo.getId());
        device.setBoundUserId(dto.getId());
        device.setBoundUserLoginName(dto.getLoginName());
        device.setBoundUserName(dto.getUserName());
        device.setBoundTime(new Date());
        device.setStatus(DeviceEnum.deviceStatusEnum.BOUND.getCode());
        opDeviceMapper.updateByPrimaryKeySelective(device);
        return ResponseUtils.success();
    }

    /**
     * 解除绑定
     *
     * @param id
     * @return
     */
    @Override
    public Response removeBound(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new Exception("设备id不能为空！");
        }
        User user = LoginUserUtil.getUser();
        Map<String, Object> map = new HashMap<>(3);
        map.put("deviceId", id);
        map.put("comId", user.getCompanyId());
        map.put("userId", user.getId());
        CamelResponse camelResponse = restTemplate.postForObject(restUrlConfig.getRemoveBound(), map, CamelResponse.class);
        if (camelResponse.getCode() == 200) {
            OpDevice opDevice = new OpDevice();
            opDevice.setId(id);
            opDevice.setBoundTime(new Date());
            opDevice.setStatus(DeviceEnum.deviceStatusEnum.UNBOUND.getCode());
            opDeviceMapper.removeBound(opDevice);
            return ResponseUtils.success();

        } else {
            return camelResponse;
        }
    }

    /**
     * 打电话
     *
     * @param custId
     * @return
     * @throws Exception
     */
    @Override
    public Response call(Integer custId) throws Exception {
        User user = LoginUserUtil.getUser();
        if (null == custId) {
            throw new Exception("客户id不能为空！");
        }
        String mobile = custMapper.selectById(user.getId(), custId);
        if (StringUtils.isEmpty(mobile)) {
            throw new Exception("该客户无电话号码！");
        }
        Map<String, Object> map = new HashMap<>(4);
        map.put("deviceId", user.getDeviceId());
        map.put("comId", user.getCompanyId());
        map.put("userId", user.getId());
        map.put("mobile", SecureNumberUtil.createSecureNumber(custId));
        return restTemplate.postForObject(restUrlConfig.getCall(), map, CamelResponse.class);
    }

    /**
     * 同步通讯录
     *
     * @param synTime
     * @param id
     * @param deviceId
     * @param companyId @return
     */
    @Override
    public void phoneBook(Date synTime, Integer id, String deviceId, Integer companyId) {
        executorService.execute(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Map> list = custMapper.selectForPhoneBook(id, synTime);
                    if (list == null || list.size() <= 0) {
                        log.info("无新数据，本次同步结束！");
                        return;
                    }
                    List<String> mobiles = new ArrayList<>();
                    for (Map map : list) {
                        if (null!=map.get("mobileStatus")&&map.get("mobileStatus").equals(10)) {
                            mobiles.add(map.get("mobile").toString());
                        }
                    }
                    Map<String, String> encrypMap = new HashMap<>();
                    try {
                        encrypMap = restTemplate.postForObject(restUrlConfig.getMobileDecodeUrl(), mobiles, Map.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("号码解析服务异常{}", e);
                    }
                    List<Map<String, String>> data = new ArrayList<>(20);
                    List<String> delMobiles = new ArrayList<>();
                    Map<String, String> stringMap;
                    for (Map map : list) {
                        if (!map.get("status").equals(1)) {
                            delMobiles.add(map.get("name").toString());
                        } else {
                            stringMap = new HashMap<>(5);
                            if (encrypMap.containsKey(map.get("mobile"))) {
                                stringMap.put("phone", encrypMap.get(map.get("mobile")));
                            } else {
                                stringMap.put("phone", map.get("mobile").toString());
                            }
                            stringMap.put("secureNumber", map.get("name").toString());
                            stringMap.put("nickname", map.get("nickName").toString());
                            String sex = (String) map.get("sex");
                            if (StringUtils.isNotEmpty(sex)) {
                                stringMap.put("sex", sex);
                            }
                            data.add(stringMap);
                        }
                    }
                    Map<String, Object> map = new HashMap<>(6);
                    map.put("mobiles", data);
                    map.put("delMobiles", delMobiles);
                    map.put("deviceId", deviceId);
                    map.put("comId", companyId);
                    map.put("userId", id);
                    map.put("createTime", list.get(0).get("createTime"));
                    CamelResponse camelResponse = restTemplate.postForObject(restUrlConfig.getPhoneBook(), map, CamelResponse.class);
                    if (camelResponse.getCode() != 200) {
                        log.error("同步通讯录失败==>result：{}", camelResponse.getMessage());
                    }
                } catch (Exception e) {
                    log.error("同步手机通讯录异常", e);
                }
            }

        });
    }

    /**
     * 同步群组
     *
     * @param synTime
     * @param id
     * @param deviceId
     * @param companyId
     */
    @Override
    public void group(Date synTime, Integer id, String deviceId, Integer companyId) {
        executorService.execute(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Map> list = custGroupMapper.selectForMarket(id, synTime);
                    if (null == list || list.size() <= 0) {
                        log.info("无新数据，本次同步结束！");
                        return;
                    }
                    List<Integer> delGroups = new ArrayList<>();
                    List<Map<String, Object>> data = new ArrayList<>();
                    Map<String, Object> groupMap;
                    List<String> custs = new ArrayList<>();
                    for (Map map : list) {
                        if (!map.get("status").equals(1)) {
                            delGroups.add((Integer) map.get("id"));
                        } else {
                            groupMap = new HashMap<>(3);
                            groupMap.put("id", map.get("id"));
                            groupMap.put("name", map.get("name"));
                            custs = custGroupMapper.selectCustForMarket((Integer) map.get("id"));
                            Set<String> set = new HashSet<>();
                            for (String name : custs) {
                                if (StringUtils.isNotEmpty(name)) {
                                    set.add(name);
                                }
                            }
                            groupMap.put("custs", set);
                            data.add(groupMap);
                        }
                    }
                    Map<String, Object> map = new HashMap<>(6);
                    map.put("groups", data);
                    map.put("delGroups", delGroups);
                    map.put("deviceId", deviceId);
                    map.put("comId", companyId);
                    map.put("userId", id);
                    map.put("createTime", list.get(0).get("createTime"));
                    CamelResponse camelResponse = restTemplate.postForObject(restUrlConfig.getGroup(), map, CamelResponse.class);
                    if (camelResponse.getCode() != 200) {
                        log.error("同步群组失败==>result：{}", camelResponse.getMessage());
                    }
                } catch (Exception e) {
                    log.error("同步群组异常", e);
                }
            }
        });
    }

    /**
     * 发送短信
     *
     * @param custId
     * @return
     */
    @Override
    public Response sendSms(Integer custId, String parm) throws Exception {
        User user = LoginUserUtil.getUser();
        if (null == custId) {
            throw new Exception("客户id不能为空！");
        }
        if (StringUtils.isEmpty(parm)) {
            throw new Exception("短信内容不能为空！");
        }
        String mobile = custMapper.selectById(user.getId(), custId);
        if (StringUtils.isEmpty(mobile)) {
            throw new Exception("该客户无电话号码！");
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("deviceId", user.getDeviceId());
        map.put("comId", user.getCompanyId());
        map.put("userId", user.getId());
        map.put("secureNumber", SecureNumberUtil.createSecureNumber(custId));
        map.put("parm", parm);
        return restTemplate.postForObject(restUrlConfig.getSendSms(), map, CamelResponse.class);
    }

    @Override
    public OpDevice selectByBoundUserId(Integer userId) {
        return opDeviceMapper.selectByBoundUserId(userId);
    }

    @Override
    public OpDevice selectByPrimaryKey(String id) {
        return opDeviceMapper.selectByPrimaryKey(id);
    }

    /**
     * 上传音频
     *
     * @param file
     * @return
     */
    @Override
    public Response uploadVoice(MultipartFile file) throws Exception {
        if (null == file) {
            throw new Exception("音频文件不能为空！");
        }
        String voiceUri = "";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        File tempFile = null;
        try {
            String contentType = file.getContentType();
            String root_fileName = file.getOriginalFilename();
            log.info("上传音频:name={},type={}", root_fileName, contentType);
            String tempFileName = UUID.randomUUID() + root_fileName.substring(root_fileName.lastIndexOf("."));
            String tempFilePath = restUrlConfig.getLocalVoicePath() + tempFileName;
            tempFile = new File(tempFilePath);
            file.transferTo(tempFile);

            FileSystemResource fileSystemResource = new FileSystemResource(tempFilePath);
            param.add("fileType", "voice");
            param.add("file", fileSystemResource);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
            ResponseEntity<String> responseEntity = restTemplate.exchange(restUrlConfig.getUploadFileUrl(), HttpMethod.POST, httpEntity, String.class);
            JSONObject result = JSON.parseObject(responseEntity.getBody());
            if (result.getInteger("status") == 200) {
                Map<String, Object> data = result.getJSONObject("data");
                voiceUri = data.get("qnUrl").toString();
            } else {
                log.info("音频上传失败！【{}】,{}", result.getInteger("status"), result.getString("message"));
                throw new Exception("音频上传失败！");
            }
        } catch (Exception e) {
            log.error("音频上传异常：", e);
            throw e;
        } finally {
            tempFile.delete();
            //eventPublisher.publishEvent(new PublicEvent.ClientReportEvent("",voiceUri));
        }
        return ResponseUtils.success(voiceUri);
    }
}

