package com.watoukuang.altxch.match.mq.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    /**
     * Kafka Broker的地址
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    /**
     * 是否启用自动提交偏移量
     */
    @Value("${spring.kafka.consumer.enable.auto.commit}")
    private boolean enableAutoCommit;
    /**
     * 会话超时时间
     */
    @Value("${spring.kafka.consumer.session.timeout}")
    private String sessionTimeout;
    /**
     * 自动提交间隔时间
     */
    @Value("${spring.kafka.consumer.auto.commit.interval}")
    private String autoCommitInterval;
    /**
     * 消费者ID
     */
    @Value("${spring.kafka.consumer.group.id}")
    private String groupId;
    /**
     * 自动偏移重置策略
     */
    @Value("${spring.kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;
    /**
     * 并发消费者的数量
     */
    @Value("${spring.kafka.consumer.concurrency}")
    private int concurrency;
    /**
     * 每次 poll 操作最多获取的记录数
     */
    @Value("${spring.kafka.consumer.maxPollRecordsConfig}")
    private int maxPollRecordsConfig;

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecordsConfig);// 每个批次获取数
        return propsMap;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.setBatchListener(true);
        return factory;
    }
}
