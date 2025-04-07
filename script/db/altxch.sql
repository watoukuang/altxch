CREATE TABLE t_exchange_coin
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY, -- 添加主键字段
    symbol             VARCHAR(50)    NOT NULL COMMENT '交易对',
    enable_trade       INT            NOT NULL COMMENT '是否可交易',
    enable_sell        INT            NOT NULL COMMENT '是否允许卖',
    enable_buy         INT            NOT NULL COMMENT '是否允许买',
    base_symbol        VARCHAR(50)    NOT NULL COMMENT '基准币',
    coin_symbol        VARCHAR(50)    NOT NULL COMMENT '交易币',
    base_coin_scale    INT            NOT NULL COMMENT '基币小数精度',
    coin_scale         INT            NOT NULL COMMENT '交易币小数精度',
    min_turnover       DECIMAL(20, 8) NOT NULL COMMENT '最小成交额',
    min_volume         DECIMAL(20, 8) DEFAULT 0 COMMENT '最小下单量，0表示不限制',
    max_volume         DECIMAL(20, 8) DEFAULT 0 COMMENT '最大下单量，0表示不限制',
    min_sell_price     DECIMAL(20, 8) COMMENT '基币小数精度(卖单最低价格)',
    max_buy_price      DECIMAL(20, 8) COMMENT '最高买单价',
    enable_market_sell INT            DEFAULT 1 COMMENT '是否启用市价卖',
    enable_market_buy  INT            DEFAULT 1 COMMENT '是否启用市价买',
    max_trading_order  INT            NOT NULL COMMENT '最大在交易中的委托数量(最大允许同时交易的订单数，0表示不限制)',
    publish_type       INT            DEFAULT 1 COMMENT '发行活动类型 1：无活动，2：抢购发行，3：分摊发行',
    clear_time         VARCHAR(50) COMMENT '活动清盘时间(抢购发行与分摊发行都需要设置)',
    enable             INT            NOT NULL COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易币种表';



CREATE TABLE `t_address_ext`
(
    `id`            INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `address`       VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
    `coin_protocol` TINYINT                                                       DEFAULT NULL COMMENT '币种协议',
    `member_id`     INT NOT NULL COMMENT '用户ID',
    `status`        TINYINT                                                       DEFAULT 0 COMMENT '状态（0:未激活, 1:激活）',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX           `idx_member_id` (`member_id`),
    INDEX           `idx_coin_protocol` (`coin_protocol`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPRESSED COMMENT='地址扩展表';

CREATE TABLE `t_coin_protocol`
(
    `id`            INT                                                           NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `protocol`      SMALLINT                                                      NOT NULL COMMENT '协议',
    `protocol_name` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '协议名称',
    `rpc_server`    VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'RPCServer',
    `rpc_user`      VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT 'RPCUser',
    `rpc_password`  VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT 'RPCPassword',
    `browser`       VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '浏览器',
    `symbol`        VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '符号',
    `chain_id`      SMALLINT                                                      NOT NULL COMMENT 'ChainId 链Id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX           `idx_protocol` (`protocol`),
    INDEX           `idx_chain_id` (`chain_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPRESSED COMMENT='币种协议表';