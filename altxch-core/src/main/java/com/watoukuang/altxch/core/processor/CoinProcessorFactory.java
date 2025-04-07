package com.watoukuang.altxch.core.processor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * CoinProcessorFactory 是一个工厂类，用于管理和提供 CoinProcessor 实例。
 */
@Slf4j
@Getter
public class CoinProcessorFactory {
    private final ConcurrentHashMap<String, CoinProcessor> processorMap;

    /**
     * 构造函数，初始化processorMap
     */
    public CoinProcessorFactory() {
        processorMap = new ConcurrentHashMap<>();
    }

    /**
     * 添加一个 CoinProcessor 到工厂中。
     *
     * @param symbol    处理器的标识符
     * @param processor 要添加的 CoinProcessor 实例
     */
    public void addProcessor(String symbol, CoinProcessor processor) {
        if (symbol == null || processor == null) {
            throw new IllegalArgumentException("Symbol and processor cannot be null"); // 参数校验
        }
        log.info("Adding CoinProcessor for symbol: {}", symbol); // 使用占位符记录日志
        processorMap.put(symbol, processor); // 将处理器添加到哈希表中
        System.out.println(processorMap);
    }

    /**
     * 检查工厂中是否包含指定的 CoinProcessor。
     */
    public boolean containsProcessor(String symbol) {
        // 直接检查哈希表中的键
        return processorMap.containsKey(symbol);
    }

    /**
     * 根据符号获取对应的 CoinProcessor 实例。
     */
    public CoinProcessor getProcessor(String symbol) {
        // 从哈希表中获取处理器
        return processorMap.get(symbol);
    }
}