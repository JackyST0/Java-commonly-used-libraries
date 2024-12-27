# Java IO Stream

## java.io
这个包中包含了所有处理输入、输出需要的类。比如FileInputStream、FileOutputStream、ObjectInputStream、ObjectOutputStream、BufferedReader、BufferedWriter等。
- ### 简单使用
    ```
    import java.io.*;

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
    ```

## java.nio
这是Java新的IO API，它提供了更高效的IO处理。这个包中的Files和Paths类是非常有用的，可以方便地读取、写入和操作文件。例如，使用Files类的readAllLines方法可以一次性读取所有行。
- ### 简单使用
    ```
    import java.nio.file.*;
    import java.io.IOException;

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
    ```

## Apache Commons IO
这是一个开源库，提供了许多有用的IO工具。例如，FileUtils类提供了一些静态的便捷方法，可以方便地读取和写入文件。
- ### 简单使用
    ```
    import org.apache.commons.io.*;

    import java.io.File;
    import java.io.IOException;

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
    ```

## Google Guava
这也是一个开源库，提供了一些IO工具类。例如，Files类提供了一些方法，可以方便地读取和写入文件。
- ### 简单使用
    ```
    import com.google.common.io.*;

    import java.io.*;
    import java.nio.charset.StandardCharsets;

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
    ```

## Hutool
Hutool是一个非常强大的Java工具包，它包含了很多方便的工具类，包括IO操作。Hutool的IO工具类可以简化Java的文件和流的操作。
- ### 简单使用
    ```
    import cn.hutool.core.io.FileUtil;
    import cn.hutool.core.io.IoUtil;

    import java.io.InputStream;
    import java.io.OutputStream;
    import java.nio.charset.StandardCharsets;

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
    ```