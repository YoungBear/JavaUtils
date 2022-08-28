package com.ysx.utils.pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-08-28 12:18
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description UUIDPattern 单元测试
 */
public class UUIDPatternJunit5Test {

    @DisplayName("DisplayName isValidUUIDTest")
    @Test
    public void isValidUUIDTest() {
        String nullStr = null;
        String emptyStr = "";
        String validUuidStr = "12467b39-de66-48f2-b4c3-5885d4dde759";
        String validUuidUpperStr = "12467b39-De66-48f2-b4c3-5885d4dde759";
        String inValidUuidStr = "g2467b39-de66-48f2-b4c3-5885d4dde759";

        Assertions.assertFalse(UUIDPattern.isValidUUID(nullStr));
        Assertions.assertFalse(UUIDPattern.isValidUUID(emptyStr));
        Assertions.assertTrue(UUIDPattern.isValidUUID(validUuidStr));
        Assertions.assertTrue(UUIDPattern.isValidUUID(validUuidUpperStr));
        Assertions.assertFalse(UUIDPattern.isValidUUID(inValidUuidStr));
    }

    @DisplayName("DisplayName isValidUUID32Test")
    @Test
    public void isValidUUID32Test() {
        String nullStr = null;
        String emptyStr = "";
        String validUuidStr = "12467b39de6648f2b4c35885d4dde759";
        String validUuidUpperStr = "12467b39De6648f2b4c35885d4dde759";
        String inValidUuidStr = "g2467b39de6648f2b4c35885d4dde759";
        String inValidUuid32Str = "12467b39-de66-48f2-b4c3-5885d4dde759";

        Assertions.assertFalse(UUIDPattern.isValidUUID32(nullStr));
        Assertions.assertFalse(UUIDPattern.isValidUUID32(emptyStr));
        Assertions.assertTrue(UUIDPattern.isValidUUID32(validUuidStr));
        Assertions.assertTrue(UUIDPattern.isValidUUID32(validUuidUpperStr));
        Assertions.assertFalse(UUIDPattern.isValidUUID32(inValidUuidStr));
        Assertions.assertFalse(UUIDPattern.isValidUUID32(inValidUuid32Str));
    }

    @DisplayName("DisplayName uuidTest")
    @Test
    public void uuidTest() {
        String uuid = UUIDPattern.uuid();
        Assertions.assertTrue(UUIDPattern.isValidUUID(uuid));
    }

    @DisplayName("DisplayName uuid32Test")
    @Test
    public void uuid32Test() {
        String uuid32 = UUIDPattern.uuid32();
        Assertions.assertTrue(UUIDPattern.isValidUUID32(uuid32));
    }

}
