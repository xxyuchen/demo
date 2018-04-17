package com.geeker.interceptor;

import com.alibaba.fastjson.JSON;
import com.geeker.model.OpDevice;
import com.geeker.model.User;
import com.geeker.response.ResponseUtils;
import com.geeker.service.OpDeviceService;
import com.geeker.service.UserService;
import com.geeker.utils.CookieUtil;
import com.geeker.utils.JwtTokenUtil;
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
import java.util.Map;

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

    public LoginVerifyIntercepor(JwtTokenUtil jwtTokenUtil,String secret,UserService userService,OpDeviceService opDeviceService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.secret = secret;
        this.userService = userService;
        this.opDeviceService = opDeviceService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> cookieMap = CookieUtil.getCookies(request);
        String token = cookieMap.get("token");
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession();
        if (StringUtils.isEmpty(token)) {
            String market = cookieMap.get("market");
            if(StringUtils.isEmpty(market)){
                response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "请先登录！")));
                return false;
            }else {
                String[] markets = market.split(":");
                Integer userId = Integer.valueOf(markets[0]);
                String deviceId = markets[1];
                OpDevice opDevice = opDeviceService.selectByPrimaryKey(deviceId);
                if(null == opDevice){
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(500, "设备不存在！")));
                    return false;
                }
                if(null == opDevice.getBoundUserId()){
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(500, "该设备未绑定任何用户！")));
                    return false;
                }
                User user = userService.getById(opDevice.getBoundUserId());
                if(null == user){
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(500, "绑定用户不存在！")));
                    return false;
                }
                if(null==user){
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "没有找到有效用户信息！")));
                    return false;
                }
                user.setDeviceId(deviceId);
                session.setAttribute(market,user);
            }
        } else {
            try {
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
                if(null == opDevice){
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(500, "未查询到绑定设备！")));
                    return false;
                }
                user.setDeviceId(opDevice.getId());
                session.setAttribute(token,user);
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
