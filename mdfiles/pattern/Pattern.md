# Java 正则表达式



## 常用API

java正则表达式主要包含三个类：(在java.util.regex包中)

- Pattern：一个正则表达式的编译表示。需要使用静态方法创建。
- Matcher：对输入字符串进行解释和匹配操作的引擎。需要通过Pattern对象的matcher方法来获得一个Matcher对象。
- PatternSyntaxException：用于表示一个正则表达式模式中的语法错误。

### Matcher的方法

#### 索引方法



- *Index methods* provide useful index values that show precisely where the match was found in the input string:
  - [`public int start()`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#start--): Returns the start index of the previous match.
  - [`public int start(int group)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#start-int-): Returns the start index of the subsequence captured by the given group during the previous match operation.
  - [`public int end()`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#end--): Returns the offset after the last character matched.
  - [`public int end(int group)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#end-int-): Returns the offset after the last character of the subsequence captured by the given group during the previous match operation.

| 方法名                        | 描述                                                   |
| ----------------------------- | ------------------------------------------------------ |
| `public int start()`          | 返回上一次匹配的初始索引                               |
| `public int start(int group)` | 返回上一次匹配的指定匹配组的初始索引                   |
| `public int end()`            | 返回上一次匹配完成后的索引偏移量(上一次匹配最后索引+1) |
| `public int end(int group)`   | 返回上一次匹配的指定匹配组的完成后的偏移量             |



#### 查找方法

*Study methods* review the input string and return a boolean indicating whether or not the pattern is found.

- [`public boolean lookingAt()`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#lookingAt--): Attempts to match the input sequence, starting at the beginning of the region, against the pattern.
- [`public boolean find()`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#find--): Attempts to find the next subsequence of the input sequence that matches the pattern.
- [`public boolean find(int start)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#find-int-): Resets this matcher and then attempts to find the next subsequence of the input sequence that matches the pattern, starting at the specified index.
- [`public boolean matches()`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#matches--): Attempts to match the entire region against the pattern.

| 方法名                           | 描述                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| `public boolean lookingAt()`     | 尝试将从区域开头开始的输入序列与该模式匹配。                 |
| `public boolean find()`          | 尝试查找与该模式匹配的输入序列的下一个子序列。               |
| `public boolean find(int start)` | 重置此匹配器，然后尝试查找匹配该模式、从指定索引开始的输入序列的下一个子序列。 |
| `public boolean matches()`       | 尝试将整个区域与模式匹配。                                   |



#### 替换方法

*Replacement methods* are useful methods for replacing text in an input string.

- [`public Matcher appendReplacement(StringBuffer sb, String replacement)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#appendReplacement-java.lang.StringBuffer-java.lang.String-): Implements a non-terminal append-and-replace step.
- [`public StringBuffer appendTail(StringBuffer sb)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#appendTail-java.lang.StringBuffer-): Implements a terminal append-and-replace step.
- [`public String replaceAll(String replacement)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#replaceAll-java.lang.String-): Replaces every subsequence of the input sequence that matches the pattern with the given replacement string.
- [`public String replaceFirst(String replacement)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#replaceFirst-java.lang.String-): Replaces the first subsequence of the input sequence that matches the pattern with the given replacement string.
- [`public static String quoteReplacement(String s)`](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Matcher.html#quoteReplacement-java.lang.String-): Returns a literal replacement `String` for the specified `String`. This method produces a `String` that will work as a literal replacement `s` in the `appendReplacement` method of the `Matcher` class. The `String` produced will match the sequence of characters in `s` treated as a literal sequence. Slashes (`'\'`) and dollar signs (`'$'`) will be given no special meaning.



参考：https://docs.oracle.com/javase/tutorial/essential/regex/matcher.html









## 元字符

### 定义

*metacharacter* — a character with special meaning interpreted by the matcher.

元字符，一个字符表示特殊的含义。java中的元字符：

```shell
<([{\^-=$!|]})?*+.>
# 换一种方式
<> () [] {} \ ^ - = $ ! | ? * + .
```



### 含义

```shell
<> # 表示命名捕获组
() # 用于捕获组
[] # 用于字符簇
{} # 用于量词
\ # 反斜杠
^ # 1-用于匹配字符串的开头，2-在字符簇第一个位置用于表示取反的含义
- # 在字符簇中表示字符范围
$ # 用于匹配字符簇的结尾
! #
| # 用于表示或的含义
? # 1-量词中表示0次或者1次匹配 2-用于捕获组
* # 量词中表示0次或更多次匹配
+ # 量词中表示0次或1次
. # 表示任意字符
```







eg.

```java
    @Test
    public void metacharacterTest() {
        String regex = "cat.";
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cats").matches());
        assertTrue(pattern.matcher("cata").matches());
    }
```





### 匹配元字符本身

如果需要匹配元字符本身，有以下几种方式：

- 使用转义符
- 使用 \Q 和 \E 包装
- 使用 Pattern的flag：LITERAL。
- 使用 Pattern.quote() 方法修饰正则表达式。（等价于 \Q  和 \E）

eg.

```java
    @Test
    public void escapingMetacharacter_using_backslash_Test() {
        String regex = "cat\\.";
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cata").matches());
    }

    @Test
    public void escapingMetacharacter_using_QE_Test() {
        String regex = "\\Qcat.\\E";
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cat").matches());
    }

    @Test
    public void escapingMetacharacter_using_flag_Test() {
        String regex = "cat.";
        Pattern pattern = Pattern.compile(regex, Pattern.LITERAL);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cata").matches());
    }

    @Test
    public void escapingMetacharacter_using_quote_Test() {
        // \Qcat.\E
        String regex = Pattern.quote("cat.");
        Pattern pattern = Pattern.compile(regex);
        assertTrue(pattern.matcher("cat.").matches());
        assertFalse(pattern.matcher("cata").matches());
    }
```





### 参考：

- [JavaAPI-Pattern](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#Summary%20of%20regular-expression%20constructs)
- [The Java™ Tutorials-String Literals](https://docs.oracle.com/javase/tutorial/essential/regex/literals.html)
- [Baeldung-java-regexp-escape-char](https://www.baeldung.com/java-regexp-escape-char)





## 量词



## 捕获组



## 



## Differences Among Greedy, Reluctant, and Possessive Quantifiers

There are subtle differences among greedy, reluctant, and possessive quantifiers.

Greedy quantifiers are considered "greedy" because they force the matcher to read in, or *eat*, the entire input string prior to attempting the first match. If the first match attempt (the entire input string) fails, the matcher backs off the input string by one character and tries again, repeating the process until a match is found or there are no more characters left to back off from. Depending on the quantifier used in the expression, the last thing it will try matching against is 1 or 0 characters.

The reluctant quantifiers, however, take the opposite approach: They start at the beginning of the input string, then reluctantly eat one character at a time looking for a match. The last thing they try is the entire input string.

Finally, the possessive quantifiers always eat the entire input string, trying once (and only once) for a match. Unlike the greedy quantifiers, possessive quantifiers never back off, even if doing so would allow the overall match to succeed.



## 验证程序

```java
package com.ysx.utils.pattern.javatuturials;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTestHarness {

    /**
     * 正则表达式和input匹配情况
     *
     * @param regex 正则表达式
     * @param input 输入字符串
     */
    public static void runTest(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean found = false;
        while (matcher.find()) {
            String format = String.format("I found the text" + " \"%s\" starting at " + "index %d and ending at index %d.",
                    matcher.group(), matcher.start(), matcher.end());
            System.out.println(format);
            found = true;
        }
        if (!found) {
            System.out.println("No match found.");
        }
    }
}

```





## 参考：

https://www.baeldung.com/regular-expressions-java

https://www.baeldung.com/java-regex-non-capturing-groups

[Java 正则表达式 | 菜鸟教程](https://www.runoob.com/java/java-regular-expressions.html)

[Java 正则表达式的捕获组(capture group) | 菜鸟教程](https://www.runoob.com/w3cnote/java-capture-group.html)

[Lesson: Regular Expressions (The Java™ Tutorials > Essential Java Classes)](https://docs.oracle.com/javase/tutorial/essential/regex/index.html)
