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
    public void userJSONTypeTest() {
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
