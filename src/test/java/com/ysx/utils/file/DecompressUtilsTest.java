package com.ysx.utils.file;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2020/3/1 18:52
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class DecompressUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecompressUtilsTest.class);

    private static final String PATH = DecompressUtilsTest.class.getResource("/file/").getPath();

    @TempDir
    private Path tempDir;

    @Test
    @DisplayName("decompress .tar.gz successfully test")
    public void decompressTarGzSuccessTest() {
        String srcFile = PATH + "pom.tar.gz";
        String destDir = tempDir.toString();
        LOGGER.info("srcFile: {}, destDir: {}", srcFile, destDir);
        try {
            DecompressUtils.decompressTarGz(srcFile, destDir);
            Assertions.assertTrue(Files.exists(Paths.get(destDir + "/pom.xml")));
        } catch (FileException e) {
            LOGGER.error("decompressTarGzSuccessTest exception", e);
            Assertions.fail();
        }
    }
}
