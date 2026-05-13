# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

JavaUtils 是一个 Java 工具库，提供了各类常用工具方法的实现与演示，涵盖加解密、日期时间、正则表达式、文件处理、安全和线程池等领域。

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

# 跳过测试
mvn compile -DskipTests

# 清理并打包
mvn clean package
```

## 项目结构

### 包组织

```
src/main/java/com/ysx/utils/
├── crypto/       # 加解密相关 (RSA、AES、MAC、MessageDigest、Base64)
├── datetime/     # 日期时间处理
├── file/         # 文件操作
├── pattern/      # 正则表达式、UUID、版本号、IP验证
├── security/     # 安全相关 (密码工具类)
└── threadpool/   # 线程池配置
```

### 测试结构

```
src/test/java/com/ysx/utils/
├── crypto/       # 加解密测试
├── datetime/     # 日期时间测试
├── log/          # 日志测试
├── mockito/      # Mockito 测试示例
├── pattern/      # 正则表达式测试
├── security/     # 安全测试
└── threadpool/   # 线程池测试
```

### 文档结构

`mdfiles/` 目录下包含各类工具的详细文档，按类别组织在子目录中（crypto、datetime、file、pattern、security 等）。

## 技术栈

- **Java 版本**: 1.8
- **构建工具**: Maven
- **测试框架**: JUnit 5 (Jupiter) + Mockito
- **主要依赖**: BouncyCastle (密码学)、Jackson/Fastjson2/Gson (JSON)、Logback (日志)、Lombok

## 开发注意事项

- 所有源文件使用 UTF-8 编码
- 编译器启用了 `-Xlint:all` 警告检查
- 测试代码位于 `src/test/java/` 目录，测试资源位于 `src/test/resources/`
