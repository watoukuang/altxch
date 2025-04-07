package com.watoukuang.altxch.match;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.watoukuang.altxch.*.dao")
@SpringBootApplication(scanBasePackages = "com.watoukuang")
public class AltxchMatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(AltxchMatchApplication.class);
    }
}
