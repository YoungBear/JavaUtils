# 使用BC进行RSA加密与解密

[项目地址](https://github.com/YoungBear/JavaUtils)

- encrypt 加密
- decrypt 解密

常量及静态方法：

```java
/**
 * 加密算法
 */
private static final String ENCRYPT_ALGORITHM = "RSA/None/OAEPWithSHA-256AndMGF1Padding";

static {
    Security.addProvider(new BouncyCastleProvider());
}
```



## 1. 使用公钥加密

```java
/**
 * 使用公钥加密
 *
 * @param publicKey 公钥
 * @param data      待加密数据明文
 * @return 密文
 * @throws NoSuchPaddingException    异常
 * @throws NoSuchAlgorithmException  异常
 * @throws NoSuchProviderException   异常
 * @throws InvalidKeyException       异常
 * @throws IllegalBlockSizeException 异常
 * @throws BadPaddingException       异常
 */
public static byte[] encrypt(PublicKey publicKey, byte[] data)
        throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher.doFinal(data);
}
```



## 2. 使用私钥解密

```java
/**
 * 使用私钥解密
 *
 * @param privateKey 私钥
 * @param cipherData 待解密的密文
 * @return 明文
 * @throws NoSuchPaddingException    异常
 * @throws NoSuchAlgorithmException  异常
 * @throws NoSuchProviderException   异常
 * @throws InvalidKeyException       异常
 * @throws IllegalBlockSizeException 异常
 * @throws BadPaddingException       异常
 */
public static byte[] decrypt(PrivateKey privateKey, byte[] cipherData)
        throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return cipher.doFinal(cipherData);
}
```

