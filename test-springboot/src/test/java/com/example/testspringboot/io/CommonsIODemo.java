package com.example.testspringboot.io;

import org.apache.commons.io.*;

import java.io.File;
import java.io.IOException;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/26 14:16
 */
public class CommonsIODemo {

    public static void main(String[] args) {
        File file = new File("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\io\\test.txt");

        // 写入文件
        try {
            FileUtils.writeStringToFile(file, "Hello, world!", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取文件
        try {
            String content = FileUtils.readFileToString(file, "UTF-8");
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
