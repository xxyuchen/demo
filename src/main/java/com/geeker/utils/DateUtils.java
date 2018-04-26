package com.geeker.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/2/1 0001.
 */
@Slf4j
public class DateUtils {
   /**
     * Date类型转换成XMLGregorianCalendar类型
     *
     * @param date
     * @return
    * */
    /*public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        }
        catch (Exception e) {
            logger.error("Date类型转换成XMLGregorianCalendar类型出错："+e);
        }
        return gc;
    }*/

    /***
     * XMLGregorianCalendar类型转换成Date类型
     *
     * @param cal
     * @return
     * @throws Exception
     */
    /*public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }*/

    /**
     * String 转 Date
     * 2015年3月25日上午11:27:14
     * auther:shijing
     * @param str 日期字符串
     * @param format 转换格式
     * @return
     * Date
     */
    public static Date stringToDate(String str, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(str);
        }
        catch (ParseException e) {
            log.error("String类型 转 Date类型出错："+e);
        }
        return date;
    }

    /**
     * Date 转 String
     * auther: shijing
     * 2015年3月25日上午11:28:14
     * @param date 日期
     * @param format 转换格式
     * @return
     */
    public static String dateToString(Date date,String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        String strDate=null;
        try {
            if(date!=null){
                strDate=dateFormat.format(date);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Date类型 转 String类型出错："+e);
        }
        return strDate;
    }

    /**
     * 获得2个时间相减的分钟数
     *
     * @return
     */
    public static int getTwoDaySubByMinute(Date start, Date end) {
        long second = (end.getTime() - start.getTime()) / (1000);
        long min = second / 60;
        int day = (int) min;
        return day;
    }

    /**
     * 与当前时间相减的分钟数
     *
     * @return
     */
    public static int getNowByMinute(Long date) {
        Date now = new Date();
        long second = (now.getTime() - date) / (1000);
        long min = second / 60;
        int day = (int) min;
        return day;
    }

    public static Date getDateByLong(Long date){
        if(null==date){
            return null;
        }
        return new Date(date);
    }
}
