# 使用BC进行RSA签名与验签

[项目地址](https://github.com/YoungBear/JavaUtils)

- signature 签名
- verify 验签



常量及静态方法：

```java
/**
 * 签名算法 SHA256withRSA 算法，PSS 填充模式
 */
private static final String SIGNATURE_ALGORITHM = "SHA256withRSA/PSS";

/**
 * 签名填充盐值长度
 */
private static final int SIGNATURE_SALT_LENGTH = 32;


static {
    Security.addProvider(new BouncyCastleProvider());
}
```



## 1. 签名

```java
/**
 * 签名
 *
 * @param privateKey 私钥
 * @param data       数据
 * @return 数据的签名值
 * @throws NoSuchAlgorithmException           异常
 * @throws NoSuchProviderException            异常
 * @throws InvalidAlgorithmParameterException 异常
 * @throws InvalidKeyException                异常
 * @throws SignatureException                 异常
 */
public static byte[] signature(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
    PSSParameterSpec pssParameterSpec = new PSSParameterSpec(MGF1ParameterSpec.SHA256.getDigestAlgorithm(),
            "MGF1", MGF1ParameterSpec.SHA256, SIGNATURE_SALT_LENGTH, 1);
    signature.setParameter(pssParameterSpec);
    signature.initSign(privateKey);
    signature.update(data);
    return signature.sign();
}
```



## 2. 验签

```java
/**
 * 验签
 *
 * @param publicKey 公钥
 * @param data      数据
 * @param sign      签名值
 * @return 验签是否成功
 * @throws NoSuchAlgorithmException           异常
 * @throws NoSuchProviderException            异常
 * @throws InvalidAlgorithmParameterException 异常
 * @throws InvalidKeyException                异常
 * @throws SignatureException                 异常
 */
public static boolean verify(PublicKey publicKey, byte[] data, byte[] sign) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
    PSSParameterSpec pssParameterSpec = new PSSParameterSpec(MGF1ParameterSpec.SHA256.getDigestAlgorithm(),
            "MGF1", MGF1ParameterSpec.SHA256, SIGNATURE_SALT_LENGTH, 1);
    signature.setParameter(pssParameterSpec);
    signature.initVerify(publicKey);
    signature.update(data);
    return signature.verify(sign);
}
```

