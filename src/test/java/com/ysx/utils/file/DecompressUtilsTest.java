package com.ysx.utils.file;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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

    private static final String BAST_PATH;

    static {
        try {
            BAST_PATH = Paths.get(Objects.requireNonNull(DecompressUtilsTest.class.getResource("/file/")).toURI()).toFile().getCanonicalPath();
        } catch (IOException e) {
            LOGGER.error("get BASE_PATH IOException", e);
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            LOGGER.error("get BASE_PATH URISyntaxException", e);
            throw new RuntimeException(e);
        }
    }


    @TempDir
    private Path tempDir;


    @Test
    @DisplayName("decompress .tar.gz successfully test")
    public void decompressTarGzSuccessTest() throws FileException {
        String srcFile = BAST_PATH + "/pom.tar.gz";
        String destDir = tempDir.toString();
        LOGGER.info("srcFile: {}, destDir: {}", srcFile, destDir);
        DecompressUtils.decompressTarGz(srcFile, destDir);
        Assertions.assertTrue(Files.exists(Paths.get(destDir + "/pom.xml")));
    }

    @Test
    @DisplayName("checkFilePathEmptyExceptionTest")
    public void checkFilePathEmptyExceptionTest() {
        String srcFile = null;
        String destDir = tempDir.toString();
        FileException fileException = Assertions.assertThrows(FileException.class, () -> DecompressUtils.decompressTarGz(srcFile, destDir));
        Assertions.assertEquals("Path can not be empty!", fileException.getMessage());

    }

    @Test
    @DisplayName("checkFileSourceFileNotExistExceptionTest")
    public void checkFileSourceFileNotExistExceptionTest() {
        String srcFile = BAST_PATH + "/pom.tar.gz1";
        String destDir = tempDir.toString();

        FileException fileException = Assertions.assertThrows(FileException.class, () -> DecompressUtils.decompressTarGz(srcFile, destDir));
        Assertions.assertEquals("Source file does not exist!", fileException.getMessage());
    }

    @Test
    @DisplayName("checkFileDestFileNotExistExceptionTest")
    public void checkFileDestFileNotExistExceptionTest() {
        String srcFile = BAST_PATH + "/pom.tar.gz";
        String destDir = tempDir + "1";

        FileException fileException = Assertions.assertThrows(FileException.class, () -> DecompressUtils.decompressTarGz(srcFile, destDir));
        Assertions.assertEquals("Dest dir not does exist!", fileException.getMessage());
    }

    @Test
    @DisplayName("checkFileDestFileNotDirExceptionTest")
    public void checkFileDestFileNotDirExceptionTest() throws IOException {
        String srcFile = BAST_PATH + "/pom.tar.gz";
        String destDir = tempDir.toString();
        Path file = Files.createFile(Paths.get(destDir + "/hello.txt"));

        FileException fileException = Assertions.assertThrows(FileException.class, () -> DecompressUtils.decompressTarGz(srcFile, file.toString()));
        Assertions.assertEquals("Dest path is not a directory!", fileException.getMessage());
    }

    @Test
    @DisplayName("checkFileDestFileDirNotEmptyExceptionTest")
    public void checkFileDestFileDirNotEmptyExceptionTest() throws IOException {
        String srcFile = BAST_PATH + "/pom.tar.gz";
        String destDir = tempDir.toString();
        Path file = Files.createFile(Paths.get(destDir + "/hello.txt"));

        FileException fileException = Assertions.assertThrows(FileException.class, () -> DecompressUtils.decompressTarGz(srcFile, destDir));
        Assertions.assertEquals("Dest dir is not empty!", fileException.getMessage());
    }
}
