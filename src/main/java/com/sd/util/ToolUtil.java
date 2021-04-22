package com.sd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;
import com.sd.modules.system.entity.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Chen Hualiang
 * @create 2020-10-10 10:27
 */
public class ToolUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(ToolUtil.class);

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     * 使用MD5散列 不迭代
     */
    public static void entryptPassword(User user) {
//        byte[] salt = Digests.generateSalt(Constants.SALT_SIZE);
//        user.setSalt(Encodes.encodeHex(salt));
        byte[] hashPassword = Digests.md5(user.getPassword().getBytes());
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

    /**
     *
     * @param paramStr 输入需要加密的字符串
     * @return
     */
    public static String entryptPassword(String paramStr,String salt) {
        if(StringUtils.isNotEmpty(paramStr)){
            byte[] saltStr = Encodes.decodeHex(salt);
            byte[] hashPassword = Digests.sha1(paramStr.getBytes(), saltStr, Constants.HASH_INTERATIONS);
            String password = Encodes.encodeHex(hashPassword);
            return password;
        }else{
            return null;
        }

    }

    /**
     * 获取客户端的ip信息
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        LOGGER.info("ipadd : " + ip);
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        LOGGER.info(" ip --> " + ip);
        return ip;
    }

    /**
     * 判断请求是否是ajax请求
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request){
        String accept = request.getHeader("accept");
        return accept != null && accept.contains("application/json") || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest"));
    }

    /**
     * 获取操作系统,浏览器及浏览器版本信息
     * @param request
     * @return
     */
    public static Map<String,String> getOsAndBrowserInfo(HttpServletRequest request){
        Map<String,String> map = Maps.newHashMap();
        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS Info=======================
        if (userAgent.toLowerCase().contains("windows"))
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().contains("mac"))
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().contains("x11"))
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().contains("android"))
        {
            os = "Android";
        } else if(userAgent.toLowerCase().contains("iphone"))
        {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        //===============Browser===========================
        if (user.contains("edge"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        +"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }

        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  ||
                (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) ||
                (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) )
        {
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser="IE" + IEVersion.substring(0,IEVersion.length() - 1);
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        map.put("os",os);
        map.put("browser",browser);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Object> getAddressByIP(String ip) {
        LOGGER.info("根据ip获取地址----getAddressByIP----0");
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip = "0.0.0.0";
        }
        // Map<String,Object> map = Maps.newHashMap();
        //StringBuilder sb = new StringBuilder("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=");
        StringBuilder sb = new StringBuilder("http://ip.taobao.com/service/getIpInfo.php?ip=");
        sb.append(ip);
        LOGGER.info("根据ip获取地址----getAddressByIP----1");
        String result= HttpUtil.get(sb.toString());
        LOGGER.info("根据ip获取地址----getAddressByIP----2");
        LOGGER.info(result);
        Map<String,Object> resultMap = JSONUtil.toBean(result,Map.class);
        return resultMap;
    }

    @SuppressWarnings("unchecked")
    public static Map<String,Object> getAddressFromAliyunByIP(String ip) {
        LOGGER.info("阿里云接口根据ip获取地址----getAddressByIP----0");
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip = "0.0.0.0";
        }
        // Map<String,Object> map = Maps.newHashMap();

        String host = "https://ipquery.market.alicloudapi.com";
        String path = "/query";
        String method = "GET";
        String appcode = "ab58659aca0b48abb233551479f839a9";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("ip", ip);


        try {


            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            Map<String,Object> resultMap = JSONUtil.toBean(EntityUtils.toString(response.getEntity()),Map.class);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getToday(){
        String res = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 8){
            res = df.format(cal.getTime());
        }else {
            cal.add(Calendar.DATE,-1);
            res = df.format(cal.getTime());
        }
        return res;
    }

    public static double getDutyHoursNow(Date tempTime) {
        //系统当前时间
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Date nowTime = cal.getTime();

        //计算日期差
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(tempTime);
        endCal.setTime(nowTime);
        double hours = ((double) (endCal.getTime().getTime()/1000)-(double) (startCal.getTime().getTime()/1000))/3600;

        return hours;
    }

    /**
     * 获取值班截止时间 第二天8点
     * @param time
     * @return
     */
    public static Date getDutyEndTime(Date time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd 08:00");
        Date beginTime = time;
        Date endTime = null;
        try {
            endTime = df.parse("9999-9-9 9:9");
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginTime);
            if (cal.get(Calendar.HOUR_OF_DAY) >= 8){
                cal.add(Calendar.DAY_OF_YEAR,1);
                endTime = cal.getTime();
                endTime = df1.parse(df.format(endTime));
            }else {
                endTime = df1.parse(df.format(endTime));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return endTime;
    }

    /**
     * 返回工作日集合，只排除周末
     * @param year
     * @param month
     * @return
     */
    public static List<Date> getWorkDays(int year, int month){
        // 用于储存每月工作日
        List<Date> dates = new ArrayList<Date>();

        Calendar cal = Calendar.getInstance();
        //设置月份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 设置为当月第一天
        cal.set(Calendar.DATE, 1);

        while(cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month){
            // 判断当前天为本周的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // 如果不为周六或者周天,将日期进行储存
            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){
                dates.add((Date)cal.getTime().clone());
            }
            // 将当前日期增加一天
            cal.add(Calendar.DATE, 1);
        }
        // 返回当前月工作日集合
        return dates;

    }

    /**
     * 返回工作日集合，只排除周末
     * @param year
     * @param month
     * @return
     */
    public static List<Date> getHoildays(int year, int month){
        // 用于储存每月工作日
        List<Date> dates = new ArrayList<Date>();

        Calendar cal = Calendar.getInstance();
        //设置月份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 设置为当月第一天
        cal.set(Calendar.DATE, 1);

        while(cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) < month){
            // 判断当前天为本周的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // 如果为周六或者周天,将日期进行储存
            if(day == Calendar.SUNDAY || day == Calendar.SATURDAY){
                dates.add((Date)cal.getTime().clone());
            }
            // 将当前日期增加一天
            cal.add(Calendar.DATE, 1);
        }
        // 返回当前月工作日集合
        return dates;

    }

    /**
     *
     * @param dateList
     * @return 返回日期字符串集合
     */
    public static List<String> getDateString(List<Date> dateList){
        // 储存日期字符串
        List<String> dateString = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Date date : dateList){
            String date2 = simpleDateFormat.format(date);
            dateString.add(date2);

        }
        return dateString;
    }


}
