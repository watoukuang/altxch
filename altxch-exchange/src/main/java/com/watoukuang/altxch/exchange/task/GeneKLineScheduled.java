package com.watoukuang.altxch.exchange.task;

import com.watoukuang.altxch.core.processor.CoinProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeneKLineScheduled {
    private final CoinProcessorFactory processorFactory;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 每分钟定时器，处理分钟K线
     */
    @Scheduled(cron = "0 * * * * *")
    public void generatorKLineGraph() {
        log.info("每分钟定时器，处理分钟K线，开始运行......");

        // 获取当前时间并设置秒和毫秒为 0
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long time = calendar.getTimeInMillis();
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // 使用流式处理来处理所有处理器
        processorFactory.getProcessorMap().forEach((symbol, processor) -> {
            // 检查处理器是否停止K线生成，并执行生成任务
            if (!processor.isStopKline()) {
                threadPoolTaskExecutor.execute(() -> {
                    log.info("处理器 {} 开始生成 K线: 时间={}, 分钟={}, 小时={}", symbol, time, minute, hour);
                    processor.generateKLine(time, minute, hour);
                    log.info("处理器 {} K线生成完成", symbol);
                });
            } else {
                log.warn("处理器 {} 已停止 K线生成", symbol);
            }
        });

        // 打印当前时间的分钟K线
        log.info("分钟K线处理完成，当前时间: {}", calendar.getTime());
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long time = calendar.getTimeInMillis();
        // 创建一个 Date 对象
        Date date = new Date(time);

        // 使用 SimpleDateFormat 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);

        // 打印时间戳和格式化后的日期
        System.out.println("Time in milliseconds: " + time);
        System.out.println("Formatted date: " + formattedDate);
    }
}
