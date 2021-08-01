# 查看当前环境所有的 Provider 信息

**Java中的Provider**

provider是jdk中密码学算法的具体实现，通常是一组包。

参考：[The `Provider` Class](https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#Provider)



查看当前环境的所有 provider 信息：

```java
public class ListProviders {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListProviders.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Provider[] providers = Security.getProviders();
        Arrays.stream(providers).forEach(
                provider -> {
                    LOGGER.info("Name: {}, Version: {}, info: {}",
                            provider.getName(), provider.getVersion(), provider.getInfo());
                }
        );
    }
}
```

运行结果为：

```
Name: SUN, Version: 1.8
Name: SunRsaSign, Version: 1.8
Name: SunEC, Version: 1.8
Name: SunJSSE, Version: 1.8
Name: SunJCE, Version: 1.8
Name: SunJGSS, Version: 1.8
Name: SunSASL, Version: 1.8
Name: XMLDSig, Version: 1.8
Name: SunPCSC, Version: 1.8
Name: SunMSCAPI, Version: 1.8
Name: BC, Version: 1.69
```

详细信息可以通过查看Info字段获取。