# ⚔️ Clash Royale Backend Server

**一个从零构建、逐步演进至云原生与 AI 赋能的《皇室战争》风格游戏服务器。**

本项目摒弃传统 CRUD 套路，深度实践**游戏后端核心痛点**：高精度游戏循环、状态机驱动、线程并发调度、REST API 暴露、容器化部署，并规划引入大模型辅助决策与可观测性监控。

> 🎯 **当前里程碑**：已完成 **Spring Boot 3.3.5 架构改造**（第五周），核心战斗组件已注册为 Spring 单例 Bean，并成功暴露 `GET /api/battle/status` 与 `POST /api/battle/deploy` 接口。下一步将进入 Docker 容器化与 AI 集成。

---

## 🚀 项目真实亮点（面试官视角）

- **纯 Java 战斗引擎**：30Hz 独立线程心跳驱动，`BattleField` 每帧执行“索敌 → 决策 → 行动 → 收尸”闭环，支持两军对垒。
- **无 if-else 状态机 + Map 注册表**：卡牌部署通过 `Map<String, UnitCreator>` 消除长链条件判断，新增兵种只需一行注册，符合开闭原则。
- **双阵营圣水管理**：蓝方与红方独立恢复圣水，支持同步消耗与不足回滚。
- **OOP 领域建模**：`GameEntity` 抽象父类，`Soldier` / `Tower` 多态实现移动与攻击差异。
- **Spring Boot 3.3.5 统一管理**：核心组件 `@Component` 化，`@RestController` 暴露 REST API，Jackson 自动序列化。
- **完整的项目结构与 Git 规范**：包结构符合大厂分层规范，commit 消息遵循 `feat:` / `fix:` 格式。

### 📌 规划中的进阶方向（第 6~12 周）
- **战斗回放**：每 Tick 记录战场快照，提供重播接口。
- **WebSocket 长连接**：替换 REST API，降低实时对战延迟。
- **AI 异步决策**：大模型通过异步队列给出出牌建议，不阻塞游戏循环。
- **卡牌克制关系本地缓存**：使用 `HashMap<String, String>` 存储克制关系，查询性能 O(1)，无外部依赖。
- **K8s 部署 + 负载均衡**：编写 Deployment / Service YAML，支持多实例。
- **Prometheus + Grafana** 可视化 Tick 漂移率、QPS、内存占用。
- **压测报告**：单节点同时支撑 N 个战斗房间的性能数据。

---

## 📦 技术栈

| 领域         | 技术选型                               |
|------------|------------------------------------|
| 语言         | Java 21                            |
| 框架         | Spring Boot 3.3.5 (正式版)           |
| 构建         | Maven 3.9+                         |
| Web 层      | Spring Web (REST API, Tomcat 11)   |
| 容器化       | Docker + Docker Compose（规划中）     |
| AI 集成      | Spring AI + DeepSeek API（规划中）    |
| 可观测性     | Spring Boot Actuator + 自定义 Metrics |
| 版本控制     | Git + GitHub                       |

---

