## 📅 第五周（剩余部分）- SpringBoot 改造与 Web 路由

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D31 | 编写 `BattleController`，暴露 `GET /api/battle/status` | `@RestController`, `@GetMapping`, `@Autowired` | curl 访问返回 JSON |
| D32 | 增加 `POST /api/battle/deploy` 接口 | `@PostMapping`, `@RequestBody` | 请求体传入卡牌类型、坐标，圣水扣减成功 |
| D33 | Docker 基础环境搭建 | `docker run`, `docker ps`, `docker images` | 成功运行 hello-world 容器 |
| D34-35 | 周末：Postman / curl 测试所有接口，复盘容器与原生进程差异 | 多客户端测试，性能对比 | 接口响应正确，理解容器网络 |

---

## 📅 第六周 - SpringAI 探路 & 容器化部署

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D36 | 编写 `Dockerfile`，将 Spring Boot 项目打包成镜像 | 多阶段构建, `docker build` | 镜像大小 < 300MB |
| D37 | 运行容器并测试接口 | `docker run -p 8080:8080` | 容器内服务正常返回 JSON |
| D38 | 引入 Spring AI / LangChain4j 依赖 | `pom.xml` 添加依赖，配置 API Key | 无编译错误 |
| D39 | 编写 `AiService.java`，本地调通大模型 API（DeepSeek / OpenAI） | `RestTemplate` / `WebClient` | 控制台打印模型返回的文本 |
| D40 | 编写结构化 Prompt，让大模型输出固定格式 | 提示词工程 | 模型输出 JSON 格式动作 |
| D41-42 | 复盘：对比原生进程与 Docker 容器的资源开销 | `docker stats`, `top` | 记录 CPU/内存差异 |

---

## 📅 第七周 - 大模型 AI 竞技代理人开发

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D43 | AI 玩家代理：拼接战场状态（圣水、手牌）作为上下文 | 字符串格式化 | 日志打印上下文 |
| D44 | 强制约束 Prompt，要求返回固定 JSON 动作 | `{"index":0,"x":12.0,"y":1.0}` | 模型返回 JSON |
| D45 | 编写 JSON 解析器，调用 `BattleField.deployCard()` | Jackson / Gson | AI 成功出兵 |
| D46 | 配置 Docker 桥接网络，让容器访问外部 AI 网关 | 网络模式 `bridge` | 容器内 ping 通 AI 服务 |
| D47 | 将 AI 决策挂载进 `GameLoop`（每 2 秒决策一次） | 定时任务 / `ScheduledExecutorService` | 每 2 秒自动出兵 |
| D48-49 | 周末：观察调优 AI 下牌合理性，防止格式崩溃 | 异常处理 + 重试 | AI 持续稳定决策 |

---

## 📅 第八周 - 卡牌平衡性 RAG 知识库与本地编排

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D50 | 编写卡牌克制关系的 Markdown 文档 | 手工编写 `cards_knowledge.md` | 文档包含克制逻辑 |
| D51 | 引入向量数据库（Chroma / FAISS），文档切片并向量化 | Spring AI Vector Store | 存储成功 |
| D52 | 新增 `GET /api/ai/recommend`，根据敌方兵种 RAG 检索给出建议 | RAG 检索增强生成 | 返回克制卡牌建议 |
| D53 | 编写 `docker-compose.yml`，编排游戏服务 + Redis + 向量库 | `docker-compose up -d` | 一键启动所有服务 |
| D54 | 验证 RAG 异步处理，避免卡死游戏主循环 | `@Async` / `CompletableFuture` | 检索耗时不影响 30Hz Tick |
| D55-56 | 周末：整体链路压测（多线程并发调用推荐接口） | JMeter / 自定义脚本 | 响应时间 < 500ms |

---

## 📅 第九周 - 线程池隔离与多房间并发调度

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D57 | 配置 `ThreadPoolTaskExecutor`，定义核心/最大线程数 | `@EnableAsync`, `ThreadPoolTaskExecutor` | 线程池 Bean 创建成功 |
| D58 | 实现 `RoomManager`，每个对局分配独立线程跑 `GameLoop` | `ConcurrentHashMap` | 多房间日志隔离 |
| D59 | 对 `BattleField` 实体列表进行线程安全改造 | `CopyOnWriteArrayList`, `synchronized` | 无并发修改异常 |
| D60 | 编写高并发模拟脚本，同时开启 50 个虚拟房间 | 多线程模拟 | 观察活跃线程数 |
| D61 | 设置线程池拒绝策略，房间满时返回 HTTP 429 | `RejectedExecutionHandler` | 超限请求返回 429 |
| D62-63 | 周末：分析 GC 与内存曲线，确保房间销毁时实体被回收 | `jstat`, `VisualVM` | 内存平稳，无泄漏 |

---

## 📅 第十周 - 多语言进程联动与 Serverless 弹性思维

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D64 | 学习 `ProcessBuilder`，Java 拉起本地 Python 脚本 | `ProcessBuilder`, `Process` | 启动成功 |
| D65 | 编写 Python 离线分析脚本 `analytics.py`（计算对局惨烈程度） | Python 数学计算 | 返回胜率预测 |
| D66 | 实现跨语言管道通信（stdin / stdout） | `BufferedReader`, `BufferedWriter` | Java 发给 Python，Python 返回结果 |
| D67 | 思考 Serverless 抽离：模拟调用 AWS Lambda / 阿里云函数计算 | HTTP 触发函数 | 异步返回分析结果 |
| D68 | 性能对比：Java 本地计算 vs 拉起 Python vs 调用 Serverless | 记录耗时 | 耗时数据对比表 |
| D69-70 | 周末：将跨语言联动逻辑封装到 `util/CrossLangUtil` | 工具类 | 可复用调用方法 |

---

## 📅 第十一周 - AIOps 监控闭环（核心亮点）

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D71 | 在 `GameLoop` 中统计 Tick 耗时，计算“晶振漂移率” | `System.nanoTime()` | 日志输出漂移百分比 |
| D72 | 引入 `Spring Boot Actuator` + 自定义 Metrics | `MeterRegistry`, `Micrometer` | 暴露 `/actuator/metrics` |
| D73 | 编写定时监控轮询器（每 10 秒抓取指标） | `@Scheduled` | 检测到 Tick 延迟 >20% 触发警报 |
| D74 | 将异常指标发送给大模型（AIOps 分析） | 调用 AI 服务 | 模型返回故障原因 |
| D75 | 根据 AI 建议自动调整线程池上限 / 触发 GC | 动态配置 | 系统自我修复 |
| D76-77 | 周末：人工制造内存泄漏或高负载，验证 AIOps 闭环 | 压力测试 | AI 给出可行建议 |

---

## 📅 第十二周 - 工程化收尾与大厂准入职简历复盘

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D78 | 全盘代码大扫除：`System.out.println` 替换为 `Log4j2` / `Slf4j` | 日志框架 | 无黄色警告 |
| D79 | 撰写精美的 `README.md`，包含架构拓扑图 | Markdown + 绘图（Mermaid） | 结构清晰，可指导运行 |
| D80 | 录制 3 分钟系统运行录屏（展示核心功能） | OBS / 屏幕录制 | 展示高并发、AI 决策、AIOps 建议 |
| D81 | 提炼简历杀手锏项目描述 | 总结技术亮点 | 一段 200 字项目说明 |
| D82 | 发布 GitHub `v1.0.0 Release` | 打 tag，写 Release Notes | 仓库达到准入职级别 |
| D83-84 | 周末：最终复盘，准备大厂面试 | 项目回顾 | 自信应对项目深挖 |

---
