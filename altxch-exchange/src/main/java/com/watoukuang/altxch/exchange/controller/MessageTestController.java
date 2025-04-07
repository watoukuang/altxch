package com.watoukuang.altxch.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "message")
@RequiredArgsConstructor
public class MessageTestController {
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping(value = "/kline") // 接收来自客户端的消息
    public void sendKLine() {
        String symbol = "BTC";
        String message = "xukui";
        messagingTemplate.convertAndSend("/topic/altxch-market/kline/" + symbol, message);
    }
}
