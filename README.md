# ⚔️ Clash Royale Backend Server

**一个从零构建、逐步演进至云原生、AI 赋能的《皇室战争》风格游戏服务器。**

本项目摒弃传统 CRUD 套路，深度实践**游戏后端核心痛点**：高精度游戏循环、无 if-else 状态机、线程并发调度、容器化部署、大模型智能代理（AI 玩家）以及 AIOps 可观测性。

> 🎯 **当前里程碑**：已完成 **Spring Boot 3.x 架构改造**（第五周），核心战斗组件已注册为 Spring 单例 Bean，正准备暴露 REST API 与 Docker 容器化。

---

## 🚀 项目亮点（简历杀手锏）

- **纯 Java 战斗引擎**：30Hz 心跳驱动、OOP 领域建模、Map 注册表消除分支、工厂模式解耦。
- **Spring Boot 3.x 微服务化**：组件 `@Component` 化，暴露 REST API，配置文件驱动。
- **云原生容器化**：提供 Dockerfile，支持 `docker-compose` 一键启动多服务。
- **AI 智能对决**：集成 Spring AI / LangChain4j，让大模型（DeepSeek / GPT）作为 AI 玩家自主决策下牌。
- **RAG 卡牌知识库**：基于向量数据库的卡牌克制关系检索，AI 实时给出最优出牌建议。
- **AIOps 智能监控**：采集 Tick 漂移率、内存/CPU 指标，调用大模型自动分析故障并给出运维建议。

---

## 📦 技术栈

| 领域             | 技术选型                                         |
|----------------|----------------------------------------------|
| 语言             | Java 21 +                                     |
| 框架             | Spring Boot 4.0.6 (等同于 Spring Boot 3.x)      |
| 构建             | Maven 3.9+                                   |
| Web 层          | Spring Web (REST API, Tomcat 11)             |
| 容器化           | Docker + Docker Compose                      |
| AI 集成          | Spring AI / 手动 Prompt + JSON 解析              |
| 向量数据库（RAG） | Chroma / FAISS (本地) + Spring AI Vector Store |
| 可观测性         | Spring Boot Actuator + 自定义 Metrics + AI 分析   |
| 版本控制         | Git + GitHub                                 |

---

## 📂 项目包结构（大厂规范）

