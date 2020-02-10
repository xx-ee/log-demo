package com.log.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class DateUtil
{
    public static String PATTERN_YYYYMM = "yyyyMM";
    public static String PATTERN_YYYYMMDD_ = "yyyy/MM/dd";
    public static String PATTERN_HHMMSS = "HHmmss";
    public static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String PATTERN_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static String PATTERN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static String PATTERN_HTTP = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static String PATTERN_YYYYMMDD_CHINESE = "yyyy年MM月dd日";
    public static String PATTERN_YYYYMMDD_HHMMSS_CHINESE = "yyyy年MM月dd日 HH:mm:ss";
    public static String PATTERN_YYYYMMDD_SLASH = "yyyy/MM/dd";
    public static String PATTERN_YYYYMMDD_HHMMSS_SLASH = "yyyy/MM/dd  HH:mm:ss";
    public static String PATTERN_YYYY_MM_DD_00 = "yyyy-MM-dd 00:00:00";

    public static String UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";


    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern)
    {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null)
        {
            synchronized (lockObj)
            {
                tl = sdfMap.get(pattern);
                if (tl == null)
                {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>()
                    {

                        @Override
                        protected SimpleDateFormat initialValue()
                        {
                            //System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 时间格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern)
    {
        return getSdf(pattern).format(date);
    }

    /**
     * 时间反格式化
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parse(String dateStr, String pattern)
    {
        Date date = null;
        try
        {
            date = getSdf(pattern).parse(dateStr);
        }
        catch (Exception e)
        {

        }
        return date;
    }

    /**
     * 本地时间转 UTC 时间字符串
     *
     * @param date
     * @return
     */
    public static String localToUtcString(Date date, String pattern)
    {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * UTC 时间反格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date utcStringToUtcDate(String date, String pattern)
    {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try
        {
            utcDate = sdf.parse(date);
        }
        catch (Exception e)
        {

        }
        return utcDate;
    }

    /**
     * UTC 时间格式化
     *
     * @param date
     * @return
     */
    public static String utcDateToUtcString(Date date)
    {
        SimpleDateFormat sdf = getSdf("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }


    /**
     * UTC 时间字符串转本地时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date utcStringToLocalDate(String date, String pattern)
    {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date localDate = null;
        try
        {
            localDate = sdf.parse(date);
        }
        catch (Exception e)
        {

        }
        return localDate;
    }

    /**
     * 获取本周一的日期
     *
     * @return
     */
    public static String getMonday()
    {
        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1)
        {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        return DateUtil.format(mondayDate, PATTERN_YYYY_MM_DD);

    }

    /**
     * 获取本周日的日期
     *
     * @return
     */
    public static String getSunday()
    {
        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1)
        {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        return DateUtil.format(sundayDate, PATTERN_YYYY_MM_DD);
    }

    /**
     * 获取之间差
     * @param bigDay
     * @param smallDay
     * @return
     */
    public static Long getDaysBetween (String bigDay, String smallDay) {
        long big = parse(bigDay, PATTERN_YYYYMMDD).getTime();
        long small = parse(smallDay, PATTERN_YYYYMMDD).getTime();
        if (big >= small) {
            return (big - small + 1000000)/(60*60*24*1000);
        } else {
            return -(small - big + 1000000)/(60*60*24*1000);
        }
    }

    /**
     * 获取之间差 天数为单位Byday
     * @param bigDay
     * @param smallDay
     * @return
     */
    public static Long getDaysBetweenByDay (String bigDay, String smallDay) {
        long big = parse(bigDay, PATTERN_YYYY_MM_DD).getTime();
        long small = parse(smallDay, PATTERN_YYYY_MM_DD).getTime();
        if (big >= small) {
            return (big - small)/(60*60*24*1000);
        } else {
            return -(small - big)/(60*60*24*1000);
        }
    }

    /**
     * 获取年月日时分秒毫秒值
     *
     * @param date  日期
     * @return
     */
    public static String getYyyyMMddHHmmssSSS(Date date)
    {
        //格式化日期
        String str = format(date, PATTERN_YYYYMMDDHHMMSSSSS);
        return str;
    }
    /**
     * 获取年月日时分秒
     *
     * @param date  日期
     * @return
     */
    public static String getYyyyMMddHHmmss(Date date)
    {
        //格式化日期
        String str = format(date, PATTERN_YYYYMMDDHHMMSS);
        return str;
    }
    /**
     * 获取时分秒
     *
     * @param date  日期
     * @return
     */
    public static String getHHmmss(Date date)
    {
        //格式化日期
        String str = format(date, PATTERN_HHMMSS);
        return str;
    }

    /**
     * 获取某月的最后一天
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static String getLastDayOfMonth(int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        String day = format(cal.getTime(), PATTERN_YYYY_MM_DD);
        return day;
    }

    /**
     * 获取指定区间内随机时间
     *
     * @param beginDate
     * @param endDate
     * @param pattern
     * @return
     */
    public static Long timeSpan(String beginDate, String endDate, String pattern)
    {
        try
        {
            Date start = parse(beginDate, pattern);
            Date end = parse(endDate, pattern);
            if (start.getTime() >= end.getTime())
            {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return end.getTime()-start.getTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static Date todayFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date todayLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取指定区间内随机时间
     *
     * @param beginDate
     * @param endDate
     * @param pattern
     * @return
     */
    public static Date randomDate(String beginDate, String endDate, String pattern)
    {
        try
        {
            Date start = parse(beginDate, pattern);
            Date end = parse(endDate, pattern);
            return randomDate(start,end);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    //PATTERN_YYYYMMDD
    /**
     * 获取年月日
     *
     * @param date  日期
     * @return
     */
    public static String getYYYYMMDD(Date date)
    {
        //格式化日期
        String str = format(date, PATTERN_YYYYMMDD);
        return str;
    }

    /**
     * 获取年月日
     *
     * @param date  日期
     * @return
     */
    public static String getYYYYMMDD_(Date date)
    {
        //格式化日期 yyyy/MM/dd
        String str = format(date, PATTERN_YYYYMMDD_);
        return str;
    }
    /**
     * 获取年-月-日
     *
     * @param date  日期
     * @return
     */
    public static String getYYYY_MM_DD(Date date)
    {
        //格式化日期
        String str = format(date, PATTERN_YYYY_MM_DD);
        return str;
    }
    /**
     * 判断当前日期是否是周i+1
     * @return
     */
    public static Boolean isX(int i)
    {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)==i?true:false;
    }
    /**
     * 获取某天日期的前N天日期
     * 格式：YYYYMMDD
     * @return
     */
    public static String getDateBeforeYYYYMMDD(Date date,int day)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,day);
        return getYYYYMMDD(c.getTime());
    }
    /**
     * 获取指定区间内随机时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Date randomDate(Date beginDate, Date endDate)
    {
        try
        {
            if (beginDate.getTime() >= endDate.getTime())
            {
                return null;
            }
            long date = random(beginDate.getTime(), endDate.getTime());
            return new Date(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static long random(long begin, long end)
    {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end)
        {
            return random(begin, end);
        }
        return rtn;
    }

    /**
     * 判断当前时间距离第二天凌晨0点的秒数（当天凌晨12点）
     *
     * @return 返回值单位为[s:秒]
     */
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    /**
     * 判断当前时间距离第二天上午8点的秒数
     *
     * @return 返回值单位为[s:秒]
     */
    public static Long getSecondsNextDayEightOclock() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

    public static String getYyyy_MM_DD_Hh_Mm_Ss(Date date)
    {
        DateFormat instance = SimpleDateFormat.getDateTimeInstance();
        return instance.format(date);
    }

    public static int getCurrentYearDays(){
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
            return 366;
        }
        return 365;
    }
    public static Date getCurrentTimeByU(Integer day,Integer hour,Integer minute,Integer second){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,day);
        instance.set(Calendar.HOUR_OF_DAY,hour);
        instance.set(Calendar.SECOND,minute);
        instance.set(Calendar.MINUTE,second);
        return instance.getTime();
    }


    public static void main(String[] args) throws Exception
    {
//        String s = localToUtcString(new Date(), UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);
//        System.err.println(s);
//
//        Date date = utcStringToLocalDate(s, UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);
//
//        System.err.println(format(date, PATTERN_YYYY_MM_DD_HHMMSS));
//
//        System.err.println("--------------------------------");
//        String da = localToUtcString(new Date(), UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);
//        System.err.println(da);
//        Date utcDate = utcStringToUtcDate(da, UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);
//        System.err.println(utcDateToUtcString(utcDate));
//        System.err.println(getSunday());
//        System.out.println(getDateBefore(new Date(),-3));
//        System.out.println("=====");
//        System.err.println(parse("20170808112758", PATTERN_YYYYMMDDHHMMSS));
//        System.out.println("=====");
//        System.out.println(getSecondsNextEarlyMorning());
//        System.out.println(getYyyyMMddHHmmss(new Date()));

//        Date parse = parse("20190822", PATTERN_YYYYMMDD);
//        Date parse1 = parse("20190823", PATTERN_YYYYMMDD);
//
//        System.out.println(parse);
//        System.out.println(parse1);
//        DateFormat timeInstance = SimpleDateFormat.getDateTimeInstance();
//        String format = timeInstance.format(new Date());
//        System.out.println(getHHmmss(new Date()));
//        Long daysBetween = getDaysBetween("20191011", "20191010");
//        System.out.println(daysBetween);

//        String test1="abcdefghijklmnopnrstuvwxyz";
//        System.out.println(test1.substring(8,16));
//        Long daysBetween = getDaysBetween("2019-12-30", "2019-11-01");
//        System.out.println(String.valueOf(daysBetween));
//        Long daysBetween1 = getDaysBetweenByDay("2019-12-30", "2019-11-01");
//        System.out.println(String.valueOf(daysBetween1));
//        System.out.println(String.valueOf(daysBetween1/30));
//        System.out.println(String.valueOf(daysBetween1%30));

//        System.out.println(getCurrentTimeByU(21,0,0));
//        System.out.println(getHHmmss(getCurrentTimeByU(21,0,0)));
//        String s1= "92339883MA2E6Q0141";
//        String s1= "923398832511661791";
//        //25116617-9
//        String orgCd = s1.substring(8, 16) + "-" + s1.substring(16, 17);
//        System.out.println(orgCd);


//        String a="20200102";
//        System.out.println(parse(getAfterDays(a,-1),PATTERN_YYYYMMDD));
//        Calendar instance = Calendar.getInstance();
////        instance.add(Calendar.,0);
//        instance.set(Calendar.HOUR_OF_DAY,21);
//
//        Date time = instance.getTime();
//
//        System.out.println(time);


        System.out.println(todayFirstDate());
        System.out.println(todayLastDate());
        System.out.println(getYyyy_MM_DD_Hh_Mm_Ss(new Date()).split(" ")[1]);
    }

    /**
     * 获取某一天之后的日期
     * @param dateStr 某一天
     * @param num 后几天
     * @return
     */
    public static String getAfterDays(String dateStr, int num) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + num);
        String dayAfter = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return dayAfter;
    }

    public static Integer differentDays(Date sourceDate,Date toDate){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(sourceDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(toDate);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        if (year1!=year2){
            int timeDistance=0;
            for (int i=year1;i<year2;i++){
                if (i%4==0&&i%100!=0||i%400==0){
                    timeDistance+=366;
                }else {
                    timeDistance+=365;
                }
            }
            return timeDistance+(day2-day1);
        }else {
            return day2-day1;
        }
    }
}
