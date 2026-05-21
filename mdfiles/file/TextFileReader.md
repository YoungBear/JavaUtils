# TextFileReader - 文本文件读取工具

## 概述

`TextFileReader` 是一个用于读取文本文件的工具类，支持自动识别文件编码格式，无需手动指定编码即可正确读取文件内容。

## 功能特性

- **自动编码检测** - 支持 UTF-8、UTF-16、GBK、GB2312、GB18030、ISO-8859-1、ASCII、Big5、Shift_JIS、EUC-KR 等常见编码
- **BOM 处理** - 支持带 BOM 头的文件，自动识别并正确处理
- **编码验证** - 对每种编码进行有效性验证，确保解码正确

## 编码检测策略

1. **BOM 检测** - 优先检测字节顺序标记 (Byte Order Mark)
2. **UTF-16 模式检测** - 通过分析 NULL 字节模式判断是否为 UTF-16BE/LE 无 BOM 情况
3. **UTF-8 有效性验证** - 验证字节序列是否符合 UTF-8 编码规范
4. **常见编码尝试** - 按优先级尝试支持的各种编码

## 使用示例

### 基本用法

```java
import com.ysx.utils.file.TextFileReader;
import com.ysx.utils.file.FileException;

// 读取文件，自动检测编码
String content = TextFileReader.readFile("path/to/file.txt");
System.out.println(content);
```

### 指定文件对象读取

```java
import java.io.File;

File file = new File("example.txt");
try {
    String content = TextFileReader.readFile(file);
    System.out.println(content);
} catch (FileException e) {
    System.err.println("读取文件失败: " + e.getMessage());
}
```

### 单独检测编码

```java
import java.nio.charset.Charset;

// 检测字节数组的编码
byte[] bytes = Files.readAllBytes(Paths.get("file.txt"));
Charset charset = TextFileReader.detectCharset(bytes);
System.out.println("检测到的编码: " + charset.name());
```

### 获取支持的编码列表

```java
String[] supportedCharsets = TextFileReader.getSupportedCharsetNames();
for (String charset : supportedCharsets) {
    System.out.println(charset);
}
```

## API 参考

### 主要方法

| 方法 | 说明 |
|------|------|
| `readFile(String filePath)` | 根据文件路径读取文件 |
| `readFile(File file)` | 根据 File 对象读取文件 |
| `detectCharset(byte[] bytes)` | 检测字节数组的字符编码 |
| `getSupportedCharsetNames()` | 获取支持的编码名称数组 |

### 异常处理

所有读取方法在以下情况会抛出 `FileException`：
- 文件路径为 `null`
- 文件不存在
- 文件不是常规文件
- 读取过程中发生 IO 错误

## 注意事项

1. **大文件处理** - 内部使用 8KB 缓冲区读取，适合中小型文本文件
2. **编码优先级** - UTF-8 优先级最高，如果文件同时满足多种编码条件，优先返回 UTF-8
3. **UTF-16 检测** - 对于无 BOM 的 UTF-16 编码，通过分析 NULL 字节模式判断字节序

## 测试覆盖

- UTF-8 文件读写（含/不含 BOM）
- UTF-16BE/LE 文件读写
- GBK 中文编码
- ISO-8859-1 西文编码
- 空文件处理
- 异常场景（null 参数、文件不存在）
