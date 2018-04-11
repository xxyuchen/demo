package com.geeker.model;

import java.util.Date;

public class User {
    private Integer id;

    private Integer companyId;

    private String loginName;

    private String loginPwd;

    private String userName;

    private String sex;

    private String mobile;

    private Boolean isMobileValid;

    private String email;

    private Boolean isEmailValid;

    private Date createTime;

    private Boolean status;

    private String qq;

    private Date lastLoginTime;

    private String weixinId;

    private String job;

    private String headName;

    private Date birthday;

    private Integer upperId;

    private Integer passCode;

    private Boolean isAdmin;

    private Boolean isAgreeDisclaimer;

    private Date agreeDisclaimerTime;

    private Date pwdUpdateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd == null ? null : loginPwd.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Boolean getIsMobileValid() {
        return isMobileValid;
    }

    public void setIsMobileValid(Boolean isMobileValid) {
        this.isMobileValid = isMobileValid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Boolean getIsEmailValid() {
        return isEmailValid;
    }

    public void setIsEmailValid(Boolean isEmailValid) {
        this.isEmailValid = isEmailValid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId == null ? null : weixinId.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName == null ? null : headName.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getUpperId() {
        return upperId;
    }

    public void setUpperId(Integer upperId) {
        this.upperId = upperId;
    }

    public Integer getPassCode() {
        return passCode;
    }

    public void setPassCode(Integer passCode) {
        this.passCode = passCode;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsAgreeDisclaimer() {
        return isAgreeDisclaimer;
    }

    public void setIsAgreeDisclaimer(Boolean isAgreeDisclaimer) {
        this.isAgreeDisclaimer = isAgreeDisclaimer;
    }

    public Date getAgreeDisclaimerTime() {
        return agreeDisclaimerTime;
    }

    public void setAgreeDisclaimerTime(Date agreeDisclaimerTime) {
        this.agreeDisclaimerTime = agreeDisclaimerTime;
    }

    public Date getPwdUpdateTime() {
        return pwdUpdateTime;
    }

    public void setPwdUpdateTime(Date pwdUpdateTime) {
        this.pwdUpdateTime = pwdUpdateTime;
    }
}