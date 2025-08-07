package com.example.testspringboot.io;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/27 10:59
 */
public class HutoolIODemo {

    public static void main(String[] args) {
        // 写入文件
        OutputStream out = FileUtil.getOutputStream("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\io\\test.txt");
        IoUtil.write(out, StandardCharsets.UTF_8, false, "Hello, world!");

        // 读取文件
        InputStream in = FileUtil.getInputStream("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\io\\test.txt");
        String content = IoUtil.read(in, StandardCharsets.UTF_8);
        System.out.println(content);
    }
}
