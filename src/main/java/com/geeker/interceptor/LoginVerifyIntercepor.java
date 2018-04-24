package com.geeker.interceptor;

import com.alibaba.fastjson.JSON;
import com.geeker.config.RequestUriConfig;
import com.geeker.model.OpDevice;
import com.geeker.model.User;
import com.geeker.response.ResponseUtils;
import com.geeker.service.OpDeviceService;
import com.geeker.service.UserService;
import com.geeker.utils.CookieUtil;
import com.geeker.utils.DateUtils;
import com.geeker.utils.JwtTokenUtil;
import com.geeker.utils.Md5Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @Author TangZhen
* @Date 2018/4/13 0013 09:22
* @Description  token解析
*/
@Slf4j
public class LoginVerifyIntercepor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    private String secret;

    private UserService userService;

    private OpDeviceService opDeviceService;

    private RequestUriConfig requestUriConfig;

    public LoginVerifyIntercepor(JwtTokenUtil jwtTokenUtil,String secret,UserService userService,
                                 OpDeviceService opDeviceService,RequestUriConfig requestUriConfig) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.secret = secret;
        this.userService = userService;
        this.opDeviceService = opDeviceService;
        this.requestUriConfig = requestUriConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String token;
        if(requestUriConfig.getWhiteList().contains(path)){
            return true;
        }
        token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            Map<String, String> cookieMap = CookieUtil.getCookies(request);
            token = cookieMap.get("token");
        }
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession();
        log.info("token:【{}】",token);
        if (StringUtils.isEmpty(token)) {
            response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "请先登录！")));
            return false;
        } else {
            try {
                if(path.equals(requestUriConfig.getLoginGeeker())){//登录数聚客
                    String deviceId = request.getParameter("deviceId");
                    Long timestamp = Long.valueOf(request.getHeader("timestamp"));
                    if(Md5Util.myEncrypt32(deviceId+timestamp,requestUriConfig.getTokenKey()).equals(token)){
                        return true;
                    }
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "登录token无效！")));
                    return false;
                }else {
                    Jws<Claims> jws = jwtTokenUtil.parse(token,secret);
                    String username = jwtTokenUtil.getUsernameFromToken(jws);
                    log.info("checking authentication " + username);
                    if (StringUtils.isBlank(username)) {
                        response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "没有找到有效用户信息！")));
                        return false;
                    }
                    User user = userService.getByLoginName(username);
                    if(null == user){
                        response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "没有找到有效用户信息！")));
                        return false;
                    }
                    OpDevice opDevice = opDeviceService.selectByBoundUserId(user.getId());
                    if(null != opDevice){
                        user.setDeviceId(opDevice.getId());
                        /*response.getWriter().write(JSON.toJSONString(ResponseUtils.error(500, "未查询到绑定设备！")));
                        return false;*/
                    }
                    session.setAttribute("userSession",user);
                }
            }catch (ExpiredJwtException e) {
                log.error("token验证异常",e);
                response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "Token已失效！")));
                return false;
            } catch (JwtException e) {
                log.error("token验证异常",e);
                response.getWriter().write(JSON.toJSONString(ResponseUtils.error(500, "Token 验证失败！")));
                return false;
            } catch (Exception e) {
                log.error("token验证异常",e);
                response.getWriter().write(JSON.toJSONString(ResponseUtils.error(500, "Token验证服务异常！")));
                return false;
            }
        }
        return true;
    }
}
