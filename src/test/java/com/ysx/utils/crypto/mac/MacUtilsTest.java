package com.ysx.utils.crypto.mac;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/7/28 22:45
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class MacUtilsTest {

    // 256bit密钥
    private static final String SK_256_HEX_STRING = "19d567ea44325e4a1f3dd6806565c12b49a1ee12ff4e64ff49ed73ad89770231";
    // 512bit密钥
    private static final String SK_512_HEX_STRING = "86311e9c13e8bfa7460b3e65ee6ec980cc0cc98df0397996978bf214e4e6370b6369fe0982c0bff93162e102775118a9064b22df25c243dc2ff9c7654ae605d6";

    @Test
    public void macHmacSha256Test() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        byte[] mac = MacUtils.mac(Hex.decode(SK_256_HEX_STRING), data, "HmacSHA256");
        String excepted = "b6fc0aab65fba494114a819c3312051b4633697dce4a17ee5cec0c7d28bffe47";
        Assert.assertEquals(excepted, Hex.toHexString(mac));
    }

    @Test
    public void macHmacSha512Test() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        byte[] mac = MacUtils.mac(Hex.decode(SK_512_HEX_STRING), data, "HmacSHA512");
        String excepted = "fcb933ea55f3f658e10fbadfc6f0c30b4ccd2149ca5e1bfa05f679894a4453186b5a9129f43c0c94473ba4cca7cccd7b92688f8962e805b4cb7319ae0dd78bb2";
        Assert.assertEquals(excepted, Hex.toHexString(mac));
    }

    @Test
    public void macHmacSha3_256Test() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        byte[] mac = MacUtils.mac(Hex.decode(SK_256_HEX_STRING), data, "HmacSHA3-256");
        String excepted = "2bc501509aa28d8f54c67f7b7f085ec31b7cb5b41ba5d0c0a73dd84cae4c9597";
        Assert.assertEquals(excepted, Hex.toHexString(mac));
    }

    @Test
    public void macHmacSha3_512Test() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        byte[] mac = MacUtils.mac(Hex.decode(SK_512_HEX_STRING), data, "HmacSHA3-512");
        String excepted = "cac22721a2be5dbce4e06653cd9d77e013c2afa7042443d2431efc204c61e5dea6b11f1953b216e44e0ddcd22b2df64f3f9cb0fdefac52130c93a034026fc431";
        Assert.assertEquals(excepted, Hex.toHexString(mac));
    }

    @Test
    public void random() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64];
        secureRandom.nextBytes(key);
        System.out.println(Hex.toHexString(key));
    }

    @Test
    public void generateKeyTest() throws NoSuchAlgorithmException, NoSuchProviderException {
        String algorithmName = "HmacSHA256";
        System.out.println(Hex.toHexString(MacUtils.generateKey(algorithmName)));
    }
}
