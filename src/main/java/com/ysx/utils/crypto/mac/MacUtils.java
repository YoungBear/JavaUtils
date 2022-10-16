package com.ysx.utils.crypto.mac;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import java.util.List;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/7/28 22:44
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description Mac 工具类
 * 参考：https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#HmacEx
 * 实践中，需要保证密钥长度必须不小于算法长度，比如HmacSHA256算法，则密钥长度需要不小于256bit
 * Mac：消息认证码 Message Authentication Code，带密钥的hash函数，用于保证消息数据完整性。
 */
public class MacUtils {

    /**
     * 常用mac算法
     * 算法名称是大小写不敏感的
     * 参考：https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Mac
     */
    private static final List<String> MAC_ALGORITHM = Arrays.asList(
            "HmacSHA256",
            "HmacSHA512",
            "HmacSHA3-256",
            "HmacSHA3-512"

    );

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 计算数据的mac值
     *
     * @param secretKey 密钥
     * @param data 数据
     * @param algorithmName 算法
     * @return mac值
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException  异常
     * @throws InvalidKeyException      异常
     */
    public static byte[] mac(byte[] secretKey, byte[] data, String algorithmName)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        SecretKey sk = new SecretKeySpec(secretKey, algorithmName);
        Mac instance = Mac.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        instance.init(sk);
        return instance.doFinal(data);
    }

    /**
     * 生成密钥
     *
     * @param algorithmName 算法名
     * @return 密钥字节数组
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException  异常
     */
    public static byte[] generateKey(String algorithmName)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        SecretKey sk = kg.generateKey();
        return sk.getEncoded();
    }
}
