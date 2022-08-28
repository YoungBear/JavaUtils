package com.ysx.utils.pattern;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022-08-28 12:18
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description UUIDPattern 单元测试
 */
public class UUIDPatternTest {

    @Test
    public void isValidUUIDTest() {
        String nullStr = null;
        String emptyStr = "";
        String validUuidStr = "12467b39-de66-48f2-b4c3-5885d4dde759";
        String validUuidUpperStr = "12467b39-De66-48f2-b4c3-5885d4dde759";
        String inValidUuidStr = "g2467b39-de66-48f2-b4c3-5885d4dde759";

        Assert.assertFalse(UUIDPattern.isValidUUID(nullStr));
        Assert.assertFalse(UUIDPattern.isValidUUID(emptyStr));
        Assert.assertTrue(UUIDPattern.isValidUUID(validUuidStr));
        Assert.assertTrue(UUIDPattern.isValidUUID(validUuidUpperStr));
        Assert.assertFalse(UUIDPattern.isValidUUID(inValidUuidStr));
    }

    @Test
    public void isValidUUID32Test() {
        String nullStr = null;
        String emptyStr = "";
        String validUuidStr = "12467b39de6648f2b4c35885d4dde759";
        String validUuidUpperStr = "12467b39De6648f2b4c35885d4dde759";
        String inValidUuidStr = "g2467b39de6648f2b4c35885d4dde759";
        String inValidUuid32Str = "12467b39-de66-48f2-b4c3-5885d4dde759";

        Assert.assertFalse(UUIDPattern.isValidUUID32(nullStr));
        Assert.assertFalse(UUIDPattern.isValidUUID32(emptyStr));
        Assert.assertTrue(UUIDPattern.isValidUUID32(validUuidStr));
        Assert.assertTrue(UUIDPattern.isValidUUID32(validUuidUpperStr));
        Assert.assertFalse(UUIDPattern.isValidUUID32(inValidUuidStr));
        Assert.assertFalse(UUIDPattern.isValidUUID32(inValidUuid32Str));
    }

    @Test
    public void uuidTest() {
        String uuid = UUIDPattern.uuid();
        Assert.assertTrue(UUIDPattern.isValidUUID(uuid));
    }

    @Test
    public void uuid32Test() {
        String uuid32 = UUIDPattern.uuid32();
        Assert.assertTrue(UUIDPattern.isValidUUID32(uuid32));
    }

}
