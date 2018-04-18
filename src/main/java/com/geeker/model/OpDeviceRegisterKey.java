package com.geeker.model;

public class OpDeviceRegisterKey {
    private Integer id;

    private String registerCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode == null ? null : registerCode.trim();
    }
}