package com.example.testspringboot.io;

import java.io.*;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/26 14:08
 */
public class JavaIODemo {

    public static void main(String[] args) {
        // 写入文件
        try (PrintWriter writer = new PrintWriter("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\io\\test.txt")) {
            writer.write("Hello, world!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 读取文件
        try (BufferedReader reader = new BufferedReader(new FileReader("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\spark\\test.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
