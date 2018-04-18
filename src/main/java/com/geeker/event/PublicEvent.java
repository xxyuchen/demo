package com.geeker.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
* @Author TangZhen
* @Date 2018/3/28 0028 16:40
* @Description  事件
*/
@Component
public class PublicEvent {

    //手机上报指令
    public static class ClientReportEvent extends ApplicationEvent {
        @Getter
        private String url;

        public ClientReportEvent(Object source,String url) {
            super(source);
            this.url = url;
        }
    }
}
