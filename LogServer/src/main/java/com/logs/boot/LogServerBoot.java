package com.logs.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @ProjectName: logdemo
 * @Package: com.logs.boot
 * @ClassName: LogServerBoot
 * @Author: dong
 * @Description: ${description}
 * @Date: 2019/9/4 22:27
 * @Version: 1.0
 */

@ComponentScan(basePackages = {"com.logs"})
@Configuration
@SpringBootApplication
public class LogServerBoot {

    @PostConstruct
    public void start() {

    }
}
