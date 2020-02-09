package com.logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: logdemo
 * @Package: com.logs
 * @ClassName: LogServer
 * @Author: dong
 * @Description: ${description}
 * @Date: 2019/9/4 22:24
 * @Version: 1.0
 */

@SpringBootApplication
public class LogServer {
    public static void main(String[] args) {
        SpringApplication.run(LogServer.class, args);
    }
}
