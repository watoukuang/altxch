package com.watoukuang.altxch.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AltxchGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AltxchGatewayApplication.class, args);
    }
}