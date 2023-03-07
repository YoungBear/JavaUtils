# 正则表达式-捕获组

主要概念：

- 捕获组 capturing group
- 命名捕获组 named-capturing group
- 非捕获组 non-capturing group
- 反向引用



## 捕获组 capturing group

**官方解释：**

*Capturing groups* are a way to treat multiple characters as a single unit. They are created by placing the characters to be grouped inside a set of parentheses. For example, the regular expression `(dog)` creates a single group containing the letters `"d" "o"` and `"g"`. The portion of the input string that matches the capturing group will be saved in memory for later recall via backreferences.

**简单翻译下：**

捕获组是将多个字符视为一个单元的一种方法。它们是通过将要分组的字符放置在一组括号中来创建的。例如，正则表达式（dog）创建了一个包含字母“d”“o”和“g”的单一组。与捕获组匹配的输入字符串的部分将保存在内存中，以便以后通过反向引用召回。

格式： `(X)`

即每遇到一个左括号，就认为是一个捕获组。可以通过 `matcher.group(int index)`  来获取当前捕获组匹配的字符串。

序号从1开始，0表示整个字符串。

eg.

```java
    @Test
    public void capturing_group_test() {
        Pattern pattern = Pattern.compile("((A)(B(C)))");
        String input = "ABC";
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Assertions.assertEquals("ABC", matcher.group(0));
            Assertions.assertEquals("ABC", matcher.group(1));
            Assertions.assertEquals("A", matcher.group(2));
            Assertions.assertEquals("BC", matcher.group(3));
            Assertions.assertEquals("C", matcher.group(4));
        }
    }
```

## 命名捕获组 named-capturing group

类似于使用索引来获取捕获组，也可以通过自定义名称来获取。

格式： `(?<name>X)` 。

使用 `matcher.group(String name)` 来获取指定的捕获组的内容。

eg.

```java
    @Test
    public void named_capturing_group_test() {
        Pattern pattern = Pattern.compile("(?<name1>(<?name2>A)(<?name3>B(<?name4>C)))");
        String input = "ABC";
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Assertions.assertEquals("ABC", matcher.group("name1"));
            Assertions.assertEquals("A", matcher.group("name2"));
            Assertions.assertEquals("BC", matcher.group("name3"));
            Assertions.assertEquals("C", matcher.group("name4"));
        }
    }
```



## 非捕获组 non-capturing group

非捕获组的格式： `(?:X)` 。其特点为：

- 匹配组的数量不会计入。即该捕获组不会记录到总的捕获组数量中。
- 可以针对该group修改flag。如 `?i:` 表示大小写不敏感。`?-i:` 表示大小写敏感。

eg.

```java
    @Test
    public void non_capturing_group_test() {
        Pattern pattern = Pattern.compile("(ABC)+");
        String input = "ABCABC";
        Matcher matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());
        Assertions.assertEquals(1, matcher.groupCount());

        pattern = Pattern.compile("(?:ABC)+");
        matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());
        Assertions.assertEquals(0, matcher.groupCount());
    }

    @Test
    public void non_capturing_group_test_flags() {
        Pattern pattern = Pattern.compile("(abc)(ABC)+");
        String input = "abcabcabc";
        Matcher matcher = pattern.matcher(input);
        Assertions.assertFalse(matcher.matches());

        pattern = Pattern.compile("(abc)(?i:ABC)+");
        matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());
    }

    @Test
    public void non_capturing_group_test_flags_negation() {
        Pattern pattern = Pattern.compile("(abc)(ABC)+", Pattern.CASE_INSENSITIVE);
        String input = "ABCabcabc";
        Matcher matcher = pattern.matcher(input);
        Assertions.assertTrue(matcher.matches());

        pattern = Pattern.compile("(abc)(?-i:ABC)+", Pattern.CASE_INSENSITIVE);
        Assertions.assertFalse(pattern.matcher("ABCabcabc").matches());
        Assertions.assertTrue(pattern.matcher("ABCABCABC").matches());
    }
```



## 反向引用 backreferences

