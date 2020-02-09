package com.logs.example;

/**
 * @ProjectName: logdemo
 * @Package: com.logs.boot
 * @ClassName: test
 * @Author: dong
 * @Description: 测试类
 * @Date: 2019/9/7 15:10
 * @Version: 1.0
 */
public class test {
    public static void main(String[] args) {
        String testm="01:19:14.936 INFO  com.logs.controller.LogController - (LogController.java:43) - http://localhost:8080/appserver/a.jsp";
        String[] split = testm.split("-");
        for (String s : split) {
            System.out.println(s);
        }
        System.out.println(split[2]);
    }
}
