package com.ysx.utils.crypto.aes;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/8/8 9:45
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class AESUtilsTest {

    /**
     * 测试用的密钥，实践中需要随机生成，并使用密钥系统管理
     * 128bit
     */
    private static final String SECRET_KEY_HEX_STRING = "4bf05f9e002f650920cdc6a4e274c41a749f1995fa55958bdcb4cc3d1f029b70";

    /**
     * 测试用的IV值，实践中，每次加密随机生成
     * 128bit
     */
    private static final String IV_HEX_STRING = "775048c30bc37d567bef7681512dfb4b411fc030048912f208e83b598e690ad5";


    /**
     * 测试用的明文数据
     * 384bit
     */
    private static final String PLAIN_DATA_HEX_STRING = "d80517332cd3f7807ddcbf79c999f93483715716ec9b97094d65872650de633bb975aa3ac5587972eb2609e8935b7e09";

    /**
     * 测试用的密文数据（使用AES 256bit的密钥加密后，密文长度为256bit的整数倍）
     * 512bit
     */
    private static final String CIPHER_DATA_HEX_STRING = "3559d4020986bfd846a0cf24df9dea53fb0a90ec199ae6d488146e081e609e1a1957a9ad5650269f305a57c6f34477a8484d7a33e5348c5cf85b09d32eb82a59";


    @Test
    public void encryptSuccessTest()
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] secretKey = Hex.decode(SECRET_KEY_HEX_STRING);
        byte[] iv = Hex.decode(IV_HEX_STRING);
        byte[] plainData = Hex.decode(PLAIN_DATA_HEX_STRING);
        byte[] cipherData = AESUtils.encrypt(secretKey, iv, plainData);
        Assertions.assertArrayEquals(Hex.decode(CIPHER_DATA_HEX_STRING), cipherData);
        Assertions.assertEquals(CIPHER_DATA_HEX_STRING, Hex.toHexString(cipherData));
    }

    @Test
    public void encryptFailedTest()
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] secretKey = Hex.decode(SECRET_KEY_HEX_STRING);
        byte[] iv = Hex.decode(IV_HEX_STRING);
        // 不同的明文，密文不同
        byte[] plainData = Hex.decode(PLAIN_DATA_HEX_STRING + "fa");
        byte[] cipherData = AESUtils.encrypt(secretKey, iv, plainData);
        Assertions.assertNotEquals(CIPHER_DATA_HEX_STRING, Hex.toHexString(cipherData));
    }

    @Test
    public void decryptSuccessTest()
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] secretKey = Hex.decode(SECRET_KEY_HEX_STRING);
        byte[] iv = Hex.decode(IV_HEX_STRING);
        byte[] cipherData = Hex.decode(CIPHER_DATA_HEX_STRING);
        byte[] plainData = AESUtils.decrypt(secretKey, iv, cipherData);
        Assertions.assertArrayEquals(Hex.decode(PLAIN_DATA_HEX_STRING), plainData);
        Assertions.assertEquals(PLAIN_DATA_HEX_STRING, Hex.toHexString(plainData));
    }

    @Test
    public void decryptFailedTest()
            throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] secretKey = Hex.decode(SECRET_KEY_HEX_STRING);
        byte[] iv = Hex.decode(IV_HEX_STRING);
        byte[] cipherData = Hex.decode(CIPHER_DATA_HEX_STRING);
        byte[] plainData = AESUtils.decrypt(secretKey, iv, cipherData);
        // excepted 为错误的明文
        Assertions.assertNotEquals(PLAIN_DATA_HEX_STRING + "fa", Hex.toHexString(plainData));
    }


    /**
     * 生成随机数
     */
    private void random() {
        SecureRandom random = new SecureRandom();
        byte[] data = new byte[48];
        random.nextBytes(data);
        System.out.println(Hex.toHexString(data));
    }
}
