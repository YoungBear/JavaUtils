## 设置Json序列化时字段的顺序



### 1. 背景

在部分使用场景（如元数据驱动，后台接口仅返回序列化后的json字符串，前端需要根据每个字段在前端呈现），需要手动设置字段的长度。通常情况下，框架是有默认的顺序，如 jackson 默认使用字段声明的顺序， fastjson 默认是使用字典序。在这种业务场景下，就需要我们可以手动指定序列化后字段的顺序。

这里分别使用 jackson 和 fastjson 两种框架。

### 2. 使用 jackson

使用注解 @JsonPropertyOrder 声明具体的字段顺序。如 `@JsonPropertyOrder({"city", "age", "name"})` 。具体参考实例程序。



pom依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.1</version>
</dependency>
```



Java 验证程序：

```json
package com.ysx.utils.json.jackson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-10-15 22:15
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
@DisplayName("使用jackson设置序列化时字段的顺序")
public class JsonFieldOrderTest {
    @Test
    @DisplayName("默认顺序:字段声明的顺序")
    public void defaultOrderTest() throws JsonProcessingException {
        Student1 student = new Student1();
        student.setName("John");
        student.setAge(25);
        student.setCity("Beijing");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(student);
        // {"name":"John","age":25,"city":"Beijing"}
        Assertions.assertEquals("{\"name\":\"John\",\"age\":25,\"city\":\"Beijing\"}", json);
    }

    @Test
    @DisplayName("使用@JsonPropertyOrder指定顺序")
    public void userJsonPropertyOrderTest() throws JsonProcessingException {
        Student2 student = new Student2();
        student.setName("John");
        student.setAge(25);
        student.setCity("Beijing");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(student);
        // {"city":"Beijing","age":25,"name":"John"}
        Assertions.assertEquals("{\"city\":\"Beijing\",\"age\":25,\"name\":\"John\"}", json);
    }

    @Getter
    @Setter
    public static class Student1 {
        private String name;
        private Integer age;
        private String city;
    }

    @Getter
    @Setter
    @JsonPropertyOrder({"city", "age", "name"})
    public static class Student2 {
        private String name;
        private Integer age;
        private String city;
    }
}

```





### 3. 使用 fastjson

有两种方法，

- 使用 @JsonField 的 ordinal 指定顺序，数越小优先级越高，默认为0。
- 使用@JSONType的orders属性指定字段顺序

详细参考实例程序：



pom依赖：

```xml
<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>2.0.41</version>
</dependency>
```



程序：

```json
package com.ysx.utils.json.fastjson;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-10-15 21:28
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */
@DisplayName("使用fastjson设置序列化时字段的顺序")
public class JsonFieldOrderTest {

    @Test
    @DisplayName("默认顺序:字典序")
    public void defaultOrderTest() {
        Student1 student = new Student1();
        student.setName("John");
        student.setAge(25);
        student.setCity("Beijing");
        String json = JSON.toJSONString(student);
        // {"age":25,"city":"Beijing","name":"John"}
        Assertions.assertEquals("{\"age\":25,\"city\":\"Beijing\",\"name\":\"John\"}", json);
    }

    @Test
    @DisplayName("使用@JsonField的ordinal指定顺序，数越小优先级越高，默认为0")
    public void userJsonFieldOrdinalTest() {
        Student2 student = new Student2();
        student.setName("John");
        student.setAge(25);
        student.setCity("Beijing");
        String json = JSON.toJSONString(student);
        // {"name":"John","age":25,"city":"Beijing"}
        Assertions.assertEquals("{\"name\":\"John\",\"age\":25,\"city\":\"Beijing\"}", json);
    }

    @Test
    @DisplayName("使用@JSONType的orders属性指定字段顺序")
    public void userJsonPropertyOrderTest() {
        Student3 student = new Student3();
        student.setName("John");
        student.setAge(25);
        student.setCity("Beijing");
        String json = JSON.toJSONString(student);
        // {"name":"John","age":25,"city":"Beijing"}
        Assertions.assertEquals("{\"name\":\"John\",\"age\":25,\"city\":\"Beijing\"}", json);
    }

    @Getter
    @Setter
    public static class Student1 {
        private String name;
        private Integer age;
        private String city;
    }

    @Getter
    @Setter
    public static class Student2 {
        @JSONField(ordinal = 1)
        private String name;
        @JSONField(ordinal = 2)
        private Integer age;
        @JSONField(ordinal = 3)
        private String city;
    }

    @Getter
    @Setter
    @JSONType(orders = {"name", "age", "city"})
    public static class Student3 {
        private String name;
        private Integer age;
        private String city;
    }
}

```



## [源代码github地址](https://github.com/YoungBear/JavaUtils)
