package com.geeker.utils;

import java.util.Random;

/**
 * Created by Administrator on 2018/4/17 0017.
 */
public class SecureNumberUtil {

    public static String createSecureNumber(Integer custId){
        return "10100"+String.format("%011d",custId);
    }

    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();
        //length为几位密码
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static void main(String[] args){
        //System.out.print(String.format("%011d",1212));
        SecureNumberUtil test = new SecureNumberUtil();
        System.out.print(test.getStringRandom(6));
    }
}
