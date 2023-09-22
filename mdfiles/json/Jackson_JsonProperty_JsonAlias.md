## Jackson 中的 @JsonProperty 和 @JsonAlias 的区别



### 1. 背景

在Spring项目中，使用到Jackson用来做Json对象的序列化和反序列化。这里就涉及到了自定义字段名称，通常可以使用 `@JsonProperty` 和 `@JsonAlias` ，那这两者的区别是什么呢？

简单的说，**@JsonProperty 支持序列化和反序列化，而 @JsonAlias 仅支持反序列化**。

### 2. 验证

pom依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.14.3</version>
</dependency>
```



Java 验证程序：

```json
package com.ysx.utils.json.jackson;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-09-22 21:24
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description @JasonProperty和@JsonAlias的用法和区别
 */
public class JsonPropertyJsonAliasTest {

    @Test
    @DisplayName("序列化测试-@JsonProperty支持序列化-@JsonAlias不支持序列化")
    public void serializationTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TestModel model = new TestModel();
        model.setA1("a1Value");
        model.setB1("b1Value");
        String jsonString = objectMapper.writeValueAsString(model);
        // {"b1":"b1Value","a_1":"a1Value"}
        assertTrue(jsonString.contains("a_1"));
        assertFalse(jsonString.contains("b_1"));
    }

    @Test
    @DisplayName("反序列化测试-支持自定义字段名称-@JsonProperty支持反序列化-@JsonAlias支持反序列化")
    public void deserializationTest1() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "{\"b1\":\"b1Value\",\"a_1\":\"a1Value\"}";
        TestModel testModel = objectMapper.readValue(jsonString, TestModel.class);
        assertEquals("a1Value", testModel.getA1());
        assertEquals("b1Value", testModel.getB1());
    }

    @Test
    @DisplayName("反序列化测试-@JsonProperty仅支持自定义名称")
    public void deserializationTest2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "{\"a_1\":\"a1Value\"}";
        TestModel testModel = objectMapper.readValue(jsonString, TestModel.class);
        assertEquals("a1Value", testModel.getA1());

        String jsonString2 = "{\"a1\":\"a1Value\"}";
        Assertions.assertThrows(JsonProcessingException.class,
                () -> objectMapper.readValue(jsonString2, TestModel.class));
    }

    @Test
    @DisplayName("反序列化测试-@JsonAlias支持原有字段名称和自定义字段名称反序列化")
    public void deserializationTest3() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "{\"b_1\":\"b1Value\"}";
        TestModel testModel = objectMapper.readValue(jsonString, TestModel.class);
        assertEquals("b1Value", testModel.getB1());

        String jsonString2 = "{\"b1\":\"b1Value\"}";
        TestModel testModel2 = objectMapper.readValue(jsonString2, TestModel.class);
        assertEquals("b1Value", testModel2.getB1());
    }

    public static class TestModel {
        @JsonProperty("a_1")
        private String a1;

        @JsonAlias("b_1")
        private String b1;

        public String getA1() {
            return a1;
        }

        public void setA1(String a1) {
            this.a1 = a1;
        }

        public String getB1() {
            return b1;
        }

        public void setB1(String b1) {
            this.b1 = b1;
        }
    }
}

```





### 3. 总结

从验证程序可以得出以下结论：

- @JsonProperty 和 @JsonAlias 均支持反序列化（deserialization）时自定义字段名称。
- @JsonProperty 支持序列化（serialization）时自定义字段名称，而 @JsonAlias 不支持序列化时自定义字段。
- @JsonProperty 在反序列化时，仅支持自定义后的字段名称，不支持原有的字段名称；而 @JsonAlias 同时支持自定义和原有的字段名称。



这些可以从官方的API注释中获取到：

com.fasterxml.jackson.annotation.JsonProperty

```java
/**
 * Marker annotation that can be used to define a non-static
 * method as a "setter" or "getter" for a logical property
 * (depending on its signature),
 * or non-static object field to be used (serialized, deserialized) as
 * a logical property.
 * 省略其他注释
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonProperty
```

**解释：**

@JsonProperty 可以用在一个非静态的方法或者属性上，用来设置逻辑的属性名（支持序列化和反序列化）。



com.fasterxml.jackson.annotation.JsonAlias

```java
/**
 * Annotation that can be used to define one or more alternative names for
 * a property, accepted during deserialization as alternative to the official
 * name. Alias information is also exposed during POJO introspection, but has
 * no effect during serialization where primary name is always used.
 * 省略其他注释
 */
@Target({ElementType.ANNOTATION_TYPE, // for combo-annotations
    ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER// for properties (field, setter, ctor param)
})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonAlias
```

**解释：**

@JsonAlias 在反序列化时，支持了一个或者多个可选的名称，且不会对其本身的名称有影响。



### 4. 参考

- [官方API文档 jackson-annotations](https://www.javadoc.io/doc/com.fasterxml.jackson.core/jackson-annotations/latest/index.html)

