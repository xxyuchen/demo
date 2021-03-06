package com.geeker.config;

import com.geeker.interceptor.LoginVerifyIntercepor;
import com.geeker.interceptor.SubmitInterceptor;
import com.geeker.service.OpDeviceService;
import com.geeker.service.UserService;
import com.geeker.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/12 0012.
 */
@Configuration
@Slf4j
public class InterceportConfig extends WebMvcConfigurerAdapter {

    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @Resource
    private TokenConfig tokenConfig;

    @Resource
    private UserService userService;

    @Resource
    private OpDeviceService opDeviceService;

    @Resource
    private RequestUriConfig requestUriConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginVerifyIntercepor(jwtTokenUtil,tokenConfig.getSecret(),userService,opDeviceService,requestUriConfig));
        registry.addInterceptor(new SubmitInterceptor());
        log.info("===========   拦截器注册完毕   ===========");
    }
}
