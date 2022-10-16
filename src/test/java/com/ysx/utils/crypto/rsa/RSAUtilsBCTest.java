package com.ysx.utils.crypto.rsa;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
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
        Assertions.assertEquals("London2012", plainText);
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
        Assertions.assertNotEquals("London2013", plainText);
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
        Assertions.assertTrue(verify);
    }

    @Test
    public void verifyTestFailed() throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        byte[] data = "Beijing2008".getBytes(StandardCharsets.UTF_8);
        byte[] signature = Hex.decode("077bacf8b43da3f84b38bec809fabcf8c6dd321dde4987be");
        String publicKeyFilePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(publicKeyFilePath);
        boolean verify = RSAUtilsBC.verify(publicKey, data, signature);
        Assertions.assertFalse(verify);
    }

    @Test
    public void decodeEncryptedPrivateKeyTest() throws IOException, OperatorCreationException, PKCSException {
        String filePath = BASE_PATH + "rsa_pss_private_3072_restricted_password.pem";
        String password = "Hello@123";
        PrivateKey privateKey = RSAUtilsBC.decodeEncryptedPrivateKey(filePath, password);
        String algorithm = privateKey.getAlgorithm();
        int length = ((RSAPrivateKey) privateKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assertions.assertEquals("RSASSA-PSS", algorithm);
        Assertions.assertEquals(3072, length);
    }

    @Test
    public void decodePrivateKeyTest() throws IOException {
        String filePath = BASE_PATH + "rsa_pss_private_3072_restricted_nopassword.pem";
        PrivateKey privateKey = RSAUtilsBC.decodePrivateKey(filePath);
        String algorithm = privateKey.getAlgorithm();
        int length = ((RSAPrivateKey) privateKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assertions.assertEquals("RSASSA-PSS", algorithm);
        Assertions.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKeyTest() throws IOException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assertions.assertEquals("RSASSA-PSS", algorithm);
        Assertions.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKeyTest2() throws IOException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_nopassword.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assertions.assertEquals("RSASSA-PSS", algorithm);
        Assertions.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKey2Test() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_password.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey2(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assertions.assertEquals("RSASSA-PSS", algorithm);
        Assertions.assertEquals(3072, length);
    }

    @Test
    public void decodePublicKey2Test2() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        String filePath = BASE_PATH + "rsa_pss_public_3072_restricted_nopassword.pem";
        PublicKey publicKey = RSAUtilsBC.decodePublicKey2(filePath);
        String algorithm = publicKey.getAlgorithm();
        int length = ((RSAPublicKey) publicKey).getModulus().bitLength();
        LOGGER.info("algorithm: {}, length: {}", algorithm, length);
        Assertions.assertEquals("RSASSA-PSS", algorithm);
        Assertions.assertEquals(3072, length);
    }
}
