# 📌 ALTXCH 数字资产交易平台

<p align="center">
  <img src="doc/imgs/logo.png" alt="ALTXCH Logo" width="200">
</p>

## 🔥 项目描述
 
<a href="http://altxch.com">ALTXCH</a> 是一款基于**SpringCloud**微服务架构的开源数字货币交易所**解决方案**，专为**小型交易所**快速部署和**开发者学习交易所核心业务**而设计。
系统采用前后端解耦的现代化架构设计，基于分布式微服务体系构建。 前端基于Vue3+TypeScript构建响应式界面, 后端采用SpringBoot和MyBatisPlus业务逻辑处理。 系统通过**Redis**实现毫秒级的高频数据缓存，**MySQL**确保核心业务数据的强一致性，**Kafka**构建高可靠的消息投递机制，*
*MongoDB**处理海量业务数据。项目主要由以下几个子系统构成:
- 网关服务: 基于Gateway构建的高性能网关系统统一入口，作为系统的统一入口提供动态路由、接口鉴权等能力，从而确保系统的安全性和稳定性。
- 注册中心: 采用Eureka构建的分布式服务注册发现中心，实现微服务的自动注册与健康检查。通过心跳检测机制实时监控服务状态变化，结合邮件告警机制，确保服务网格的稳定运行。
- 管理系统: 基于RBAC模型的综合管理平台，包含用户管理、菜单管理、权限管理、内容管理、币币管理、财务管理等功能。
- 会员系统: 主要处理会员相关的操作，包括会员登录、注册、信息修改、现货管理、交易记录查询等功能。
- 市场服务: 市场服务作为交易系统的核心枢纽，采用高效的事件驱动架构处理全流程交易业务。用户下单，保存订单，KFK发送订单到撮合系统，撮合系统进行处理，接受戳和系统处理的结果，保存成交记录，卖盘卖盘数据，深度图，币种缩略图，基于WebSocket进行推送， 
- 撮合服务: 遍历每一订单记录，进行撮合，撮合成功，产生交易记录和订单薄深度
- 钱包服务: 钱包服务提供充币、提币和获取地址等功能，支持用户管理其数字资产
- 节点服务: 节点服务主要用于对接比特币（BTC）和以太坊（ETH）公链，支持区块链网络的交互。
- 任务模块: 基于XXL-JOB的分布式任务调度平台，实时定时转账，深度图推送等批处理业务

---

## 🚀 系统架构


## 🍀 系统流程

### 鉴权认证
<p align="center">
  <img src="doc/imgs/gateway.png" alt="Gateway">
</p>



---

## 🛠️ 技术栈

- **前端**：Vue 3 + View UI Plus
- **后端**：Node.js + Express
- **数据库**：MongoDB
- **区块链交互**：Web3.js
- **部署**：Docker + Nginx

---

## 📂 项目结构

```
altxch/
├── src/
│   ├── assets/          # 静态资源
│   ├── components/      # 公共组件
│   ├── views/           # 页面视图
│   ├── router/          # 路由配置
│   ├── store/           # 状态管理
│   ├── services/        # API服务
│   ├── utils/           # 工具函数
│   └── App.vue          # 根组件
├── public/              # 公共文件
└── package.json         # 项目依赖
```

---

## 🏁 快速开始

### 🛠️ 开发环境

1. 克隆仓库

```bash
git clone https://github.com/yourusername/altxch.git
cd altxch
```

2. 安装依赖

```bash
npm install
```

3. 启动开发服务器

```bash
npm run dev
```

### 🚀 生产构建

```bash
npm run build
```

---

## ⚙️ 配置

复制 `.env.example` 文件为 `.env` 并修改配置：

```env
VUE_APP_API_URL=https://api.altxch.com
VUE_APP_CHAIN_ID=1
```

---

---

## ⚠️ 免责声明与使用条款

### 🔒 重要声明

1. **📜 代码用途限制**
    - 🚫 本项目开源代码**仅供学习参考和技术研究**，禁止用于任何非法用途
    - ⛔ 严禁将本代码用于未经授权的商业运营或非法金融活动

2. **⚖️ 法律合规要求**
    - ✅ 如需基于本代码搭建运营交易所，**必须**：
        - 📑 遵守所在国家/地区的金融监管法律法规
        - 🏛️ 取得必要的金融业务许可和资质
        - 🕵️ 实施完整的KYC/AML合规流程

3. **🛡️ 责任免除**
    - ❌ 开发者/代码贡献者**不承担**因以下情况导致的任何法律责任：
        - ⚠️ 代码被用于非法用途
        - ⚠️ 未取得合法资质擅自运营
        - ⚠️ 违反当地金融监管规定

4. **👤 使用者义务**
    - 🤝 使用本代码即表示您已充分理解并同意：
        - ⚠️ 自行承担所有使用风险
        - ✅ 确保使用方式完全合法合规
        - 🚨 如发现非法使用，有义务立即停止并举报

### 💡 合规提示

我们强烈建议：  
🔹 ✅ 在使用前咨询专业法律顾问  
🔹 📚 完整了解所在司法管辖区的数字货币监管政策  
🔹 🔍 仅将代码用于合法授权的技术研究

*❗ 注：任何违反上述条款的行为均与代码作者及贡献者无关，使用者需自行承担全部法律责任。*

---

## 🤝 贡献指南

欢迎提交 Pull Request。对于重大变更，请先开 Issue 讨论。

1. 🍴 Fork 项目
2. 🌿 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 💾 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 🚀 推送到分支 (`git push origin feature/AmazingFeature`)
5. 🔄 开启 Pull Request

---

## 📜 许可证

[MIT](https://choosealicense.com/licenses/mit/)

---

## 📞 联系方式

- 🌐 官网: [https://altxch.com](https://altxch.com)
- ✉️ 邮箱: contact@altxch.com
- 🐦 Twitter: [@altxch](https://twitter.com/altxch)

---

💖 *感谢您的关注与支持！* 💖