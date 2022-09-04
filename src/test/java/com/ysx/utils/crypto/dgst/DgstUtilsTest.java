package com.ysx.utils.crypto.dgst;

import com.ysx.utils.crypto.rsa.RSAUtilsBCTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/7/27 23:20
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class DgstUtilsTest {

    private static final String BASE_PATH = RSAUtilsBCTest.class.getResource("/digest/").getPath();

    @Test
    public void dgstSAH256Test() throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        String digest = DgstUtils.digest(data, "SHA-256");
        String excepted = "9e7076e2f8063e1f4b884014d3150a2d17f0ff92e681e41ee4aba1ca24a71a0f";
        Assertions.assertEquals(excepted, digest);
    }

    @Test
    public void dgstSAH512Test() throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        String digest = DgstUtils.digest(data, "SHA-512");
        String excepted = "5262b18dea4dfca9cc0d943f4e1872298868a8ca53f2eb9f014e75894d14b36c2e00842e049ca8d02c25a5736acbf1ed53534df18de5be51270967f34ba0a903";
        Assertions.assertEquals(excepted, digest);
    }

    @Test
    public void dgstSAH512_256Test() throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        String digest = DgstUtils.digest(data, "SHA-512/256");
        String excepted = "335685f9c5011dcfe1bf02f19983f486be87a87695999366343ade6db40657de";
        Assertions.assertEquals(excepted, digest);
    }

    @Test
    public void dgstSAH3_256Test() throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = "Lindon2012".getBytes(StandardCharsets.UTF_8);
        String digest = DgstUtils.digest(data, "SHA3-256");
        String excepted = "425e9cdd015f46b19c9b38bc64c0dfd624c96cd8d02a444892f8e312d9d303c0";
        Assertions.assertEquals(excepted, digest);
    }

    @Test
    public void dgstFileSAH256Test() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        File file = new File(BASE_PATH + "Lindon2012.txt");
        // sha256sum Lindon2012.txt
        // openssl dgst -sha256 Lindon2012.txt
        String digest = DgstUtils.digest(file, "SHA-256");
        String excepted = "9ccad67beb3a52c661da1483ae898c679c32814849daeb95bf7f5f15f19c443a";
        Assertions.assertEquals(excepted, digest);
    }

    @Test
    public void dgstFileSAH512Test() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        File file = new File(BASE_PATH + "Lindon2012.txt");
        // sha512sum Lindon2012.txt
        // openssl dgst -sha512 Lindon2012.txt
        String digest = DgstUtils.digest(file, "SHA-512");
        String excepted = "a29f01307e077502ae822522801143ac63209222bcf4a1f593fcdb76beed6cd73873e25904b7969f4a22ecb74f0a18fdef12c6d8ff1859ca9d2466a1a3f63f01";
        Assertions.assertEquals(excepted, digest);
    }

    @Test
    public void dgstFileSAH512_256Test() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        File file = new File(BASE_PATH + "Lindon2012.txt");
        // openssl dgst -sha512-256 Lindon2012.txt
        String digest = DgstUtils.digest(file, "SHA-512/256");
        String excepted = "8e9c612a1b2183ccaaf7f574f28270a18e44f03356b2dfbc09c4a257767bd970";
        Assertions.assertEquals(excepted, digest);
    }

    @Test
    public void dgstFileSAH3_256Test() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        File file = new File(BASE_PATH + "Lindon2012.txt");
        // openssl dgst -sha3-256 Lindon2012.txt
        String digest = DgstUtils.digest(file, "SHA3-256");
        String excepted = "35133fe1494d629118b64db91dc8a1b86648003908aef3a94732efcea9b0e488";
        Assertions.assertEquals(excepted, digest);
    }
}
