package com.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;
/**
 * @ClassName: DataViewApplication
 * @Description:
 * @Author: xiedong
 * @Date: 2020/2/10 13:02
 */
@SpringBootApplication
@MapperScan("com.log.mapper")
public class DataViewApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataViewApplication.class,args);
    }
}
