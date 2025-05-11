# java正则表达式中的断言

Java正则表达式中的断言属于一种零宽度断言（Zero-width Assertion），用于检查某个位置前后是否满足特定条件，但**不会消耗字符**。



## 分类



| 类型                         | 语法    | 描述                           |
| ---------------------------- | ------- | ------------------------------ |
| 正向先行 Positive Lookahead  | X(?=Y)  | 匹配X的位置，要求右侧只能匹配Y |
| 负向先行 Negative Lookahead  | X(?!Y)  | 匹配X的位置，要求右侧不能匹配Y |
| 正向后行 Positive Lookbehind | (?<=Y)X | 匹配X的位置，要求左侧只能匹配Y |
| 负向后行 Negative Lookbehind | (?<!Y)X | 匹配X的位置，要求左侧不能匹配Y |



## 语法示例代码



```java
    @Test
    @DisplayName("正向先行：foo(?=bar) 匹配foo其右边要匹配bar")
    public void positiveLookaheadTest1() {
        Pattern pattern = Pattern.compile("foo(?=bar)");
        Matcher matcher1 = pattern.matcher("foobar");
        assertTrue(matcher1.find());
        assertEquals("foo", matcher1.group());
        Matcher matcher2 = pattern.matcher("foocar");
        assertFalse(matcher2.find());
        Matcher matcher3 = pattern.matcher("goobar");
        assertFalse(matcher3.find());
    }

    @Test
    @DisplayName("负向先行：foo(?!bar) 匹配foo其右边不能匹配bar")
    public void negativeLookaheadTest1() {
        Pattern pattern = Pattern.compile("foo(?!bar)");
        Matcher matcher1 = pattern.matcher("foobar");
        assertFalse(matcher1.find());
        Matcher matcher2 = pattern.matcher("foocar");
        assertTrue(matcher2.find());
        assertEquals("foo", matcher2.group());
        Matcher matcher3 = pattern.matcher("goobar");
        assertFalse(matcher3.find());
    }

    @Test
    @DisplayName("正向后行：(?<=bar)foo 匹配foo其左边要匹配bar")
    public void positiveLookbehindTest1() {
        Pattern pattern = Pattern.compile("(?<=bar)foo");
        Matcher matcher1 = pattern.matcher("barfoo");
        assertTrue(matcher1.find());
        assertEquals("foo", matcher1.group());
        Matcher matcher2 = pattern.matcher("carfoo");
        assertFalse(matcher2.find());
        Matcher matcher3 = pattern.matcher("cargoo");
        assertFalse(matcher3.find());
    }

    @Test
    @DisplayName("负向后行：(?<!bar)foo 匹配foo其左边不能匹配bar")
    public void negativeLookbehindTest1() {
        Pattern pattern = Pattern.compile("(?<!bar)foo");
        Matcher matcher1 = pattern.matcher("barfoo");
        assertFalse(matcher1.find());
        Matcher matcher2 = pattern.matcher("carfoo");
        assertTrue(matcher2.find());
        assertEquals("foo", matcher2.group());
        Matcher matcher3 = pattern.matcher("cargoo");
        assertFalse(matcher3.find());
    }
```



## 应用示例代码

```java
    @Test
    @DisplayName("应用1 密码强度校验")
    public void assertionPractise1() {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\Q`~!@#$%^&*()-_=+[]{}\\|;:'\",.<>/? \\E]).{8,16}$");
        // 满足
        assertTrue(pattern.matcher("Abc@2025").matches());
        assertTrue(pattern.matcher("Abcd@2025!123").matches());
        // 长度不满足8-16位
        assertFalse(pattern.matcher("Ab@2025").matches());
        assertFalse(pattern.matcher("Abc@2025Xbc@2025!").matches());
        // 不包含大写字母
        assertFalse(pattern.matcher("abc@2025").matches());
        // 不包含小写字母
        assertFalse(pattern.matcher("ABC@2025").matches());
        // 不包含特殊字符
        assertFalse(pattern.matcher("Abcd2025").matches());
        // 不包含数字
        assertFalse(pattern.matcher("Abc@xyzu").matches());
    }

    @Test
    @DisplayName("应用2 不能包含连续三个数字")
    public void assertionPractise2() {
        String regex = "^(?!.*\\d{3}).*$";
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("a12b").matches());
        assertFalse(pattern.matcher("a123b").matches());
    }

    @Test
    @DisplayName("应用3 提取两标签之间的内容")
    public void assertionPractise3() {
        String html = "<div>Hello</div>";
        Pattern pattern = Pattern.compile("(?<=<div>).*?(?=</div>)");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            System.out.println(matcher.group()); // 输出 "Hello"
        }
    }

    @Test
    @DisplayName("应用4 获取特定符号后的值")
    public void assertionPractise4() {
        String text = "Price: $200, Tax: $30";
        Pattern pattern = Pattern.compile("(?<=\\$)\\d+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group()); // 输出 "200" 和 "30"
        }
    }

    @Test
    @DisplayName("应用5 替换或插入内容（保留上下文）: 在特定位置插入字符")
    // 场景：在每三个数字后添加逗号（如货币格式化）
    // 方案：用正向先行断言匹配每三个数字的位置
    public void assertionPractise5() {
        String regex = "(?<=\\d)(?=(\\d{3})+$)";
        Pattern pattern = Pattern.compile(regex);
        String number = "123456789";
        String formatted = pattern.matcher(number).replaceAll(",");
        System.out.println(formatted); // 输出 "123,456,789"
    }

    @Test
    @DisplayName("应用6 替换或插入内容（保留上下文）: 替换但不破坏原有结构")
    // 场景：将句子中的Python替换为Java，但仅当后面是代码时
    // 方案：用正向先行断言限制替换条件
    public void assertionPractise6() {
        String regex = "Python(?=代码)";
        Pattern pattern = Pattern.compile(regex);
        String text = "Python代码 Python教程";
        String replaced = pattern.matcher(text).replaceAll(",");
        System.out.println(replaced); // 输出 "Java代码 Python教程"
    }

    @Test
    @DisplayName("应用7 避免匹配重叠内容: 排除注释中的内容")
    // 场景：匹配代码中的TODO，但排除注释中的//TODO。
    //方案：用负向后行断言排除前面有//的情况。
    public void assertionPractise7() {
        Pattern pattern = Pattern.compile("(?<!//)TODO");
        Matcher matcher1 = pattern.matcher("int x; //TODO: 修复");
        assertFalse(matcher1.find());
        Matcher matcher2 = pattern.matcher("int x; TODO: 修复");
        assertTrue(matcher2.find());
    }

    @Test
    @DisplayName("应用8 避免匹配重叠内容: 匹配独立单词")
    // 场景：匹配独立的cat（前后不是字母）。
    // 方案：用负向断言确保单词边界。
    public void assertionPractise8() {
        String text = "cat category cat5";
        Pattern pattern = Pattern.compile("(?<!\\w)cat(?!\\w)");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group()); // 仅匹配独立的 "cat"
        }
    }
```





## 源代码地址

- [github](https://github.com/YoungBear/JavaUtils)
- [gitee](https://gitee.com/YoungBear2023/JavaUtils)

