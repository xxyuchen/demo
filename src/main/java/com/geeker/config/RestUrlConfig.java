package com.geeker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @Author TangZhen
* @Date 2018/4/16 0016 09:39
* @Description  微服务接口配置
*/
@Component
@ConfigurationProperties(prefix = "market.url")
@Data
public class RestUrlConfig {
    private String bound;

    private String removeBound;

    private String call;

    private String mobileDecodeUrl;

    private String phoneBook;

    private String group;

    private String sendSms;

    private String uploadFileUrl;

    private String localVoicePath;

    private String upLoadVocie;

}
