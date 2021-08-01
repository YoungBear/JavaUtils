# Mac 消息认证码算法Java实现

[项目地址](https://github.com/YoungBear/JavaUtils)

**算法描述：**

Mac：消息认证码 Message Authentication Code，带密钥的hash函数，用于保证消息数据完整性。

**常用算法：**(算法名大小写不敏感)

- HmacSHA256
- HmacSHA512
- HmacSHA3-256
- HmacSHA3-512



参考：

- https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Mac
- https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#Mac
- https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#HmacEx



**代码实现：**

```java
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
     * @param secretKey     密钥
     * @param data          数据
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

```



测试代码：

```java
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

```













