package com.example.testspringboot.io;

import com.google.common.io.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/26 14:17
 */
public class GuavaIODemo {

    public static void main(String[] args) {
        File file = new File("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\io\\test.txt");

        // 写入文件
        try {
            Files.write("Hello, world!", file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取文件
        try {
            String content = Files.asCharSource(file, StandardCharsets.UTF_8).read();
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
