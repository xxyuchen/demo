package com.geeker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
* @Author TangZhen
* @Date 2018/4/12 0012 10:50
* @Description  token解析参数
*/
@Data
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class TokenConfig {
    private String header;
    private String secret;
    private Integer expiration;
    private Integer autoRefreshIn;
    private String tokenHead;
    private String cookieName;
    private boolean writeCookie;
    private boolean writeHeader;
}
