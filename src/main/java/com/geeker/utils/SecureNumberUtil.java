package com.geeker.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018/4/17 0017.
 */
public class SecureNumberUtil {

    public static String createSecureNumber(Integer custId){
        return "10100"+String.format("%011d",custId);
    }

    public static void main(String[] args){
        System.out.print(String.format("%011d",1212));
    }
}
