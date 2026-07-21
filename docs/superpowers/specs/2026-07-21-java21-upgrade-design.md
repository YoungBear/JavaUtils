# Java 8 → Java 21 升级设计

## 目标

将项目从 Java 1.8 升级到 Java 21，项目版本号从 1.0.0-SNAPSHOT 升级到 2.0.0-SNAPSHOT。

## 变更清单

### 1. pom.xml — 版本号与 Java 版本

| 属性 | 当前值 | 新值 |
|---|---|---|
| `project.version` | 1.0.0-SNAPSHOT | 2.0.0-SNAPSHOT |
| `java.version` | 1.8 | 21 |
| `maven.compiler.source` | 1.8 | 21 |
| `maven.compiler.target` | 1.8 | 21 |

maven-compiler-plugin 配置用 `<release>21</release>` 替代已废弃的 `<compilerArgument>-Xlint:all</compilerArgument>`。

### 2. 依赖升级

| 依赖 | 当前版本 | 新版本 | 原因 |
|---|---|---|---|
| mockito-inline | 4.11.0 | mockito-core 5.14.2 | inline 已合并到 core；5.x 需要 JDK 11+，支持 JDK 21 |
| logback-classic | 1.2.13 | 1.4.14 | JDK 21 兼容（1.5.x API 变化较大，1.4.x 最稳妥） |

其他依赖不变：jackson 2.18.9、bouncycastle 1.80、junit5 5.11.4、JMH 1.37 均已支持 JDK 21。

### 3. 代码重构 — ThreadPoolConfig

当前 `calculateCorePoolCount()` 读取 `static final` 字段 `CPU_CORE_COUNT`，该字段在静态初始化块中通过 `Runtime.getRuntime().availableProcessors()` 赋值。测试通过反射修改 `Field.modifiers` 来 mock CPU 核心数，JDK 12+ 已不支持。

重构方案：
- `ThreadPoolConfig` 新增 `calculateCorePoolCount(Runtime runtime)` 重载方法，接收 `Runtime` 实例
- 原无参方法保留为便捷入口，委托到新方法：`calculateCorePoolCount(Runtime.getRuntime())`
- 移除 `CPU_CORE_COUNT` 静态常量
- 核心数上限 10 的逻辑保持不变

### 4. 测试重写 — ThreadPoolConfigTest

- 使用 Mockito `MockedStatic<Runtime>` mock 静态方法 `Runtime.getRuntime()`
- 对每种场景（4 核、10 核、16 核），mock `Runtime.availableProcessors()` 返回对应值
- 直接调用 `ThreadPoolConfig.calculateCorePoolCount()` 验证结果
- 不再使用反射

### 5. 验证

- `mvn clean test` 确保全部测试通过
- 确认 0 failures, 0 errors
