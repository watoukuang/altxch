package com.watoukuang.altxch.exchange.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单内存消息代理，前缀为/topic的消息会发送到消息代理
        config.enableSimpleBroker("/topic");
        // 设置应用程序目的地前缀，客户端发送消息需要带上/app前缀
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点，客户端将使用它连接到我们的STOMP服务器
        registry.addEndpoint("/altxch-market-ws").setAllowedOrigins("*")
                .withSockJS();
    }

}
