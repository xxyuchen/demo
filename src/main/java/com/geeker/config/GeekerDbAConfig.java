package com.geeker.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
@Configuration
@MapperScan(basePackages = {"com.geeker.mapper.geeker"}, sqlSessionFactoryRef = "geekerSqlSessionFactory")
public class GeekerDbAConfig {

    @Bean(name = "geekerDataSource")
    @ConfigurationProperties(prefix = "geeker.datasource") // prefix值必须是application.properteis中对应属性的前缀
    public DataSource geekerDataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean
    public SqlSessionFactory geekerSqlSessionFactory(@Qualifier("geekerDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mapping/geeker/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate microSqlSessionTemplate(@Qualifier("geekerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory); // 使用上面配置的Factory
        return template;
    }
}
