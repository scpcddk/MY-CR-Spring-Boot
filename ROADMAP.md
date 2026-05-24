# 皇室战争后端服务器 - 12周改进版行军路线图（V2）

## 📅 第五周（剩余部分）- SpringBoot 改造与 Web 路由

| 天数 | 任务 | 技术点 | 验证方式 | 状态 |
|------|------|--------|----------|------|
| D31 | 编写 `BattleController`，暴露 `GET /api/battles/{battleId}` | `@RestController`, `@GetMapping`, `@PathVariable` | curl 返回 JSON | ✅ 已完成 |
| D32 | 增加 `POST /api/battles/{battleId}/cards` 接口 | `@PostMapping`, `@RequestBody`, 线程安全锁 | 请求体传入卡牌类型、坐标，圣水扣减成功 | ✅ 已完成 |
| D33 | Docker 基础环境搭建 | `docker run`, `docker ps`, `docker images` | 成功运行 hello-world 容器 | ⏳ 进行中 |
| D34-35 | 周末：Postman/curl 测试 RESTful 接口，增加全局异常处理器 | `@RestControllerAdvice` | 接口返回统一格式，错误不暴露堆栈 | ✅ 已完成 |

---

## 📅 第六周 - 容器化部署与 Spring AI 探路（修正版）

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D36 | 编写 `Dockerfile`，使用 `eclipse-temurin:21-jre-alpine` 基础镜像，多阶段构建 | 镜像瘦身 | 镜像大小 < 150MB |
| D37 | 运行容器并测试 RESTful 接口 | `docker run -p 8080:8080` | 容器内服务正常返回 JSON |
| D38 | 引入 Spring AI 依赖，版本固定为 `1.0.0-M3` | `pom.xml` 添加 | 无编译错误，与 Spring Boot 3.3.x 兼容 |
| D39 | 编写 `AiService.java`，本地调通大模型 API（DeepSeek / OpenAI） | `RestTemplate` / `WebClient` | 控制台打印模型返回的文本 |
| D40 | 编写结构化 Prompt，末尾增加保底格式：`如果你不确定，就返回 {"action":"wait"}` | 提示词工程 | 模型永远输出合法 JSON |
| D41-42 | 复盘：对比容器与原生进程的资源开销（启动时间、停止时间、镜像拉取时间） | `docker stats`, `time` | 记录性能数据 |

---

## 📅 第七周 - 异步 AI 竞技代理人（核心修正）

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D43 | 设计 AI 决策独立线程池 + 队列架构 | `ExecutorService`, `BlockingQueue` | 日志显示 AI 线程与主线程分离 |
| D44 | 强制约束 Prompt，要求返回固定 JSON 动作 | `{"action":"deploy","cardIndex":0,"x":12.0,"y":1.0}` | 模型返回 JSON |
| D45 | 编写 JSON 解析器 + 保底机制（连续3次失败则随机出牌） | Jackson, 重试计数器 | AI 稳定出兵，不崩溃 |
| D46 | 配置 Docker 桥接网络，让容器访问外部 AI 网关 | 网络模式 `bridge` | 容器内可调用大模型 API |
| D47 | 将 AI 决策挂载进 `GameLoop`：主线程每 2 秒提交一次决策任务到队列，消费队列结果 | 生产者-消费者模式 | 决策不阻塞 30Hz 心跳 |
| D48-49 | 周末：调优 AI 下牌合理性，日志记录决策耗时 | 异常处理 + 重试 | AI 决策耗时 < 500ms，不影响战斗 |

---

## 📅 第八周 - 卡牌平衡性与本地智能推荐（移除 RAG）

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D50 | 设计卡牌克制关系的静态配置类 `CardCounterConfig` | `HashMap<String, List<String>>` | 单元测试验证克制关系 |
| D51 | 实现克制关系查询方法（正向/反向查找） | 静态工具方法 | `getCounterFor("PRINCE")` 返回 `"DRAGON"` |
| D52 | 新增 `GET /api/ai/recommend?enemyCard=xxx` 接口 | `@GetMapping`, `@RequestParam` | 返回推荐克制卡牌，响应时间 < 10ms |
| D53 | 编写 `docker-compose.yml`，仅编排游戏服务（本阶段不依赖外部数据库） | `docker-compose up -d` | 一键启动，接口正常 |
| D54 | 集成到 AI 决策流程：在调用大模型前，先查询本地克制推荐作为决策参考 | 策略注入 | AI 出牌更合理 |
| D55-56 | 周末：压力测试推荐接口（1000 QPS），记录延迟 | JMeter / wrk | 稳定 O(1) 查询 |

---

