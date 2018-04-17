package com.geeker.utils;

import com.geeker.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 当前登录用户的工具类
 *
 * @author xuzao
 * @see
 */
public class LoginUserUtil {

    /**
     * 当前登录用户对象
     * (可以根据下面返回的userid)
     *
     * @return
     */
    public static User getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = CookieUtil.getCookies(request).get("token");
        if(StringUtils.isEmpty(token)){
            token = CookieUtil.getCookies(request).get("market");
        }
        return (User) request.getSession().getAttribute(token);
    }



    /**
     * 当前登录用户,取用户id
     */
    public static Integer getUserId() {
        User user = getUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}
