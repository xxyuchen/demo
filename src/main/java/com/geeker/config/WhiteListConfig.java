package com.geeker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18 0018.
 */
@Configuration
@ConfigurationProperties(prefix = "request.uri")
@Data
public class WhiteListConfig {

    private List<String> whiteList;
}
