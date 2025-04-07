package com.watoukuang.altxch.exchange;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.watoukuang.altxch.*.dao.mapper")
@EnableMongoRepositories("com.watoukuang.altxch.core.dao.repository")
@SpringBootApplication(scanBasePackages = "com.watoukuang")
public class AltxchExchangeApplication {
    public static void main(String[] args) {
        SpringApplication.run(AltxchExchangeApplication.class);
    }
}