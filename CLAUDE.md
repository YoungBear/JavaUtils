# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

JavaUtils 是一个 Java 工具库，提供了各类常用工具方法的实现与演示，涵盖加解密、日期时间、正则表达式、文件处理、并发编程、JSON、安全和线程池等领域。

## 常用命令

```bash
# 编译项目
mvn compile

# 运行测试
mvn test

# 运行单个测试类
mvn test -Dtest=ClassName

# 运行单个测试方法
mvn test -Dtest=ClassName#methodName

# 跳过测试编译
mvn compile -DskipTests

# 清理并打包
mvn clean package
```

## 项目结构

### 主代码包组织

```
src/main/java/com/ysx/utils/
├── HelloJavaUtils.java        # 入口类
├── crypto/                    # 加解密相关
│   ├── PBKDF2Practise.java    # PBKDF2 密钥导出实践
│   ├── aes/                   # AES 加解密 (AESUtils, AESPractise, AESException)
│   ├── bc/                    # BouncyCastle Provider 列表
│   ├── codec/                 # 编解码 (Base64, URL)
│   ├── dgst/                  # 消息摘要 (MessageDigest)
│   ├── mac/                   # 消息认证码 (MAC)
│   └── rsa/                   # RSA 加解密/签名 (基于 BouncyCastle)
├── datetime/                  # 日期时间处理 (转换、格式化、夏令时、时区)
├── file/                      # 文件操作 (解压缩、TextFileReader 自动识别编码读取)
├── pattern/                   # 正则表达式 (UUID、IP、版本号、MAC地址、Levenshtein距离)
├── security/                  # 安全相关 (PasswordUtils 密码工具类)
└── threadpool/                # 线程池配置 (ThreadPoolConfig)
```

### 测试结构

```
src/test/java/com/ysx/utils/
├── HelloJavaUtilsTest.java
├── concurrent/                # 并发测试 (CyclicBarrier)
├── crypto/                    # 加解密测试
│   ├── aes/                   # AES 测试
│   ├── codec/                 # 编解码测试
│   ├── dgst/                  # 消息摘要测试
│   ├── mac/                   # MAC 测试
│   └── rsa/                   # RSA 测试
├── datetime/                  # 日期时间测试
│   └── old/                   # 旧版 SimpleDateFormat 演示
├── file/                      # 文件操作测试
├── json/                      # JSON 测试
│   ├── fastjson/              # Fastjson2 字段顺序测试
│   └── jackson/               # Jackson 字段顺序/注解测试
├── log/                       # 日志测试 (Logback, MDC)
├── mockito/                   # Mockito 测试示例 (Answer 参数、final 字段)
├── pattern/                   # 正则表达式测试
│   ├── javatuturials/         # Java 正则教程示例
│   └── performance/           # 正则预编译性能测试
├── security/                  # 安全测试
└── threadpool/                # 线程池测试
```

### 测试资源

```
src/test/resources/
├── digest/                    # 消息摘要测试用文件
├── file/                      # 文件操作测试用文件 (pom.tar.gz)
└── openssl/                   # OpenSSL 生成的 RSA 密钥文件 (.pem)
```

### 文档结构

`mdfiles/` 目录下包含各类工具的详细文档，按类别组织在子目录中：

- `concurrent/` — 并发编程 (CyclicBarrier)
- `crypto/` 及 `crypto/bc/` — 加解密 (AES、RSA、PBKDF2、MessageDigest、MAC、BouncyCastle)
- `datetime/` — 日期时间处理
- `file/` — 文件操作
- `json/` — JSON 处理 (Jackson 注解、字段顺序)
- `log/` — 日志 (Logback)
- `pattern/` — 正则表达式
- `security/` — 安全
- `test/` — 单元测试技巧 (Mockito Answer)
- `threadpool/` — 线程池

## 技术栈

- **Java 版本**: 1.8
- **构建工具**: Maven
- **测试框架**: JUnit 5 (Jupiter) + JUnit Platform Launcher + Mockito (inline)
- **日志**: Logback
- **密码学**: BouncyCastle (bcprov-jdk18on, bcpkix-jdk18on)
- **JSON**: Jackson、Fastjson2、Gson
- **工具库**: Apache Commons Text、Commons Compress、Commons IO
- **性能基准测试**: JMH (OpenJDK)
- **代码简化**: Lombok
- **编译器**: Maven Compiler Plugin 3.14.0，启用 `-Xlint:all` 警告检查
- **测试运行器**: Maven Surefire Plugin 3.5.2

## Git 提交规范

- 提交信息格式：`[type] 中文描述`，type 使用 `[add]`、`[fix]`、`[update]` 等标签
- AI 生成的提交需在末尾添加实际模型的 Co-Authored-By，例如：
  ```
  Co-Authored-By: deepseek-v4-pro <noreply@anthropic.com>
  ```

## 开发注意事项

- 所有源文件使用 UTF-8 编码
- 编译器启用了 `-Xlint:all` 警告检查
- 测试代码位于 `src/test/java/` 目录，测试资源位于 `src/test/resources/`
- README.md 中包含完整的文档索引，新增文档后需同步更新
