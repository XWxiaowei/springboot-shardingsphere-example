package com.jay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.jay.mapper"})
public class SpringbootShardingsphereExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShardingsphereExampleApplication.class, args);
    }

}
