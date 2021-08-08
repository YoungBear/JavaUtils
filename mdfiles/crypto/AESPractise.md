# AES 加解密Java实践

[项目地址](https://github.com/YoungBear/JavaUtils)

AES 实践：

- 封装异常抛出统一异常

- 密文格式一般为：加密算法id+密钥id+iv值+密文数据

- 每次加密随机生成iv值

- 使用算法 "AES/GCM/NoPadding"

  

```java
/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/8/8 18:07
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * AES 实践
 */
public class AESPractise {

    /**
     * 本示例仅使用一种算法，即 "AES/GCM/NoPadding" ，算法ID记为 "1"
     * 实际中可能会考虑多种算法，并需要考虑向前兼容，随时间变化，适用最合适（效率，安全性）的算法
     */
    private static final String ALGORITHM_ID = "1";

    /**
     * IV 数组字节长度 16byte 即 128bit
     */
    private static final int IV_BYTE_LENGTH = 16;

    /**
     * 密文中用到的分隔符
     * 即密文格式为：算法id#密钥id#iv值#密文数据
     * 其中，算法id和密钥id为字符串，iv值和密文数据，为字节数组的十六进制编码
     */
    private static final String SEPARATOR = "#";


    /**
     * 密钥id与密钥值的map，模拟密钥管理系统
     */
    private final static Map<String, String> secretKeyMap = new HashMap<>();

    static {
        // 本示例仅使用2个密钥，实际中随时间变化，会要求产品使用者更新密钥
        // 本示例使用明文存储，实际中需要考虑密文存储，并实现缓存机制
        secretKeyMap.put("001", "7c89f3a887a60e3dba9a3116a3a6da7dbc6f67bc951cd4aebc18df7370a39d5d");
        secretKeyMap.put("002", "a8b2f3690465d450865d20a2e0db6370661e8e4960da92630d4b30c4d4d153a5");
    }

    /**
     * 当前生效的密钥id
     */
    private String currentSecretKeyId;


    /**
     * 构造函数
     * 指定默认算法id和密钥id
     */
    public AESPractise() {
        currentSecretKeyId = "001";
    }

    public static void main(String[] args) {
        random();
    }

    /**
     * 生成随机数
     */
    private static void random() {
        SecureRandom random = new SecureRandom();
        byte[] data = new byte[32];
        random.nextBytes(data);
        System.out.println(Hex.toHexString(data));
    }

    /**
     * 加密
     *
     * @param plainData 明文
     * @return 密文字符串
     * @throws AESException 异常
     */
    public String encrypt(byte[] plainData) throws AESException {
        byte[] secretKey = Hex.decode(secretKeyMap.get(currentSecretKeyId));
        byte[] iv = random(IV_BYTE_LENGTH);
        byte[] cipherData;
        try {
            cipherData = AESUtils.encrypt(secretKey, iv, plainData);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException
                | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new AESException(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ALGORITHM_ID)
                .append(SEPARATOR)
                .append(currentSecretKeyId)
                .append(SEPARATOR)
                .append(Hex.toHexString(iv))
                .append(SEPARATOR)
                .append(Hex.toHexString(cipherData));
        // 编码后，将字节数组置位0
        Arrays.fill(secretKey, (byte) 0);
        Arrays.fill(iv, (byte) 0);
        Arrays.fill(cipherData, (byte) 0);
        return sb.toString();
    }


    /**
     * 解密
     *
     * @param encryptedString 密文字符串
     * @return 明文字节数组
     * @throws AESException 异常
     */
    public byte[] decrypt(String encryptedString) throws AESException {
        String[] split = encryptedString.split(SEPARATOR);
        if (split.length != 4) {
            // 无效的密文格式
            throw new AESException("invalid encrypted String");
        }
        byte[] secretKey = Hex.decode(secretKeyMap.get(split[1]));
        byte[] iv = Hex.decode(split[2]);
        byte[] cipherData = Hex.decode(split[3]);
        byte[] plainData;
        try {
            plainData = AESUtils.decrypt(secretKey, iv, cipherData);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException
                | InvalidAlgorithmParameterException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new AESException(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ALGORITHM_ID)
                .append(currentSecretKeyId)
                .append(Hex.toHexString(iv))
                .append(Hex.toHexString(cipherData));
        // 编码后，将字节数组置位0
        Arrays.fill(secretKey, (byte) 0);
        Arrays.fill(iv, (byte) 0);
        Arrays.fill(cipherData, (byte) 0);
        return plainData;
    }

    /**
     * 随机函数：生成 8*length bit的随机数据
     *
     * @param length 字节数
     * @return 随机数据字节数组
     */
    private byte[] random(int length) {
        SecureRandom random = new SecureRandom();
        byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }

    public String getCurrentSecretKeyId() {
        return currentSecretKeyId;
    }

    public void setCurrentSecretKeyId(String currentSecretKeyId) {
        this.currentSecretKeyId = currentSecretKeyId;
    }
}
```

