package com.ysx.utils.crypto.aes;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/8/8 9:34
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * AES 加解密
 * 实践中，可以封装加解密方法，封装异常抛出统一异常
 * 密文格式一般为：加密算法id+密钥id+iv值+密文数据
 * 结合密钥管理系统，缓存，实现加解密
 * 本示例使用 AES/GCM/NoPadding
 */
public class AESUtils {

    /**
     * AES 加密算法
     * AES/GCM/NoPadding
     */
    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";

    /**
     * GCM 消息认证码长度(bit) 128bit
     */
    private static final int GCM_AUTH_TAG_LENGTH = 8 * 16;

    /**
     * AES 算法名称
     */
    private static final String AES_ALGORITHM_NAME = "AES";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     *
     * @param secretKey 密钥
     * @param iv        iv值
     * @param plainData 待加密的明文数据
     * @return 密文
     * @throws NoSuchPaddingException             异常
     * @throws NoSuchAlgorithmException           异常
     * @throws NoSuchProviderException            异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws InvalidKeyException                异常
     * @throws IllegalBlockSizeException          异常
     * @throws BadPaddingException                异常
     */
    public static byte[] encrypt(byte[] secretKey, byte[] iv, byte[] plainData)
            throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        AlgorithmParameterSpec parameterSpec = new GCMParameterSpec(GCM_AUTH_TAG_LENGTH, iv);
        Key secretKeySpec = new SecretKeySpec(secretKey, AES_ALGORITHM_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);
        return cipher.doFinal(plainData);
    }

    /**
     * 解密
     *
     * @param secretKey  密钥
     * @param iv         iv值
     * @param cipherData 待解密的密文数据
     * @return 明文
     * @throws NoSuchPaddingException             异常
     * @throws NoSuchAlgorithmException           异常
     * @throws NoSuchProviderException            异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws InvalidKeyException                异常
     * @throws IllegalBlockSizeException          异常
     * @throws BadPaddingException                异常
     */
    public static byte[] decrypt(byte[] secretKey, byte[] iv, byte[] cipherData)
            throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
        AlgorithmParameterSpec parameterSpec = new GCMParameterSpec(GCM_AUTH_TAG_LENGTH, iv);
        Key secretKeySpec = new SecretKeySpec(secretKey, AES_ALGORITHM_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);
        return cipher.doFinal(cipherData);
    }
}
