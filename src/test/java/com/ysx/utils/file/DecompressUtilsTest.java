package com.ysx.utils.file;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
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

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void decompressTarGzSuccessTest() throws IOException {
        String srcFile = PATH + "pom.tar.gz";
        String destDir = temporaryFolder.newFolder().getAbsolutePath();
        LOGGER.info("srcFile: {}, destDir: {}", srcFile, destDir);
        try {
            DecompressUtils.decompressTarGz(srcFile, destDir);
            Assert.assertTrue(Files.exists(Paths.get(destDir + "/pom.xml")));
        } catch (FileException e) {
            LOGGER.error("decompressTarGzSuccessTest exception", e);
            Assert.fail();
        }
    }
}
