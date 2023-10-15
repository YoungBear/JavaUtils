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
