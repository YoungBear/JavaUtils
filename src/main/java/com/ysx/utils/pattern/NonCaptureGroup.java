package com.ysx.utils.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2023-02-19 23:52
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description java中的非捕获组 non-capture group
 * https://www.baeldung.com/java-regex-non-capturing-groups
 */
public class NonCaptureGroup {
    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
        test6();


    }

    static void test1() {
        System.out.println("test1 begin *******************");
        Pattern simpleUrlPattern = Pattern.compile("[^:]+://(?:[.a-z]+/?)+");
        Matcher urlMatcher
                = simpleUrlPattern.matcher("http://www.microsoft.com/some/other/url/path");
        System.out.println(urlMatcher.matches());
        System.out.println("test1 end *******************");
    }

    static void test2() {
        // 非匹配组 group(1)会异常
        System.out.println("test2 begin *******************");
        Pattern simpleUrlPattern = Pattern.compile("[^:]+://(?:[.a-z]+/?)+");
        Matcher urlMatcher = simpleUrlPattern.matcher("http://www.microsoft.com/");
        System.out.println(urlMatcher.matches());
        System.out.println(urlMatcher.group(0));
        System.out.println(urlMatcher.group(1));
        System.out.println("test2 end *******************");
    }

    static void test3() {
        // 大小写敏感
        System.out.println("test3 begin *******************");
        Pattern simpleUrlPattern = Pattern.compile("[^:]+://(?:[.a-z]+/?)+");
        Matcher urlMatcher = simpleUrlPattern.matcher("http://www.Microsoft.com/");
        System.out.println(urlMatcher.matches());
        System.out.println("test3 end *******************");
    }

    static void test4() {
        // 大小写敏感，非匹配组可以单独设置flag（如大小写不敏感）
        System.out.println("test4 begin *******************");
        Pattern scopedCaseInsensitiveUrlPattern = Pattern.compile("[^:]+://(?i:[.a-z]+/?)+");
        Matcher urlMatcher = scopedCaseInsensitiveUrlPattern.matcher("http://www.Microsoft.com/");
        System.out.println(urlMatcher.matches());
        System.out.println("test4 end *******************");
    }

    static void test5() {
        // 大小写敏感， -i表示大小写敏感（独立于全局的flag：不敏感）
        System.out.println("test5 begin *******************");
        Pattern scopedCaseSensitiveUrlPattern = Pattern.compile("[^:]+://(?-i:[.a-z]+/?)+/ending-path", Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = scopedCaseSensitiveUrlPattern.matcher("http://www.Microsoft.com/ending-path");
        System.out.println(urlMatcher.matches());
        System.out.println("test5 end *******************");
    }

    static void test6() {
        // 大小写敏感， -i表示大小写敏感（独立于全局的flag：不敏感）
        System.out.println("test6 begin *******************");
        Pattern independentUrlPattern
                = Pattern.compile("[^:]+://(?>[.a-z]+/?)+/ending-path");
        Matcher independentMatcher
                = independentUrlPattern.matcher("http://www.microsoft.com/ending-path");
        System.out.println(independentMatcher.matches());
        System.out.println("test6 end *******************");
    }


}
