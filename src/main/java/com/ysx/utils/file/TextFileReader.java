package com.ysx.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;

/**
 * 文本文件读取工具类，支持自动识别编码格式
 *
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2026/5/21
 */
public class TextFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 常见编码列表，按优先级排序
     */
    private static final Charset[] COMMON_CHARSETS = {
            StandardCharsets.UTF_8,
            Charset.forName("GBK"),
            Charset.forName("GB2312"),
            Charset.forName("GB18030"),
            StandardCharsets.ISO_8859_1,
            StandardCharsets.US_ASCII,
            Charset.forName("Big5"),
            Charset.forName("Shift_JIS"),
            Charset.forName("EUC-KR")
    };

    /**
     * 读取文本文件，自动识别编码
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws FileException 读取失败时抛出
     */
    public static String readFile(String filePath) throws FileException {
        if (filePath == null) {
            throw new FileException("File path is null");
        }
        return readFile(new File(filePath));
    }

    /**
     * 读取文本文件，自动识别编码
     *
     * @param file 文件对象
     * @return 文件内容
     * @throws FileException 读取失败时抛出
     */
    public static String readFile(File file) throws FileException {
        if (file == null) {
            throw new FileException("File is null");
        }
        if (!file.exists()) {
            throw new FileException("File does not exist: " + file);
        }
        if (!file.isFile()) {
            throw new FileException("Not a file: " + file);
        }

        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] fileBytes = baos.toByteArray();

            Charset charset = detectCharset(fileBytes);
            LOGGER.info("Detected charset for file {}: {}", file.getName(), charset.name());

            // Skip BOM bytes if present
            byte[] contentBytes = skipBOM(fileBytes, charset);

            return new String(contentBytes, charset);

        } catch (IOException e) {
            LOGGER.error("Read file exception: {}", file, e);
            throw new FileException("Read file exception: " + file.getPath(), e);
        }
    }

    /**
     * 检测字节数组的字符编码
     *
     * @param bytes 文件字节数组
     * @return 检测到的字符集
     */
    public static Charset detectCharset(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return StandardCharsets.UTF_8;
        }

        // 1. 首先尝试检测 BOM (Byte Order Mark)
        Charset bomCharset = detectBOM(bytes);
        if (bomCharset != null) {
            return bomCharset;
        }

        // 2. 尝试检测 UTF-16BE/LE (无BOM情况)
        Charset utf16Charset = detectUtf16WithoutBom(bytes);
        if (utf16Charset != null) {
            return utf16Charset;
        }

        // 3. 尝试验证是否为有效的 UTF-8
        if (isValidUtf8(bytes)) {
            return StandardCharsets.UTF_8;
        }

        // 4. 尝试检测常见编码
        for (Charset charset : COMMON_CHARSETS) {
            if (isValidCharset(bytes, charset)) {
                return charset;
            }
        }

        // 5. 默认返回 UTF-8
        return StandardCharsets.UTF_8;
    }

    /**
     * 检测 BOM (Byte Order Mark)
     *
     * @param bytes 文件字节数组
     * @return 检测到的字符集，如果无 BOM 则返回 null
     */
    private static Charset detectBOM(byte[] bytes) {
        if (bytes.length < 2) {
            return null;
        }

        // UTF-8 BOM: EF BB BF
        if (bytes.length >= 3 && (bytes[0] & 0xFF) == 0xEF
                && (bytes[1] & 0xFF) == 0xBB && (bytes[2] & 0xFF) == 0xBF) {
            return StandardCharsets.UTF_8;
        }

        // UTF-16 BE BOM: FE FF
        if ((bytes[0] & 0xFF) == 0xFE && (bytes[1] & 0xFF) == 0xFF) {
            return StandardCharsets.UTF_16;
        }

        // UTF-16 LE BOM: FF FE
        if ((bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xFE) {
            return StandardCharsets.UTF_16;
        }

        // UTF-32 BE BOM: 00 00 FE FF
        if (bytes.length >= 4 && (bytes[0] & 0xFF) == 0x00
                && (bytes[1] & 0xFF) == 0x00 && (bytes[2] & 0xFF) == 0xFE
                && (bytes[3] & 0xFF) == 0xFF) {
            return Charset.forName("UTF-32BE");
        }

        // UTF-32 LE BOM: FF FE 00 00
        if (bytes.length >= 4 && (bytes[0] & 0xFF) == 0xFF && (bytes[1] & 0xFF) == 0xFE
                && (bytes[2] & 0xFF) == 0x00 && (bytes[3] & 0xFF) == 0x00) {
            return Charset.forName("UTF-32LE");
        }

        return null;
    }

    /**
     * 检测无BOM的UTF-16编码
     * 通过检测NULL字节模式来判断是否为UTF-16BE或UTF-16LE编码
     *
     * @param bytes 文件字节数组
     * @return UTF-16BE或UTF-16LE，如果不确定则返回null
     */
    private static Charset detectUtf16WithoutBom(byte[] bytes) {
        if (bytes.length < 4 || bytes.length % 2 != 0) {
            return null;
        }

        int nullCountBe = 0;
        int nullCountLe = 0;
        int evenNullBe = 0;
        int evenNullLe = 0;

        // Check UTF-16BE pattern: even index = 0x00, odd index = ASCII
        for (int i = 0; i < bytes.length; i += 2) {
            if ((bytes[i] & 0xFF) == 0x00) {
                nullCountBe++;
                evenNullBe++;
            }
        }

        // Check UTF-16LE pattern: even index = ASCII, odd index = 0x00
        for (int i = 1; i < bytes.length; i += 2) {
            if ((bytes[i] & 0xFF) == 0x00) {
                nullCountLe++;
                evenNullLe++;
            }
        }

        double nullRatioBe = (double) nullCountBe / (bytes.length / 2);
        double nullRatioLe = (double) nullCountLe / (bytes.length / 2);

        // If more than 30% of even-positioned bytes are NULL, likely UTF-16BE
        if (nullRatioBe > 0.3) {
            return StandardCharsets.UTF_16BE;
        }

        // If more than 30% of odd-positioned bytes are NULL, likely UTF-16LE
        if (nullRatioLe > 0.3) {
            return StandardCharsets.UTF_16LE;
        }

        return null;
    }

    /**
     * 跳过 BOM 字节
     *
     * @param bytes  文件字节数组
     * @param charset 检测到的字符集
     * @return 跳过 BOM 后的字节数组
     */
    private static byte[] skipBOM(byte[] bytes, Charset charset) {
        if (charset == StandardCharsets.UTF_8) {
            // Skip UTF-8 BOM (3 bytes)
            if (bytes.length >= 3 && (bytes[0] & 0xFF) == 0xEF
                    && (bytes[1] & 0xFF) == 0xBB && (bytes[2] & 0xFF) == 0xBF) {
                byte[] result = new byte[bytes.length - 3];
                System.arraycopy(bytes, 3, result, 0, bytes.length - 3);
                return result;
            }
        } else if (charset == StandardCharsets.UTF_16) {
            // Skip UTF-16 BOM (2 bytes)
            if (bytes.length >= 2) {
                int b0 = bytes[0] & 0xFF;
                int b1 = bytes[1] & 0xFF;
                if ((b0 == 0xFE && b1 == 0xFF) || (b0 == 0xFF && b1 == 0xFE)) {
                    byte[] result = new byte[bytes.length - 2];
                    System.arraycopy(bytes, 2, result, 0, bytes.length - 2);
                    return result;
                }
            }
        } else if (charset == StandardCharsets.UTF_16BE || charset == StandardCharsets.UTF_16LE) {
            // UTF-16BE/LE without BOM - no need to skip anything
            return bytes;
        }
        return bytes;
    }

    /**
     * 验证字节数组是否为有效的 UTF-8 编码
     *
     * @param bytes 字节数组
     * @return 是否为有效的 UTF-8
     */
    private static boolean isValidUtf8(byte[] bytes) {
        int i = 0;
        while (i < bytes.length) {
            int b = bytes[i] & 0xFF;

            // 单字节字符 (0xxxxxxx)
            if (b < 0x80) {
                i++;
                continue;
            }

            // 判断字符长度
            int charLength;
            if ((b & 0xE0) == 0xC0) {
                // 2字节字符 (110xxxxx 10xxxxxx)
                charLength = 2;
            } else if ((b & 0xF0) == 0xE0) {
                // 3字节字符 (1110xxxx 10xxxxxx 10xxxxxx)
                charLength = 3;
            } else if ((b & 0xF8) == 0xF0) {
                // 4字节字符 (11110xxx 10xxxxxx 10xxxxxx 10xxxxxx)
                charLength = 4;
            } else {
                // 无效的 UTF-8 首字节
                return false;
            }

            // 检查是否有足够的字节
            if (i + charLength > bytes.length) {
                return false;
            }

            // 验证后续字节是否为 10xxxxxx 格式
            for (int j = 1; j < charLength; j++) {
                int followingByte = bytes[i + j] & 0xFF;
                if ((followingByte & 0xC0) != 0x80) {
                    return false;
                }
            }

            i += charLength;
        }
        return true;
    }

    /**
     * 验证字节数组是否可以用指定字符集有效解码
     *
     * @param bytes  字节数组
     * @param charset 待验证的字符集
     * @return 是否可以有效解码
     */
    private static boolean isValidCharset(byte[] bytes, Charset charset) {
        try {
            CharsetDecoder decoder = charset.newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPORT);
            decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
            decoder.decode(ByteBuffer.wrap(bytes));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }

    /**
     * 获取支持的常见编码列表
     *
     * @return 常见编码名称列表
     */
    public static String[] getSupportedCharsetNames() {
        String[] names = new String[COMMON_CHARSETS.length];
        for (int i = 0; i < COMMON_CHARSETS.length; i++) {
            names[i] = COMMON_CHARSETS[i].name();
        }
        return names;
    }
}
