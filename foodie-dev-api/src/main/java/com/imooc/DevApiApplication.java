package com.imooc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = "com.imooc.mapper")
public class DevApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevApiApplication.class, args);
    }

}
