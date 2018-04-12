package com.geeker.interceptor;

import com.alibaba.fastjson.JSON;
import com.geeker.model.User;
import com.geeker.response.ResponseUtils;
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
 * Created by Administrator on 2018/4/12 0012.
 */
@Slf4j
public class LoginVerifyIntercepor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    private String secret;

    private UserService userService;

    public LoginVerifyIntercepor(JwtTokenUtil jwtTokenUtil,String secret,UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.secret = secret;
        this.userService = userService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> cookieMap = CookieUtil.getCookies(request);
        String token = cookieMap.get("token");
        response.setContentType("application/json;charset=UTF-8");
        if (StringUtils.isEmpty(token)) {
            response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "请先登录！")));
            return false;
        } else {
            try {
                Jws<Claims> jws = jwtTokenUtil.parse(token,secret);
                String username = jwtTokenUtil.getUsernameFromToken(jws);
                log.info("checking authentication " + username);
                if (StringUtils.isBlank(username)) {
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "没有找到有效用户信息！")));
                    return false;
                }
                HttpSession session = request.getSession();
                User user = userService.getByLoginName(username);
                if(null == user){
                    response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "没有找到有效用户信息！")));
                }
                session.setAttribute(token,user);
            }catch (ExpiredJwtException e) {
                response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "Token已失效！")));
            } catch (JwtException e) {
                response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "Token 验证失败！")));
            } catch (Exception e) {
                response.getWriter().write(JSON.toJSONString(ResponseUtils.error(401, "Token验证服务异常！")));
            }
        }
        return true;
    }
}
