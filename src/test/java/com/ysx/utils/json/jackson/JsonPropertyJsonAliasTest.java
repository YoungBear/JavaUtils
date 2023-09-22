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
