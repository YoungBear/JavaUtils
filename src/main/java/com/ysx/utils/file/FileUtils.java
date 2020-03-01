package com.ysx.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2020/3/1 17:51
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static void createDirectories(String path) throws FileException {
        if (null != path) {
            if (!Paths.get(path).toFile().exists()) {
                try {
                    Files.createDirectories(Paths.get(path));
                } catch (IOException e) {
                    LOGGER.error("CreateDirectories exception!", e);
                    throw new FileException("CreateDirectories exception!");
                }
            }
        }
    }

    public static void cleanDirectory(String path) throws FileException{
        try {
            org.apache.commons.io.FileUtils.cleanDirectory(new File(path));
        } catch (IOException e) {
            LOGGER.error("cleanDirectory exception!", e);
            throw new FileException("cleanDirectory exception!");
        }
    }
}
