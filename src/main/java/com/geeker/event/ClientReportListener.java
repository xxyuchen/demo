package com.geeker.event;

import com.geeker.config.RestUrlConfig;
import com.geeker.model.User;
import com.geeker.response.CamelResponse;
import com.geeker.utils.LoginUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
* @Author TangZhen
* @Date 2018/4/18 0018 13:21
* @Description  监听设备上报信息
*/
@Component
@Slf4j
public class ClientReportListener implements ApplicationListener<PublicEvent.ClientReportEvent> {

    @Resource
    private RestUrlConfig restUrlConfig;

    private RestTemplate template = new RestTemplate();

    @Override
    public void onApplicationEvent(PublicEvent.ClientReportEvent clientReportEvent) {
        User user = LoginUserUtil.getUser();
        Map<String,Object> map = new HashMap<>(5);
        map.put("comId",user.getCompanyId());
        map.put("parm",clientReportEvent.getUrl());
        map.put("deviceId",user.getDeviceId());
        try {
            template.postForObject(restUrlConfig.getUpLoadVocie(), map, CamelResponse.class);

        }catch (Exception e){
            log.info("ClientReportEvent:上报指令--上传音频--下发失败！",e);
        }
    }
}
