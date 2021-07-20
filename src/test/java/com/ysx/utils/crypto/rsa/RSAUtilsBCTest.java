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
