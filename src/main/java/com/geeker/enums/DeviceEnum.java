package com.geeker.enums;

/**
 * Created by Administrator on 2018/2/2 0002.
 */
public class DeviceEnum {

    /**
     * 任务状态
     */
    public enum deviceStatusEnum {
        UNBOUND(0, "未绑定"),
        BOUND(1, "已绑定"),
        NOTPASS(2, "认证不通过");


        private Integer code;
        private String value;

        deviceStatusEnum(int key, String value) {
            this.code = key;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static deviceStatusEnum valueOf(Integer code) {
            deviceStatusEnum[] enums = deviceStatusEnum.values();
            for (deviceStatusEnum e : enums) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
