package com.watoukuang.altxch.match.mq.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    /**
     * 设置KDK服务器地址，从配置文件中读取
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    /**
     * 生产者重试次数，从配置文件中读取
     */
    @Value("${spring.kafka.producer.retries}")
    private int retries;
    /**
     * 生产者批量发生的大小，从配置文件中读取
     */
    @Value("${spring.kafka.producer.batch.size}")
    private int batchSize;
    /**
     * 生产者发送延迟，单位为毫秒，默认为 1 毫秒。
     */
    @Value("${spring.kafka.producer.linger}")
    private int linger;
    /**
     * 生产者缓冲区内存大小，从配置文件中读取
     */
    @Value("${spring.kafka.producer.buffer.memory}")
    private int bufferMemory;

    /**
     * 配置生产者属性
     */
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }
}
