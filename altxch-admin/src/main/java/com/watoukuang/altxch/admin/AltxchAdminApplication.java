package com.watoukuang.altxch.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@MapperScan("com.watoukuang.altxch.*.dao.mapper")
@EnableMongoRepositories("com.watoukuang.altxch.core.dao.repository")
@SpringBootApplication(scanBasePackages = "com.watoukuang")
public class AltxchAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AltxchAdminApplication.class);
    }
}