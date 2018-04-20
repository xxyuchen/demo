package com.geeker.vo;

import com.geeker.model.OpDevice;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/4/18 0018.
 */
@Data
public class OpDeviceRegisterVo extends OpDevice {

    @NotNull(message = "注册码不能为空！")
    private String registerCode;
}
