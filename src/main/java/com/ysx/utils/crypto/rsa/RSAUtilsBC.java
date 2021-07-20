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
 * todo 添加加解密，签名，验签方法
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