## 📅 第九周 - 线程池隔离与多房间并发调度（核心）

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D57 | 配置 `ThreadPoolTaskExecutor`，核心线程数 = `Runtime.getRuntime().availableProcessors()`，最大 = 核心 ×2，队列长度 100，拒绝策略 `AbortPolicy` | `@EnableAsync`, `ThreadPoolExecutor` | 线程池 Bean 创建成功 |
| D58 | 实现 `RoomManager`，每个对局分配独立线程跑 `GameLoop`，实现房间过期自动销毁（5分钟无活动） | `ConcurrentHashMap`, `ScheduledExecutorService` | 多房间日志隔离，过期房间自动释放 |
| D59 | 对 `BattleField` 实体列表进行线程安全改造 | `CopyOnWriteArrayList`, `ReentrantLock` | 无并发修改异常 |
| D60 | 编写高并发模拟脚本，同时开启 50 个虚拟房间 | 多线程模拟 | 观察活跃线程数，无崩溃 |
| D61 | 设置线程池拒绝策略，房间满时返回 HTTP 429 | `RejectedExecutionHandler` | 超限请求返回 429 |
| D62-63 | 周末：创建100个房间后销毁，用 VisualVM 观察内存回收；将压测结果写入 README | `jstat`, `VisualVM` | 内存平稳，无泄漏 |

---

## 📅 第十周 - 聚焦 AIOps（跳过跨语言联动）

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D64 | 学习 `Micrometer` 指标采集 | `MeterRegistry` | 自定义指标注册成功 |
| D65 | 在 `GameLoop` 中记录 Tick 耗时、实体数量、QPS | 计数器, `Summary` | 指标暴露给 `/actuator/metrics` |
| D66 | 引入 `Prometheus` + `Grafana` 本地搭建，配置 Spring Boot 指标拉取 | docker-compose 编排 | 可视化面板显示实时指标 |
| D67 | 编写定时监控轮询器（每 10 秒抓取指标），检测 Tick 漂移率 > 20% 触发告警 | `@Scheduled` | 日志打印告警 |
| D68 | 将异常指标（告警信息）发送给大模型，获取运维建议 | 调用 AI 服务 | 模型返回故障原因和建议 |
| D69-70 | 周末：人工制造高负载（开200个房间），验证告警 → AI 分析 → 动态调整线程池上限的闭环 | 压力测试 | 系统根据 AI 建议自动调参 |

---

## 📅 第十一周 - AIOps 监控闭环深化

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D71 | 实现基于 AI 建议的自动扩缩容模拟（修改线程池最大线程数） | `ThreadPoolExecutor.setMaximumPoolSize()` | 告警后自动调整 |
| D72 | 增加 GC 耗时监控，当 GC 时间 > 20% 时触发告警 | `GarbageCollectorMXBean` | 控制台输出告警 |
| D73 | 将历史指标存储到简单文件中，用于趋势分析 | CSV 文件 | 可查询过去5分钟的指标 |
| D74 | 编写 `/actuator/aiops/analyze` 接口，手动触发 AI 深度分析最近5分钟所有指标 | AI 大模型 + 指标数据 | 返回诊断报告 |
| D75 | 实现故障演练脚本：内存泄漏、死循环、死锁 | 模拟故障 | AIOps 能识别并给出建议 |
| D76-77 | 周末：录制 AIOps 闭环演示视频 | OBS | 展示告警 → AI 分析 → 自动修复全流程 |

---

## 📅 第十二周 - 工程化收尾与简历复盘

| 天数 | 任务 | 技术点 | 验证方式 |
|------|------|--------|----------|
| D78 | 全盘代码大扫除：`System.out.println` 替换为 `Log4j2` / `Slf4j`，消除黄色警告 | 日志框架 | 代码整洁无警告 |
| D79 | 撰写最终版 `README.md`，包含架构拓扑图（Mermaid）、压测报告、AIOps 演示截图 | Markdown + 绘图 | 结构清晰，可指导运行 |
| D80 | 录制3分钟系统运行录屏（展示多房间并发、AI 决策、AIOps 自动修复） | OBS | 上传至 B站/YouTube 供面试官查看 |
| D81 | 提炼简历杀手锏项目描述（200字），突出“解决了什么问题” | 总结 | 简历项目栏可直接使用 |
| D82 | 发布 GitHub `v1.0.0 Release`，打 tag，写 Release Notes | 版本管理 | 仓库达到准入职级别 |
| D83-84 | 周末：最终复盘，准备大厂面试项目深挖问题 | 项目回顾 | 自信应对面试官挑战 |

---

## 🎯 关键节点自检清单

- [ ] 所有 API 符合 RESTful 规范（资源命名 + 状态码）
- [ ] 全局异常处理已实现，返回统一 JSON 格式
- [ ] `BattleField` 中所有写操作已加锁（或使用并发集合）
- [ ] Docker 镜像基于 alpine 瘦身
- [ ] AI 决策采用异步队列，不阻塞主循环
- [ ] 卡牌克制关系使用 `HashMap`，无外部依赖
- [ ] 多房间并发 + 线程池配置基于 CPU 核心数动态调整
- [ ] 房间自动过期清理，内存泄漏测试通过
- [ ] AIOps 能根据指标告警并给出可操作建议
- [ ] README 包含压测数据和架构图