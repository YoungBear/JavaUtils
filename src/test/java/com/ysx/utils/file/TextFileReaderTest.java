package com.ysx.utils.file;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TextFileReader 单元测试
 */
public class TextFileReaderTest {

    private static final String TEST_DIR = "src/test/resources/file/";

    @Test
    void testReadUtf8File() throws IOException, FileException {
        String content = "Hello, 你好, 世界！UTF-8 文件测试";
        File testFile = createTempFile("utf8_test.txt", content, StandardCharsets.UTF_8);

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals(content, result);
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testReadGbkFile() throws IOException, FileException {
        String content = "GBK编码测试，中文内容";
        File testFile = createTempFile("gbk_test.txt", content, Charset.forName("GBK"));

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals(content, result);
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testReadUtf8WithBom() throws IOException, FileException {
        String content = "UTF-8 with BOM test";
        File testFile = createTempFileWithBom("utf8_bom_test.txt", content, StandardCharsets.UTF_8);

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals(content, result);
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testReadUtf16BeFile() throws IOException, FileException {
        String content = "UTF-16 BE encoding test";
        // Use UTF_16 with BE byte order (no BOM) - Java's UTF_16BE writes big-endian without BOM
        File testFile = createTempFileUtf16("utf16be_test.txt", content, StandardCharsets.UTF_16BE);

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals(content, result);
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testReadUtf16LeFile() throws IOException, FileException {
        String content = "UTF-16 LE encoding test";
        // Use UTF_16 with LE byte order (no BOM)
        File testFile = createTempFileUtf16("utf16le_test.txt", content, StandardCharsets.UTF_16LE);

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals(content, result);
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testReadIso8859File() throws IOException, FileException {
        String content = "ISO-8859-1 test: abc123";
        File testFile = createTempFile("iso8859_test.txt", content, StandardCharsets.ISO_8859_1);

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals(content, result);
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testReadFileWithNullPath() {
        assertThrows(FileException.class, () -> TextFileReader.readFile((String) null));
    }

    @Test
    void testReadFileWithNonExistentPath() {
        assertThrows(FileException.class, () -> TextFileReader.readFile("non_existent_file.txt"));
    }

    @Test
    void testGetSupportedCharsetNames() {
        String[] names = TextFileReader.getSupportedCharsetNames();
        assertNotNull(names);
        assertTrue(names.length > 0);
        assertEquals("UTF-8", names[0]);
    }

    @Test
    void testDetectCharset() {
        // Test UTF-8
        byte[] utf8Bytes = "Hello".getBytes(StandardCharsets.UTF_8);
        assertEquals(StandardCharsets.UTF_8, TextFileReader.detectCharset(utf8Bytes));

        // Test GBK
        byte[] gbkBytes = "你好".getBytes(java.nio.charset.Charset.forName("GBK"));
        assertEquals(java.nio.charset.Charset.forName("GBK"), TextFileReader.detectCharset(gbkBytes));
    }

    @Test
    void testDetectCharsetWithBom() {
        // UTF-8 BOM
        byte[] utf8BomBytes = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'H', 'e', 'l', 'l', 'o'};
        assertEquals(StandardCharsets.UTF_8, TextFileReader.detectCharset(utf8BomBytes));

        // UTF-16 BE BOM - returns UTF_16 since it handles BOM automatically
        byte[] utf16BeBomBytes = new byte[]{(byte) 0xFE, (byte) 0xFF, 'H', 'e', 'l', 'l', 'o'};
        assertEquals(StandardCharsets.UTF_16, TextFileReader.detectCharset(utf16BeBomBytes));

        // UTF-16 LE BOM - returns UTF_16 since it handles BOM automatically
        byte[] utf16LeBomBytes = new byte[]{(byte) 0xFF, (byte) 0xFE, 'H', 'e', 'l', 'l', 'o'};
        assertEquals(StandardCharsets.UTF_16, TextFileReader.detectCharset(utf16LeBomBytes));
    }

    @Test
    void testReadEmptyFile() throws IOException, FileException {
        File testFile = createTempFile("empty_test.txt", "", StandardCharsets.UTF_8);

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals("", result);
        } finally {
            testFile.delete();
        }
    }

    @Test
    void testReadAsciiFile() throws IOException, FileException {
        String content = "Simple ASCII content 12345";
        File testFile = createTempFile("ascii_test.txt", content, StandardCharsets.US_ASCII);

        try {
            String result = TextFileReader.readFile(testFile);
            assertEquals(content, result);
        } finally {
            testFile.delete();
        }
    }

    private File createTempFile(String fileName, String content, Charset charset) throws IOException {
        File dir = new File(TEST_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(charset));
        }
        return file;
    }

    private File createTempFileWithBom(String fileName, String content, Charset charset) throws IOException {
        File dir = new File(TEST_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Write UTF-8 BOM
            fos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            fos.write(content.getBytes(charset));
        }
        return file;
    }

    private File createTempFileUtf16(String fileName, String content, Charset charset) throws IOException {
        File dir = new File(TEST_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(charset));
        }
        return file;
    }
}
