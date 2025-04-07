package com.watoukuang.altxch.wallet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.chain168.altxch")
public class AltxchWalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(AltxchWalletApplication.class);
    }
}