package com.ysx.utils.crypto.bc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/8/1 7:48
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description List the currently installed providers in the Java Runtime
 * 查看当前环境所有的 Provider 信息
 * 参考：https://www.bouncycastle.org/documentation.html
 */
public class ListProviders {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListProviders.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Provider[] providers = Security.getProviders();
        Arrays.stream(providers).forEach(
                provider -> {
                    LOGGER.info("Name: {}, Version: {}",
                            provider.getName(), provider.getVersion());
                }
        );
    }
}
