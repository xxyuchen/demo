package com.geeker.model;

import lombok.Data;

import java.util.Date;
@Data
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

}