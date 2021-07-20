# 使用BouncyCastle 解析私钥

[项目地址](https://github.com/YoungBear/JavaUtils)



相关openssl命令

```shell
# OpenSSL 1.1.1h  22 Sep 2020

# 生成一个3072bit的RSA私钥
openssl genpkey -algorithm RSA-PSS -pkeyopt rsa_keygen_bits:3072 -pkeyopt rsa_keygen_pubexp:65537 -pkeyopt rsa_pss_keygen_md:sha256 -pkeyopt rsa_pss_keygen_mgf1_md:sha256 -pkeyopt rsa_pss_keygen_saltlen:32 -out rsa_pss_private_3072_restricted_nopassword.pem
# 查看私钥
openssl pkey -in rsa_pss_private_3072_restricted_nopassword.pem -text
# 导出公钥
openssl pkey -in rsa_pss_private_3072_restricted_nopassword.pem -out rsa_pss_public_3072_restricted_nopassword.pem -pubout

# 生成一个口令保护的3072bit的RSA私钥 Hello@123
openssl genpkey -aes256 -algorithm RSA-PSS -pkeyopt rsa_keygen_bits:3072 -pkeyopt rsa_keygen_pubexp:65537 -pkeyopt rsa_pss_keygen_md:sha256 -pkeyopt rsa_pss_keygen_mgf1_md:sha256 -pkeyopt rsa_pss_keygen_saltlen:32 -out rsa_pss_private_3072_restricted_password.pem
# 查看私钥
openssl pkey -in rsa_pss_private_3072_restricted_password.pem -text
# 导出公钥
openssl pkey -in rsa_pss_private_3072_restricted_password.pem -out rsa_pss_public_3072_restricted_password.pem -pubout
```





## 1. 引入pom依赖

```xml
        <bouncycastle.version>1.69</bouncycastle.version>
        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcprov-jdk15on</artifactId>
            <version>${bouncycastle.version}</version>
        </dependency>

        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcpkix-jdk15on</artifactId>
            <version>${bouncycastle.version}</version>
        </dependency>
```

## 2. RSA 常用方法

```java
package com.ysx.utils.crypto.rsa;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/7/20 23:29
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 使用BouncyCastle RSA 工具类
 * 参考：https://www.baeldung.com/java-read-pem-file-keys
 */
public class RSAUtilsBC {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

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

}

```



## 3. 测试类

```java
package com.ysx.utils.crypto.rsa;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/7/20 23:48
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class RSAUtilsBCTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtilsBCTest.class);

    private static final String BASE_PATH = RSAUtilsBCTest.class.getResource("/openssl/").getPath();

    @Test
    public void decodeEncryptedPrivateKeyTest() throws IOException, OperatorCreationException, PKCSException {
        String filePath = BASE_PATH + "rsa_pss_private_3072_restricted_password.pem";
        String password = "Hello@123";
        PrivateKey privateKey = RSAUtilsBC.decodeEncryptedPrivateKey(filePath, password);
        String algorithm = privateKey.getAlgorithm();
        int length = ((RSAPrivateKey) privateKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assert.assertEquals("RSASSA-PSS", algorithm);
        Assert.assertEquals(3072, length);
    }

    @Test
    public void decodePrivateKeyTest() throws IOException {
        String filePath = BASE_PATH + "rsa_pss_private_3072_restricted_nopassword.pem";
        PrivateKey privateKey = RSAUtilsBC.decodePrivateKey(filePath);
        String algorithm = privateKey.getAlgorithm();
        int length = ((RSAPrivateKey) privateKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assert.assertEquals("RSASSA-PSS", algorithm);
        Assert.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKeyTest() throws IOException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assert.assertEquals("RSASSA-PSS", algorithm);
        Assert.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKeyTest2() throws IOException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_nopassword.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assert.assertEquals("RSASSA-PSS", algorithm);
        Assert.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKey2Test() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey2(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assert.assertEquals("RSASSA-PSS", algorithm);
        Assert.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKey2Test2() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_nopassword.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey2(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assert.assertEquals("RSASSA-PSS", algorithm);
        Assert.assertEquals(3072, length);
    }
}

```