```
my-clash-royale-server/
│
├── .gitignore                     # Git 忽略规则（排除 .idea、.iml 等 IDE 文件）
├── README.md                      # 项目说明、架构演进、每日进度
├── pom.xml                        # Maven 依赖管理（Spring Boot 3.x 等）
├── Dockerfile                     # 容器镜像构建文件（W6 使用）
├── docker-compose.yml             # 多服务编排（游戏服务 + 数据库 + AI 等）
│
├── mvnw / mvnw.cmd                # Maven Wrapper（无需全局安装 Maven 即可构建）
│
└── src/
    ├── main/
    │   ├── java/com/neudoc/clashroyale/
    │   │   │
    │   │   ├── ClashRoyaleApplication.java   # 🚀 Spring Boot 启动入口
    │   │   │
    │   │   ├── constant/                     # 📌 常量与枚举
    │   │   │   ├── Team.java                 #   阵营枚举（BLUE / RED）
    │   │   │   └── EntityState.java          #   实体状态机（IDLE / WALKING / ATTACKING / DEAD）
    │   │   │
    │   │   ├── model/                        # 🃏 面向对象数据模型
    │   │   │   ├── GameEntity.java           #   抽象父类（坐标、血量、阵营、状态等公共属性）
    │   │   │   ├── Soldier.java              #   士兵（可移动，有速度、攻击力等）
    │   │   │   ├── Tower.java                #   建筑（不可移动，防御塔、国王塔等）
    │   │   │   ├── Card.java                 #   卡牌数据（名称、圣水消耗、类型）
    │   │   │   └── Deck.java                 #   卡组管理器（8 张牌循环，手牌 + 牌库队列）
    │   │   │
    │   │   ├── engine/                       # 🧠 核心战斗引擎
    │   │   │   ├── BattleField.java          #   战场管理器（实体列表、索敌、收尸、注册表出牌）
    │   │   │   ├── ElixirManager.java        #   圣水管理器（自动恢复、上限控制、消耗扣费）
    │   │   │   ├── UnitCreator.java          #   函数式接口（定义实体创建契约）
    │   │   │   └── GameLoop.java             #   30Hz 时间轮心跳（虚拟线程，驱动战场更新）
    │   │   │
    │   │   ├── player/                       # 🪙 玩家资产
    │   │   │   └── Player.java               #   玩家账号（金币、宝石、持有卡组、阵营）
    │   │   │
    │   │   ├── factory/                      # 📦 设计模式（工厂）
    │   │   │   └── UnitFactory.java          #   兵种制造工厂（静态方法批量生产士兵/建筑）
    │   │   │
    │   │   ├── util/                         # 🛠️ 算法工具
    │   │   │   └── MathUtil.java             #   欧几里得距离计算、AoE 范围判定等
    |   |   |
    |   |   ├── controller/                   #   接收浏览器、Postman 或其他客户端发来的 HTTP 请求,调用后端的业务逻辑,把结果包装成 JSON 返回给客户端
    |   |   |   └──BattleController           #   处理与"战斗状态"相关的 HTTP 请求
    |   |   |
    │   │   │
    │   │   └── config/                       # ⚙️ 应用配置与启动管理
    │   │       └── GameStarter.java          #   CommandLineRunner，容器就绪后自动点火 30Hz 循环
    │   │
    │   └── resources/
    │       ├── application.yml               # Spring Boot 核心配置（帧率、圣水参数等）
    │       └── application.properties        # 备选配置（或留空）
    │
    └── test/
        └── java/com/neudoc/clashroyale/     # 🧪 单元测试目录（JUnit 预留）
```

---

## 📅 当前进度（12 周计划）

| 阶段 | 周次 | 完成情况 |
|------|------|----------|
| **地基期** – Java SE 引擎 + Linux 底座 | W1~W4 | ✅ 100% |
| **核心期** – Spring Boot 改造 + AI 工程化接入 | W5~W8 | 🔄 进行中（W5 已完成 D30，开始 D31） |
| **项目期** – 高并发调度 + AIOps 智能监控 | W9~W12 | ⏳ 待开始 |

### ✅ 已完成关键模块
- 纯 Java 版本 30Hz 游戏主循环
- 士兵/塔/法术 OOP 继承体系
- 工厂模式无 if-else 部署卡牌
- 圣水管理、多对多索敌算法
- Git 规范提交、Linux 基础操作
- **Spring Boot 项目创建、Maven 依赖管理**
- **核心组件注册为 Spring Bean（`@Component`）**
- **YAML 配置文件、Tomcat 启动成功**

### 🔄 本周（W5）任务
- D31 – 编写 `BattleController`，暴露 `GET /api/battle/status`
- D32 – 增加 `POST /api/battle/deploy` 接口
- D33 – Docker 基础学习
- D34-35 – 周末接口测试 + 容器化复盘

---

## 🧪 快速运行（当前版本）

### 前置条件
- JDK 17 或 21
- Maven 3.6+（或使用项目内 `mvnw`）

### 运行步骤
```bash
git clone https://github.com/scpcddk/MY-CR-Spring-Boot.git
cd MY-CR-Spring-Boot
./mvnw spring-boot:run
```

启动后访问：`http://localhost:8080/api/battle/status`（待 D31 完成后返回 JSON）

---

## 🐳 Docker 运行（即将支持）

```bash
docker build -t clash-royale-server .
docker run -p 8080:8080 clash-royale-server
```

---

## 🤝 贡献与联系

本项目为个人学习 & 求职作品，欢迎交流技术细节。  
GitHub: [scpcddk](https://github.com/scpcddk)

---

## 📜 许可证

MIT License

---

**下一步**：完成 `BattleController` 后，即可通过 HTTP 获取战场状态，并向容器化迈进。