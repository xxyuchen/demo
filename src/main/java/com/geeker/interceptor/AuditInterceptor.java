package com.geeker.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.geeker.annotation.AuditCut;
import com.geeker.model.SysAuditRecord;
import com.geeker.model.User;
import com.geeker.service.SysAuditRecordService;
import com.geeker.utils.LoginUserUtil;
import com.geeker.utils.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
* @Author TangZhen
* @Date 2018/4/13 0013 10:05
* @Description  审计记录
*/
@Component
@Aspect
public class AuditInterceptor {
    @Resource
    private SysAuditRecordService sysAuditRecordService;

    @Pointcut("@annotation(com.geeker.annotation.AuditCut)")
    public void controllerPointCut() {
    }
    @Before("controllerPointCut() && @annotation(auditCut)")
    public void controller(JoinPoint pjp, AuditCut auditCut) throws Throwable {
        User user= LoginUserUtil.getUser();
        String ip= WebUtils.getIp();
        Object[] objects=pjp.getArgs();
        //操作平台
        int platform=auditCut.platform();
        //功能名称
        String funName=auditCut.funcName();
        //当前时间
        Date date=new Date();
        SysAuditRecord sysAuditRecord=new SysAuditRecord();
        sysAuditRecord.setCreateTime(date);
        sysAuditRecord.setFuncName(funName);
        sysAuditRecord.setPlatform(platform);
        sysAuditRecord.setIp(ip);
        if (objects.length > 0) {
            if(objects.length >2000){
                sysAuditRecord.setParams(JSONObject.toJSONString(pjp.getArgs()).substring(1,700));
            }else {
                sysAuditRecord.setParams(JSONObject.toJSONString(pjp.getArgs()));
            }
        }
        if(user!=null) {
            sysAuditRecord.setLoginName(user.getLoginName());
            sysAuditRecord.setUserName(user.getUserName());
            sysAuditRecord.setUserId(user.getId());
        }
        sysAuditRecordService.insert(sysAuditRecord);
    }

}
