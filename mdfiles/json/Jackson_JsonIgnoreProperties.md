## Jackson 中的 @JsonIgnoreProperties(ignoreUnknown = true) 的用法



### 1. 背景

在开发中，使用 Jackson 进行 JSON 反序列化时，默认情况下如果 JSON 字符串中包含 Java 类中不存在的字段，Jackson 会抛出 `UnrecognizedPropertyException` 异常。

这种场景常见于：
- 调用第三方 API 时，对方新增了字段，但本地的 Java 类还未更新
- 只需要 JSON 中的部分字段，忽略其他不需要的字段
- 后端 API 升级新增字段后，旧版本客户端反序列化时兼容处理

使用 `@JsonIgnoreProperties(ignoreUnknown = true)` 注解，可以让 Jackson 在反序列化时忽略未知字段，避免抛出异常，提高程序的鲁棒性。

除了在类上使用注解外，也可以使用 `ObjectMapper` 全局配置 `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES` 为 `false` 来达到相同效果。

### 2. 验证

pom依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.1</version>
</dependency>
```



Java 验证程序：

```java
package com.ysx.utils.json.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2025-08-01 21:30
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description @JsonIgnoreProperties(ignoreUnknown = true) 的用法
 */
@DisplayName("@JsonIgnoreProperties(ignoreUnknown = true) 反序列化时忽略未知字段")
public class JsonIgnorePropertiesTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("反序列化-默认行为-含有未知字段时抛出UnrecognizedPropertyException异常")
    public void defaultBehaviorTest() {
        String jsonString = "{\"name\":\"John\",\"age\":25,\"unknownField\":\"unexpected\"}";
        Assertions.assertThrows(UnrecognizedPropertyException.class,
                () -> objectMapper.readValue(jsonString, User1.class));
    }

    @Test
    @DisplayName("反序列化-使用@JsonIgnoreProperties(ignoreUnknown = true) 忽略未知字段")
    public void ignoreUnknownTest() throws JsonProcessingException {
        String jsonString = "{\"name\":\"John\",\"age\":25,\"unknownField\":\"unexpected\"}";
        User2 user = objectMapper.readValue(jsonString, User2.class);
        Assertions.assertEquals("John", user.getName());
        Assertions.assertEquals(25, user.getAge());
    }

    @Test
    @DisplayName("反序列化-无未知字段时正常解析")
    public void noUnknownFieldTest() throws JsonProcessingException {
        String jsonString = "{\"name\":\"John\",\"age\":25}";
        User1 user = objectMapper.readValue(jsonString, User1.class);
        Assertions.assertEquals("John", user.getName());
        Assertions.assertEquals(25, user.getAge());
    }

    @Test
    @DisplayName("测试全局ObjectMapper配置忽略未知字段")
    public void globalConfigureTest() throws JsonProcessingException {
        String jsonString = "{\"name\":\"John\",\"age\":25,\"unknownField\":\"unexpected\"}";

        // 默认 ObjectMapper 会抛出异常
        Assertions.assertThrows(UnrecognizedPropertyException.class,
                () -> objectMapper.readValue(jsonString, User1.class));

        // 配置全局忽略未知字段
        ObjectMapper lenientMapper = new ObjectMapper();
        lenientMapper.configure(
                com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        User1 user = lenientMapper.readValue(jsonString, User1.class);
        Assertions.assertEquals("John", user.getName());
        Assertions.assertEquals(25, user.getAge());
    }

    @Getter
    @Setter
    public static class User1 {
        private String name;
        private Integer age;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User2 {
        private String name;
        private Integer age;
    }
}

```



### 3. 总结

从验证程序可以得出以下结论：

- Jackson 反序列化时，默认如果存在未知字段，会抛出 `UnrecognizedPropertyException` 异常。
- 使用 `@JsonIgnoreProperties(ignoreUnknown = true)` 注解标注在类上，可以在反序列化时忽略未知字段，不会抛出异常。
- 该注解**仅影响反序列化（deserialization）**，对序列化（serialization）没有影响。
- 也可以通过 `ObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)` 全局配置忽略未知属性，适合需要统一处理的项目。
- 适用场景：调用第三方 API、JSON 数据不完全匹配、API 版本兼容等。



### 4. 参考

- [官方 API 文档 jackson-annotations](https://www.javadoc.io/doc/com.fasterxml.jackson.core/jackson-annotations/latest/index.html)
- [github地址](https://github.com/YoungBear/JavaUtils)
