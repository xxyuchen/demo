package com.geeker.model;

import lombok.Data;

import java.util.Date;

@Data
public class OpDevice {
    private String id;

    private String mobile;

    private String imsi;

    private String imei;

    private String model;

    private String cpu;

    private String ram;

    private String mem;

    private String networkMode;

    private String remark;

    private Integer sysId;

    private Integer comId;

    private Integer groupId;

    private Date boundTime;

    private Integer boundUserId;

    private Integer status;
}