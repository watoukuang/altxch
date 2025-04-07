package com.watoukuang.altxch.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 将字符串日期转换为时间戳（毫秒）。
     *
     * @param dateString 日期字符串，格式为 "yyyy-MM-dd HH:mm:ss"
     * @return 时间戳（毫秒），如果解析失败则返回 -1
     */
    public static long convertStringToTimestamp(String dateString) throws ParseException {
        // 定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 解析字符串日期为 Date 对象
        Date date = sdf.parse(dateString);
        // 返回时间戳（毫秒）
        return date.getTime();
    }
}
