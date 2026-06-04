package com.ysx.utils.json.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
        lenientMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
