package com.imooc.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = "com.imooc.mapper")
public class DevServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevServiceApplication.class, args);
    }

}
