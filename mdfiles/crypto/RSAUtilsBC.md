# 使用BouncyCastle 实现RSA常用方法

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

- encrypt 加密
- decrypt 解密
- signature 签名
- verify 验签
- decodeEncryptedPrivateKey 从文件加载口令保护的私钥
- decodePrivateKey 从文件加载私钥（无口令保护）
- decodePublicKey 从文件加载公钥
- decodePublicKey2 从文件加载公钥2



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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
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

    /**
     * 加密算法
     */
    private static final String ENCRYPT_ALGORITHM = "RSA/None/OAEPWithSHA-256AndMGF1Padding";

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
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
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
    public void encryptTest() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        String publicKeyFilePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(publicKeyFilePath);
        byte[] data = "London2012".getBytes(StandardCharsets.UTF_8);
        byte[] cipherData = RSAUtilsBC.encrypt(publicKey, data);
        String cipherDataHexString = Hex.toHexString(cipherData);
        System.out.println("cipherDataHexString: " + cipherDataHexString);
    }

    @Test
    public void decryptTestSuccess() throws IOException, OperatorCreationException, PKCSException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        String privateKeyFilePath = BASE_PATH + "rsa_pss_private_3072_restricted_password.pem";
        String password = "Hello@123";
        PrivateKey privateKey = RSAUtilsBC.decodeEncryptedPrivateKey(privateKeyFilePath, password);
        String cipherDataHexString = "5ae58323930a9f0dac220483dba7e2dd1da4d56ebbfc458ff64eae0f1df599db0f3c4438e280108f1e443071efc3012b30b75a0aea565fe89d876da4d32f3574538c5410fd86531ec402775f1d3574343d726add0da44fc14b18716c4e4b2a4fc309036813b75ba5863515c394b1280ab9b94b26b576a7880c094f39bd25821992c7fd28271986eab957515458419014c1890c82a9c814bbc6eef0006fed23f62ea2a2e0f7a30cfaf5873f66267c4032e14773f2b965d9d66ad9ca492a8b847c59f454b6795d24456f288bb7420b8f5e62b34057057f339c01af5460f21e2f73dd1f90a8559b0bfbc8bea0003dcb0d9c1cda7b7f8fa169d636ddc1584d94b48bf0cfcd2f369fa3ce959ef3478b034c7253e738a7ac6b6cd999d57f7451752aa8fc6afdec6a1451b00eaef540f340c5a65d2b9084c3ad70d1e7dcf5b8847a14c7c1d2c92cf6f220d881582c7a5f5a03cba436223c5b0d4eb4b5fbc1357c477b435a16d9eb54ac9373c675ff50dc36b6b626547fe7930e8ec08676613787fcd7df";
        byte[] cipherData = Hex.decode(cipherDataHexString);
        byte[] plainData = RSAUtilsBC.decrypt(privateKey, cipherData);
        String plainText = new String(plainData, StandardCharsets.UTF_8);
        Assert.assertEquals("London2012", plainText);
    }

    @Test
    public void decryptTestFailed() throws IOException, OperatorCreationException, PKCSException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        String privateKeyFilePath = BASE_PATH + "rsa_pss_private_3072_restricted_password.pem";
        String password = "Hello@123";
        PrivateKey privateKey = RSAUtilsBC.decodeEncryptedPrivateKey(privateKeyFilePath, password);
        String cipherDataHexString = "5ae58323930a9f0dac220483dba7e2dd1da4d56ebbfc458ff64eae0f1df599db0f3c4438e280108f1e443071efc3012b30b75a0aea565fe89d876da4d32f3574538c5410fd86531ec402775f1d3574343d726add0da44fc14b18716c4e4b2a4fc309036813b75ba5863515c394b1280ab9b94b26b576a7880c094f39bd25821992c7fd28271986eab957515458419014c1890c82a9c814bbc6eef0006fed23f62ea2a2e0f7a30cfaf5873f66267c4032e14773f2b965d9d66ad9ca492a8b847c59f454b6795d24456f288bb7420b8f5e62b34057057f339c01af5460f21e2f73dd1f90a8559b0bfbc8bea0003dcb0d9c1cda7b7f8fa169d636ddc1584d94b48bf0cfcd2f369fa3ce959ef3478b034c7253e738a7ac6b6cd999d57f7451752aa8fc6afdec6a1451b00eaef540f340c5a65d2b9084c3ad70d1e7dcf5b8847a14c7c1d2c92cf6f220d881582c7a5f5a03cba436223c5b0d4eb4b5fbc1357c477b435a16d9eb54ac9373c675ff50dc36b6b626547fe7930e8ec08676613787fcd7df";
        byte[] cipherData = Hex.decode(cipherDataHexString);
        byte[] plainData = RSAUtilsBC.decrypt(privateKey, cipherData);
        String plainText = new String(plainData, StandardCharsets.UTF_8);
        Assert.assertNotEquals("London2013", plainText);
    }

    @Test
    public void signAndVerifyTest() throws IOException, OperatorCreationException, PKCSException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        String privateKeyFilePath = BASE_PATH + "rsa_pss_private_3072_restricted_password.pem";
        String password = "Hello@123";
        PrivateKey privateKey = RSAUtilsBC.decodeEncryptedPrivateKey(privateKeyFilePath, password);
        byte[] data = "Beijing2008".getBytes(StandardCharsets.UTF_8);
        byte[] signature = RSAUtilsBC.signature(privateKey, data);
        String signHexString = Hex.toHexString(signature);
        System.out.println("signHexString: " + signHexString);
    }

    @Test
    public void verifyTestSuccess() throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        byte[] data = "Beijing2008".getBytes(StandardCharsets.UTF_8);
        byte[] signature = Hex.decode("077bacf8b43da3f84b38bec809fabcf8c6dd321dde4987beca58c33fcfae7f71cfe1d0e19ec1b91802efc016da36e598e03599d6c0c6dad4209a3360d817c6119e4f8b69d342e9a263c67d03dd3212c35f34a23fcb7ae266a21f4277d0bd0d0783b6807b1f8157babc78a7ca84035029c4554e1dc843b17886d5c26d394d50be210140d40a117535a37cc20f26e93da53c52f8d112d9fbc9670fe1a08ba9753f4d204d37ffdf97f7539ddafbe95af10550b05a78a31eef9217059c556775f5bbdf88aba4ab5a30ac48989d428bb6c163910f3c34659e97b01388fb39b38c0e80b6ec69b359c30f114a9bf53253a60239d7fab2f4737af5fd9c5b15cee6c86bd65f9d89af77c93ccdf3510b9b2bb65b04020bd7570714cd230b89fb333ea0c126d59f9f40ef7e24af8bbf0eb3613e1e325251e9e6b9de2db2f19cca12b15583dfbceb56687113c8d46c4f0dbac8facde217ad2bed9c46d053d6458a64a439f6d5dbabc7e208c1e608318180882083890bd08c4dc89b9abb4084bc991901d4aaff");
        String publicKeyFilePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(publicKeyFilePath);
        boolean verify = RSAUtilsBC.verify(publicKey, data, signature);
        Assert.assertTrue(verify);
    }

    @Test
    public void verifyTestFailed() throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        byte[] data = "Beijing2008".getBytes(StandardCharsets.UTF_8);
        byte[] signature = Hex.decode("077bacf8b43da3f84b38bec809fabcf8c6dd321dde4987be");
        String publicKeyFilePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(publicKeyFilePath);
        boolean verify = RSAUtilsBC.verify(publicKey, data, signature);
        Assert.assertFalse(verify);
    }

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



