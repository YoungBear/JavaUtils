package com.ysx.utils.crypto.aes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/8/8 18:48
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class AESPractiseTest {

    @Test
    public void practiseTest() throws AESException {
        AESPractise practise = new AESPractise();
        String dataString = "Beijing 2008";
        byte[] data = dataString.getBytes(StandardCharsets.UTF_8);
        String encryptedString = practise.encrypt(data);
        System.out.println(encryptedString);

        String cipherString = "1#001#7505ff2cf7de25d90c157624f569e9ad#c58cd00ac8061ff1e49c7d7da5fd810cdd3e07725610e82356ea91b3";
        byte[] decryptedData = practise.decrypt(cipherString);
        Assertions.assertArrayEquals(data, decryptedData);
        Assertions.assertEquals(dataString, new String(decryptedData));

    }

    @Test
    public void practise2Test() throws AESException {
        AESPractise practise = new AESPractise();
        // 指定密钥id
        practise.setCurrentSecretKeyId("002");
        String dataString = "Beijing 2008";
        byte[] data = dataString.getBytes(StandardCharsets.UTF_8);
        String encryptedString = practise.encrypt(data);
        System.out.println(encryptedString);

        String cipherString = "1#002#c5dcfd831d2273efdf3d5f029b921022#464c587fef2ab29375db3668f23d788c9f5bf750777f5ab7b62a0c37";
        byte[] decryptedData = practise.decrypt(cipherString);
        Assertions.assertArrayEquals(data, decryptedData);
        Assertions.assertEquals(dataString, new String(decryptedData));
    }
}
