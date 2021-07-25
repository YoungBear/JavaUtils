# 使用BC从文件解析RSA私钥和公钥

[项目地址](https://github.com/YoungBear/JavaUtils)

- decodeEncryptedPrivateKey 从文件加载口令保护的私钥
- decodePrivateKey 从文件加载私钥（无口令保护）
- decodePublicKey 从文件加载公钥
- decodePublicKey2 从文件加载公钥2

常量及静态方法：

```java
static {
    Security.addProvider(new BouncyCastleProvider());
}
```



## 1. 从文件加载口令保护的私钥：

```java
/**
 * 从文件加载口令保护的私钥
 *
 * @param filePath 私钥文件路径
 * @param password 口令
 * @return 私钥
 * @throws IOException               异常
 * @throws OperatorCreationException 异常
 * @throws PKCSException             异常
 */
public static PrivateKey decodeEncryptedPrivateKey(String filePath, String password) throws IOException, OperatorCreationException, PKCSException {
    try (PEMParser pemParser = new PEMParser(new FileReader(filePath))) {
        Object pem = pemParser.readObject();
        if (pem instanceof PKCS8EncryptedPrivateKeyInfo) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME);
            PKCS8EncryptedPrivateKeyInfo keyInfo = (PKCS8EncryptedPrivateKeyInfo) pem;
            InputDecryptorProvider pkcs8Prov = new JceOpenSSLPKCS8DecryptorProviderBuilder()
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                    .build(password.toCharArray());
            return converter.getPrivateKey(keyInfo.decryptPrivateKeyInfo(pkcs8Prov));
        }
        throw new RuntimeException("invalid key file.");
    }
}
```



## 2. 从文件加载私钥（无口令保护）

```java
/**
 * 从文件加载私钥（无口令保护）
 *
 * @param filePath 私钥文件路径
 * @return 私钥
 * @throws IOException 异常
 */
public static PrivateKey decodePrivateKey(String filePath) throws IOException {
    try (PEMParser pemParser = new PEMParser(new FileReader(filePath))) {
        Object pem = pemParser.readObject();
        if (pem instanceof PrivateKeyInfo) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                    .setProvider(BouncyCastleProvider.PROVIDER_NAME);
            PrivateKeyInfo keyInfo = (PrivateKeyInfo) pem;
            return converter.getPrivateKey(keyInfo);
        }
        throw new RuntimeException("invalid key file.");
    }
}
```



## 3. 从文件加载公钥

```java
/**
 * 从文件加载公钥
 *
 * @param filePath 公钥文件路径
 * @return 私钥
 * @throws IOException 异常
 */
public static PublicKey decodePublicKey(String filePath) throws IOException {
    try (PEMParser pemParser = new PEMParser(new FileReader(filePath))) {
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME);
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
        return converter.getPublicKey(publicKeyInfo);
    }
}
```



## 4. 从文件加载公钥2

```java
/**
 * 从文件加载公钥2
 *
 * @param filePath 公钥文件路径
 * @return 公钥
 * @throws IOException              异常
 * @throws NoSuchAlgorithmException 异常
 * @throws InvalidKeySpecException  异常
 * @throws NoSuchProviderException  异常
 */
public static PublicKey decodePublicKey2(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
    try (PEMParser pemParser = new PEMParser(new FileReader(filePath))) {
        PemObject pemObject = pemParser.readPemObject();
        byte[] content = pemObject.getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        KeyFactory factory = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
        return factory.generatePublic(pubKeySpec);
    }
}
```

