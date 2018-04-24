package com.geeker.vo;

import com.geeker.model.OpDevice;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Data
public class OpDeviceVo extends OpDevice {

    private Integer pageSize;

    private Integer pageIndex;

    private String userName;

    private String departName;
}
