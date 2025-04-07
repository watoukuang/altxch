package com.watoukuang.altxch.core.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberRegDTO implements Serializable {
    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String passwd;

    /**
     * 用户验证码
     */
    private String captcha;
}