## 📂 项目包结构（大厂规范）
````
my-clash-royale-server/
├── .gitignore                     # Git 忽略规则（排除 .idea、.iml、target 等）
├── README.md                      # 项目概述、架构图、快速开始、面试应答模板
├── ROADMAP.md                     # 12 周改进版行军路线图（含 AI 异步、AIOps 等）
├── pom.xml                        # Maven 依赖管理（Spring Boot 3.3.5、Lombok、测试等）
├── Dockerfile                     # (W6) 容器镜像构建（基于 eclipse-temurin:21-jre-alpine）
├── docker-compose.yml             # (W8) 多服务编排（游戏服务 + 可选向量库，RAG 已废弃）
├── mvnw / mvnw.cmd                # Maven Wrapper（无需预装 Maven）
│
└── src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── myclashroyaleserver/
│   │               │
│   │               ├── MyClashRoyaleServerApplication.java   # Spring Boot 启动类，@SpringBootApplication
│   │               │
│   │               ├── constant/                            # 📌 常量与枚举
│   │               │   ├── Team.java                        # 阵营枚举（BLUE / RED）
│   │               │   ├── EntityState.java                 # 实体状态机（IDLE / WALKING / ATTACKING / DEAD）
│   │               │   ├── ConfigManager.java               # 配置中心（卡牌属性、升级公式等）
│   │               │   └── GameConstants.java               # 静态常量（战场尺寸、圣水上限、帧率等）
│   │               │
│   │               ├── model/                               # 🃏 面向对象数据模型
│   │               │   ├── GameEntity.java                  # 抽象父类：坐标、血量、阵营、状态、攻击力、攻击范围
│   │               │   ├── Soldier.java                     # 士兵实体：可移动，重写 moveTowards()
│   │               │   ├── Tower.java                       # 建筑实体：不可移动，空闲时待机
│   │               │   ├── Spell.java                       # 法术实体：范围伤害、持续效果（预留）
│   │               │   ├── Card.java                        # 卡牌数据类：名称、圣水消耗、对应实体类型
│   │               │   └── Deck.java                        # 卡组管理器：8张牌循环（手牌List + 牌库Queue）
│   │               │
│   │               ├── engine/                              # 🧠 核心战斗引擎
│   │               │   ├── BattleField.java                 # 战场管理器：实体集合、索敌、状态驱动、部署卡牌、清理死亡
│   │               │   ├── ElixirManager.java               # 圣水管理器：每帧恢复、上限10、扣费验证
│   │               │   ├── GameLoop.java                    # 30Hz 心跳线程：无限循环，调用 battleField.update()
│   │               │   └── UnitCreator.java                 # 函数式接口：定义实体创建契约（(Team,x,y)->GameEntity）
│   │               │
│   │               ├── player/                              # 🪙 玩家资产
│   │               │   └── Player.java                      # 玩家账户：ID、金币、宝石、卡组（Deck）、阵营
│   │               │
│   │               ├── factory/                             # 📦 工厂模式
│   │               │   └── UnitFactory.java                 # 兵种工厂：静态方法 createPrince(), createArcher() 等
│   │               │
│   │               ├── util/                                # 🛠️ 算法工具箱
│   │               │   └── MathUtil.java                    # 欧几里得距离计算、范围碰撞检测（AABB）
│   │               │
│   │               ├── exception/                           # ⚠️ 全局异常处理
│   │               │   └── GlobalExceptionHandler.java      # @RestControllerAdvice，统一错误响应格式（400/500）
│   │               │
│   │               ├── controller/                          # 🌐 REST API 控制器
|   |               |   ├── AiTestController.java            # 测试Api调用
│   │               │   ├── BattleController.java            # 处理 /api/battles/{id} 与 /api/battles/{id}/cards
│   │               │   ├── BattleStatusResponse.java        # 战场状态响应 DTO（圣水、存活单位数、塔血量）
│   │               │   ├── DeployRequest.java               # 卡牌部署请求 DTO（cardType, elixirCost, x, y）
│   │               │   └── DeployResponse.java              # 部署结果响应 DTO（success, message）
│   │               │
│   │               ├── config/                              # ⚙️ Spring 配置
│   │               │   └── GameStarter.java                 # CommandLineRunner：Spring 启动后自动 new GameLoop().start()
│   │               │
│   │               ├── ai/                                  # 🤖 AI 模块（计划 W6~W8）
│   │               │   ├── AiService.java                   # 调用大模型 API（DeepSeek/OpenAI），使用 RestTemplate
│   │               │   ├── AiDecisionQueue.java             # 异步决策队列：生产者（定时提交战场上下文）消费者（主线程）
│   │               │   └── CardCounterConfig.java           # 卡牌克制关系：HashMap 本地缓存，查询 O(1)
│   │               │
│   │               ├── room/                                # 🏠 多房间并发管理（计划 W9）
│   │               │   ├── RoomManager.java                 # 房间池：创建/销毁房间，线程池调度，过期清理（5分钟无活动）
│   │               │   └── Room.java                        # 单个房间：封装 BattleField + GameLoop，提供状态快照
│   │               │
│   │               └── monitor/                             # 📊 AIOps 监控（计划 W11）
│   │                   ├── MetricsCollector.java            # 采集 Tick 耗时、实体数、QPS，暴露给 Micrometer
│   │                   └── AiopsAnalyzer.java               # 将异常指标发给大模型，返回运维建议（动态调参）
│   │
│   └── resources/
│       ├── application.yml                                  # Spring Boot 主配置：server.port=8080，logging.level
│       └── application.properties                           # 备选配置（未使用）
│
└── test/                                                     # 🧪 单元测试与集成测试
└── java/
└── com/example/myclashroyaleserver/
├── engine/                                      # BattleField、ElixirManager 测试
└── controller/                                  # WebMvcTest 测试 REST 接口
````
---

> 说明：`GameStarter` 实现 `CommandLineRunner`，在 Spring 容器启动后自动开启 `GameLoop` 线程。

---


> 说明：`GameStarter` 实现 `CommandLineRunner`，在 Spring 容器启动后自动开启 `GameLoop` 线程。

---

## 📅 当前进度（12 周计划）

| 阶段 | 周次 | 完成情况 |
|------|------|----------|
| **地基期** – Java SE 引擎 + Linux 底座 | W1~W4 | ✅ 100% |
| **核心期** – Spring Boot 改造 + 接口暴露 | W5   | 🔄 D31 已完成，D32 进行中 |
| **容器化 + AI 集成** | W6~W8 | ⏳ 待开始 |
| **高并发 + AIOps** | W9~W12 | ⏳ 待开始 |

### ✅ 本周（W5）已实现
- [x] Spring Boot 3.3.5 项目创建，Maven 依赖管理
- [x] 核心战斗组件注册为 `@Component`
- [x] `application.yml` 配置（端口 8080）
- [x] `GET /api/battle/status` 返回战场 JSON
- [x] `POST /api/battle/deploy` 接收卡牌部署请求（临时实现）

### 🚧 正在推进
- D32 完善 `deployUnit` 与真实圣水扣减逻辑
- D33 Docker 基础学习

---

## 🧪 快速运行（当前版本）

### 前置条件
- JDK 21（项目已配置 `java.version=21`）
- Maven 3.6+（或使用 `mvnw`）

### 运行步骤
```bash
git clone https://github.com/scpcddk/MY-CR-Spring-Boot.git
cd MY-CR-Spring-Boot
./mvnw spring-boot:run