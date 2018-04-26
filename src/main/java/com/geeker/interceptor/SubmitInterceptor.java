package com.geeker.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.geeker.annotation.Submit;
import com.geeker.response.ResponseUtils;
import com.geeker.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @Author TangZhen
 * @Date 2018/1/31 0031 14:08
 * @Description 重复提交验证
 */
@Slf4j
public class SubmitInterceptor implements HandlerInterceptor {
    /**
     * 请求重复的标记，key是用户标识和url，value是标记
     */
    private Map<String, Boolean> duplicateRecordMap = new HashMap<>();

    private ScheduledExecutorService scheduledExecutorService =
            new ScheduledThreadPoolExecutor(100, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("duplicate-record");
                return t;
            });

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) o;
        if(!handlerMethod.getMethod().isAnnotationPresent(Submit.class)){
            return true;
        }

        String token = CookieUtil.getToken(request);
        if(StringUtils.isEmpty(token)){
            return true;
        }
        String path = request.getRequestURI();
        if (path == null) {
            return true;
        }
        String key = token+"_"+path;
        synchronized (key) {
            Boolean submit = duplicateRecordMap.get(key);
            if (BooleanUtils.isTrue(submit)) {
                log.debug("拦截key={}", key);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSONObject.toJSONString(ResponseUtils.error(500, "您的操作太快了！")));
                return false;
            } else {
                duplicateRecordMap.put(key, Boolean.TRUE);
                log.debug("记录key={}", key);
                scheduledExecutorService.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (key) {
                            duplicateRecordMap.remove(key);
                            log.info("超时移除key={}", key);
                        }
                    }
                }, 3, TimeUnit.SECONDS);
                return true;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) o;
        if(!handlerMethod.getMethod().isAnnotationPresent(Submit.class)){
            return;
        }
        String token = CookieUtil.getToken(request);
        if(StringUtils.isEmpty(token)){
            return;
        }
        String path = request.getRequestURI();
        if (path == null) {
            return;
        }
        String key = token+"_"+path;
        scheduledExecutorService.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (key) {
                    duplicateRecordMap.remove(key);
                    log.info("移除key={}", key);
                }
            }
        }, 1, TimeUnit.SECONDS);
    }
}
