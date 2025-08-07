package com.example.testspringboot.optional;

import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 16:40
 */
public class Tests {

    @Test
    public void test1() {
        Optional<String> empty = Optional.empty();
        System.out.println(empty.isPresent());  // false
        System.out.println(empty.orElse("hello"));  // hello
        System.out.println(empty);
    }

    @Test
    public void test2() {
        Optional<String> opt = Optional.of("hello world");
        System.out.println(opt); // 输出：Optional[hello world]
    }

    @Test
    public void test3() {
        Optional<String> optOrNull = Optional.ofNullable(null);
        System.out.println(optOrNull); // 输出：Optional.empty
    }

    @Test
    public void test4() {
        Optional<String> opt = Optional.of("hello world");
        System.out.println(opt.isPresent()); // 输出：true

        Optional<String> optOrNull = Optional.ofNullable(null);
        System.out.println(optOrNull.isPresent()); // 输出：false
    }

    @Test
    public void test5() {
        Optional<String> optOrNull = Optional.ofNullable(null);
        if (optOrNull.isPresent()) {
            System.out.println(optOrNull.get().length());
        }

        Optional<String> opt = Optional.of("hello world");
        opt.ifPresent(str -> System.out.println(str.length())); // 如果是null这里就不会执行了
    }

    @Test
    public void test6() {
        String name = "hello";
        System.out.println("orElse");
        String name2 = Optional.ofNullable(name).orElse(getDefaultValue());

        System.out.println("orElseGet");
        String name3 = Optional.ofNullable(name).orElseGet(Tests::getDefaultValue);
    }

    private static String getDefaultValue() {
        System.out.println("getDefaultValue");
        return "getDefaultValue";
    }

    @Test
    public void test7() {
        String name = null;
        Optional<String> optOrNull = Optional.ofNullable(name);
        System.out.println(optOrNull.get());
    }

    @Test
    public void test8() {
        String name = "hello";
        name = Optional.ofNullable(name)
                .filter(name::startsWith)
                .orElseThrow(()->new IllegalArgumentException("Invalid username."));
        System.out.println(name);
    }
}
