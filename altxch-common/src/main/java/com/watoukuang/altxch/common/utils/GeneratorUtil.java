package com.watoukuang.altxch.common.utils;

import java.util.Random;
import java.util.UUID;

import java.util.Random;
import java.util.UUID;

public class GeneratorUtil {
    private static final Random RANDOM = new Random();
    private static final String PROMOTION_SEED = "E5FCDG3HQA4B1NOPIJ2RSTUV67MWX89KLYZ";
    private static final String NONCE_SEED = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 得到from到to的随机数，包括to
     *
     * @param from 起始值
     * @param to   结束值
     * @return 随机数
     */
    public static int getRandomNumber(int from, int to) {
        return RANDOM.nextInt(to - from + 1) + from;
    }

    /**
     * 生成推广码
     *
     * @param uid 用户ID
     * @return 推广码
     */
    public static String getPromotionCode(long uid) {
        long num = uid + 10000;
        StringBuilder code = new StringBuilder();
        while (num > 0) {
            long mod = num % 35;
            num /= 35;
            code.insert(0, PROMOTION_SEED.charAt((int) mod));
        }
        while (code.length() < 4) {
            code.insert(0, "0");
        }
        return code.toString();
    }

    /**
     * 生成随机字符串
     *
     * @param len 字符串长度
     * @return 随机字符串
     */
    public static String getNonceString(int len) {
        StringBuilder tmp = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            tmp.append(NONCE_SEED.charAt(getRandomNumber(0, NONCE_SEED.length() - 1)));
        }
        return tmp.toString();
    }

    /**
     * 生成UUID
     *
     * @return UUID字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成订单ID
     *
     * @param prefix 订单ID前缀
     * @return 订单ID
     */
    public static String getOrderId(String prefix) {
        return prefix + System.currentTimeMillis() + getRandomNumber(10, 99);
    }
}