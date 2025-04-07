package com.watoukuang.altxch.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "health")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping
    public String health() {
        return "altxch-exchange";
    }
}
