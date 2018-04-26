//Copyright (c) 2011-2012 by Hanghang Science and Technology Co., Ltd, All rights reserved.
//Xihu Technology Park No.206 Zhenhua Rd., Hangzhou 310031 China
//
//This software is the confidential and proprietary information of 
//hzhanghang.com, Inc. ("Confidential Information"). You shall not disclose 
//such Confidential Information and shall use it only in accordance 
//with the terms of the license agreement you entered into with hzhanghang.com, Inc.
package com.geeker.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * CookieUtil 工具类
 *
 * @author xuzao
 */
public class CookieUtil {

	/**
	 * 设置客户端Cookie值,redirect方式跳转则无效
	 * 
	 * @param response 请求对象
	 * @param name Cookie名称
	 * @param value Cookie值
	 */
	public static void addCookie(HttpServletResponse response, String name, String value) {
        addCookie(response, name, value, -1);
	}
    
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        /**
         *  该Cookie失效的时间，单位秒。
         *  如果为正数，则该Cookie在maxAge秒之后失效。
         *  如果为负数，该Cookie为临时Cookie，关闭浏览器即失效，浏览器也不会以任何形式保存该Cookie。
         *  如果为0，表示删除该Cookie。默认为-1
         */
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

	/**
	 * Cookie的MAP对象
	 * 
	 * @param request 请求对象
	 * 
	 * @return MAP对象
	 */
	public static Map<String, String> getCookies(HttpServletRequest request) {
		Map<String, String> cookies = new HashMap<String, String>();
		Cookie[] array = request.getCookies();
        if(null != array){
            int size = array.length;
            for (int i = 0; i < size; i++) {
                Cookie cookie = array[i];
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }

		return cookies;
	}

	/**
	 * 删除客户端Cookie值,redirect方式跳转则无效
	 * 
	 * @param response 请求对象
	 * @param name Cookie名称
	 */
	public static void removeCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/**
	 * 获取token
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request){
		String token = request.getHeader("token");
		if(StringUtils.isEmpty(token)){
			Map<String, String> cookieMap = getCookies(request);
			token = cookieMap.get("token");
		}
		return token;
	}

}
