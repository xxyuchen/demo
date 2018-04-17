package com.geeker.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysAuditRecord {
    private Integer id;

    private Integer userId;

    private String ip;

    private Integer platform;

    private String loginName;

    private String userName;

    private String funcName;

    private String params;

    private String resultData;

    private Date createTime;

}