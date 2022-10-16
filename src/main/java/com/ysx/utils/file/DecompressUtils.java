package com.ysx.utils.file;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2020/3/1 17:37
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 解压缩用具类
 */
public class DecompressUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecompressUtils.class);

    // 解压缩时的缓存为 1M
    private static final int DECOMPRESS_BUFFER_SIZE = 1024 * 1024;

    public static void decompressTarGz(String srcFilePath, String destDirPath) throws FileException {
        checkFile(srcFilePath, destDirPath);
        try {
            doDecompressTarGz(srcFilePath, destDirPath);
        } catch (FileException e) {
            LOGGER.error("FileException exception!", e);
            FileUtils.cleanDirectory(destDirPath);
            throw new FileException("FileException exception!", e);
        }
    }

    /**
     * 解压.tar.gz或者.tgz文件
     *
     * @param srcFilePath 源文件路径
     * @param destDirPath 目标路径
     * @throws FileException 文件操作异常
     */
    private static void doDecompressTarGz(String srcFilePath, String destDirPath) throws FileException {
        try (TarArchiveInputStream ais = new TarArchiveInputStream(new GzipCompressorInputStream(
                new BufferedInputStream(new FileInputStream(srcFilePath))))) {
            TarArchiveEntry archiveEntry;
            while (null != (archiveEntry = ais.getNextTarEntry())) {
                String entryName = archiveEntry.getName();
                if (archiveEntry.isDirectory()) {
                    FileUtils.createDirectories(destDirPath + "/" + entryName);
                } else {
                    // 如果父目录不存在，则需要创建
                    FileUtils.createDirectories(new File(destDirPath + "/" + entryName).getParent());
                    int count;
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destDirPath + "/" + entryName))) {
                        byte[] buffer = new byte[DECOMPRESS_BUFFER_SIZE];
                        while (-1 != (count = ais.read(buffer, 0, DECOMPRESS_BUFFER_SIZE))) {
                            bos.write(buffer, 0, count);
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("doDecompressTarGz exception!", e);
            throw new FileException("doDecompressTarGz exception!", e);
        }
    }

    /**
     * 输入参数校验
     *
     * @param srcFilePath 源文件路径
     * @param destDirPath 解压目标文件路径
     * @throws FileException
     */
    private static void checkFile(String srcFilePath, String destDirPath) throws FileException {
        if (null == srcFilePath || srcFilePath.isEmpty() || null == destDirPath || destDirPath.isEmpty()) {
            throw new FileException("Path can not be empty!");
        }
        if (!new File(srcFilePath).exists()) {
            throw new FileException("Source file does not exist!");
        }
        if (!new File(destDirPath).exists()) {
            throw new FileException("Dest dir not does exist!");
        }
        if (!new File(destDirPath).isDirectory()) {
            throw new FileException("Dest path is not a directory!");
        }
        if (Objects.requireNonNull(new File(destDirPath).list()).length > 0) {
            throw new FileException("Dest dir is not empty!");
        }
    }
}
