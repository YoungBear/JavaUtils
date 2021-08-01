package com.ysx.utils.crypto.bc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2021/8/1 7:48
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description
 * List the currently installed providers in the Java Runtime
 */
public class ListProviders {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Provider[] providers = Security.getProviders();
        Arrays.stream(providers).forEach(
                provider -> {
                    System.out.println("Name: " + provider.getName()
                            + ", Version: " + provider.getVersion()
                            + ", info: " + provider.getInfo());
                }
        );

    }
}
