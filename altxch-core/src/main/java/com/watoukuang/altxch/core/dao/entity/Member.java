package com.watoukuang.altxch.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_member")
public class Member implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户密码
     */
    private String passwd;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 交易状态
     */
    private Integer isTrade;
}
