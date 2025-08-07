package com.example.testspringboot.io;

import java.nio.file.*;
import java.io.IOException;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/26 14:14
 */
public class JavaNIODemo {

    public static void main(String[] args) {
        Path path = Paths.get("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\io\\test.txt");

        // 写入文件
        try {
            Files.write(path, "Hello, world!".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取文件
        try {
            byte[] bytes = Files.readAllBytes(path);
            System.out.println(new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
