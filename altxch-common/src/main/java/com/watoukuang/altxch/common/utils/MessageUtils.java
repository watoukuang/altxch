package com.watoukuang.altxch.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 获取 i18n 资源
 */
public class MessageUtils {

    /**
     * 根据消息键和参数获取对应的消息
     *
     * @param code
     * @param args
     */
    public static String message(String code, Object... args) {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
