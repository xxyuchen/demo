package com.geeker.utils;
import java.security.MessageDigest;
import java.util.Date;

/**
* @Author TangZhen
* @Date 2018/4/18 0018 09:38
* @Description  Md5加密工具
*/
public class Md5Util {

    /**
     * md5 32位加密
     * @param encryptStr
     * @return
     */
    public static String encrypt32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16){
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

    /**
     *   密码 盐值加密
     * @param encryptStr
     * @return
     */
    public static String myEncrypt32(String encryptStr,String salt) {
        MessageDigest md5;
        try {
            encryptStr=encryptStr + salt;
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16){
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

    /**
     * @Description:加密-16位小写
     * @author:liuyc
     * @time:2016年5月23日 上午11:15:33
     */
    public static String encrypt16(String encryptStr) {
        return encrypt32(encryptStr).substring(8, 24);
    }

    public static void main(String[] args) {
        Long str=System.currentTimeMillis();
        Long dateTime =(new Date()).getTime();
        System.out.println(dateTime);
        System.out.println(Md5Util.myEncrypt32("cr6Em1LMZEfd1okkRP1p0naBCdl2FmjE",dateTime+"4c116ee17d14"));
    }

}
