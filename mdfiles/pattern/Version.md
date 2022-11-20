# 版本号

## 1. 定义

版本号定义可以参考[语义化版本 2.0.0](https://semver.org/lang/zh-CN/),本文只考虑三位版本号，不考虑先行版本号和版本编译信息。具体如下：

版本格式：**主版本号.次版本号.修订号**，版本号递增规则如下：

1. 主版本号：当你做了不兼容的 API 修改，
2. 次版本号：当你做了向下兼容的功能性新增，
3. 修订号：当你做了向下兼容的问题修正。

## 2. 格式

采用 **主版本号.次版本号.修订号**的格式，且只能使用数字，不能以0开头。

正则表达式为：

```shell
^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)$
```

对应java程序及单元测试：

```java
    private static final String PATTERN_STR = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)$";

    private static final Pattern VERSION_PATTERN = Pattern.compile(PATTERN_STR);

    /**
     * 是否是有效的版本号
     *
     * @param input 字符串
     * @return 是否是有效的版本号
     */
    public static boolean isValidVersion(String input) {
        if (input == null) {
            return false;
        }
        return VERSION_PATTERN.matcher(input).matches();
    }
```

对应单元测试代码：

```java
    private static Stream<String> validVersionProvider() {
        return Stream.of(
                "0.0.4",
                "1.2.3",
                "10.20.30",
                "1.0.0",
                "2.0.0",
                "1.1.7",
                "99999999999999999999999.999999999999999999.99999999999999999"
        );
    }

    private static Stream<String> invalidVersionProvider() {
        return Stream.of(
                "1",
                "1.2",
                "1.2.3-0123",
                "+invalid",
                "invalid",
                "alpha",
                "01.1.1",
                "1.01.1",
                "1.1.01",
                "1.2-SNAPSHOT",
                "1.2-RC-SNAPSHOT"
        );
    }

    @ParameterizedTest(name = "#{index} - Run test with Version = {0}")
    @MethodSource("validVersionProvider")
    void test_version_regex_valid(String inputVersion) {
        assertTrue(VersionUtils.isValidVersion(inputVersion));
    }

    @ParameterizedTest(name = "#{index} - Run test with Version = {0}")
    @MethodSource("invalidVersionProvider")
    void test_version_regex_invalid(String inputVersion) {
        assertFalse(VersionUtils.isValidVersion(inputVersion));
    }
```







## 3. 优先级比较

参考：https://semver.org/lang/zh-CN/#spec-item-11

版本的优先层级指的是不同版本在排序时如何比较。

1. 判断优先层级时，必须（MUST）把版本依序拆分为主版本号、次版本号、修订号后进行比较。

2. 由左到右依序比较每个标识符，第一个差异值用来决定优先层级：主版本号、次版本号及修订号以**数值**比较。

   例如：1.0.0 < 2.0.0 < 2.1.0 < 2.1.1。

对应java代码及单元测试代码：

```java
    /**
     * 比较两个版本号大小
     * version1比version2大返回1
     * version1比version2小返回-1
     * version1与version2相同返回0
     *
     * @param version1 version1
     * @param version2 version2
     * @return 比较结果
     */
    public static int compare(String version1, String version2) {
        if (!isValidVersion(version1) || !isValidVersion(version2)) {
            throw new RuntimeException("invalid version.");
        }
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Codes = version1.split("\\.");
        String[] version2Codes = version2.split("\\.");
        for (int i = 0; i < version1Codes.length; i++) {
            int code1 = Integer.parseInt(version1Codes[i]);
            int code2 = Integer.parseInt(version2Codes[i]);
            if (code1 > code2) {
                return 1;
            } else if (code1 < code2) {
                return -1;
            }
        }
        return 0;
    }
```



对应单元测试代码：

```java
    @Test
    @DisplayName("version compare return 1")
    public void test_version_compare_return_1() {
        String version1 = "1.1.1";
        String version2 = "1.1.0";
        Assertions.assertEquals(1, VersionUtils.compare(version1, version2));
        version1 = "2.1.1";
        version2 = "1.2.0";
        Assertions.assertEquals(1, VersionUtils.compare(version1, version2));
        version1 = "10.1.1";
        version2 = "2.2.0";
        Assertions.assertEquals(1, VersionUtils.compare(version1, version2));
    }

    @Test
    @DisplayName("version compare return -1")
    public void test_version_compare_return__1() {
        String version1 = "1.1.1";
        String version2 = "1.1.2";
        Assertions.assertEquals(-1, VersionUtils.compare(version1, version2));
        version1 = "2.1.1";
        version2 = "2.2.0";
        Assertions.assertEquals(-1, VersionUtils.compare(version1, version2));
        version1 = "10.1.1";
        version2 = "11.2.0";
        Assertions.assertEquals(-1, VersionUtils.compare(version1, version2));
    }

    @Test
    @DisplayName("version compare return 0")
    public void test_version_compare_return_0() {
        String version1 = "1.1.1";
        String version2 = "1.1.1";
        Assertions.assertEquals(0, VersionUtils.compare(version1, version2));
    }
```







# 参考

[1. 语义化版本 2.0.0](https://semver.org/lang/zh-CN/),
