package com.geeker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
@Configuration
public class DataSourceConfig {
    @Bean(name = "geeker")
    @ConfigurationProperties(prefix = "geeker.datasource")
    public DataSource geekerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "micro")
    @ConfigurationProperties(prefix = "micro.datasource")
    public DataSource microDataSource() {
        return DataSourceBuilder.create().build();
    }


}
