package com.example.testspringboot.util;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/9 17:02
 */
@Slf4j
@Configuration
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static String filePath;

    @Value("${custom.uploadFilePath:}")
    private String tempFilePath;

//    @PostConstruct
    public void init() {
        filePath = tempFilePath;
    }

    public static void main(String[] args) throws Exception {
        writeCsv(new File("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\excel\\test.csv").getAbsolutePath(), Arrays.asList(
                Arrays.asList("name", "sex"), Arrays.asList("name1", "sex1"),
                Arrays.asList("name2", "sex2"), Arrays.asList("name5", "sex5"),
                Arrays.asList("name3", "sex3"), Arrays.asList("name6", "sex6"),
                Arrays.asList("name4", "sex4"), Arrays.asList("name7", "sex7")));
    }

    /**
     * 递归删除文件或文件夹及其子文件或子文件夹
     */
    public static boolean deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deleteRecursively(child);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }

    /**
     * 删除指定文件名的文件
     */
    public static void deleteFile(String fileName) {
        // file
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 在指定文件名的文件末尾追加一行内容
     */
    public static void appendFileLine(String fileName, String content) {
        // file
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return;
            }
        }

        // content
        if (content == null) {
            content = "";
        }
        content += "\r\n";

        // append file content
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 读取CSV文件，返回一个二维列表，每一行是一个列表。
     */
    public static List<List<String>> readCsv(String fileName) throws Exception {
        return readCsv(fileName, ',', "UTF-8");
    }
    public static List<List<String>> readCsv(String fileName, char c, String code) throws Exception {
        List<List<String>> result = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(fileName);
        CsvReader csvReader = new CsvReader(inputStream, c, Charset.forName(code));
        while (csvReader.readRecord()) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < csvReader.getColumnCount(); i++) {
                list.add(csvReader.get(i));
            }
            result.add(list);
        }
        csvReader.close();
        inputStream.close();
        return result;
    }

    /**
     * 将二维列表的内容写入CSV文件。
     */
    public static void writeCsv(String fileName, List<List<String>> list) throws Exception {
        writeCsv(fileName, ',', "UTF-8", list);
    }
    public static void writeCsv(String fileName, char c, String code, List<List<String>> list) throws Exception {
        CsvWriter wr = new CsvWriter(fileName, c, Charset.forName(code));
        for (List<String> data : list) {
            wr.writeRecord(data.toArray(new String[0]));
        }
        wr.close();
    }

    /**
     * 读取指定文件的内容，返回一个字符串。
     */
    public static String loadFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        List<String> list = loadFileLines(fileName);
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 读取指定文件的内容，返回一个列表，每一行是一个元素。
     */
    public static List<String> loadFileLines(String fileName) {
        return loadFileLines(fileName, 0);
    }
    public static List<String> loadFileLines(String fileName, int toLineNum) {
        List<String> result = new ArrayList<>();

        // valid log file
        File file = new File(fileName);
        if (!file.exists()) {
            return result;
        }

        // read file
        LineNumberReader reader = null;
        try {
            //reader = new LineNumberReader(new FileReader(logFile));
            reader = new LineNumberReader(new InputStreamReader(Files.newInputStream(file.toPath()), "GBK"));
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    if (index >= toLineNum) {
                        result.add(line);
                    }
                    index++;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    /**
     * 创建指定路径的父文件夹。
     */
    public static void mkdirParentFolder(String path) {
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) parentFile.mkdirs();
    }

    /**
     * 文件复制
     */
    public static void copyFile(String srcPath, String targetPath) throws Exception {
        File srcFile = new File(srcPath);
        File target = new File(targetPath);
        if (!srcFile.exists()) {
            throw new Exception("文件不存在！");
        }
        if (!srcFile.isFile()) {
            throw new Exception("不是文件！");
        }
        // 判断目标路径是否是目录
        if (target.isFile()) {
            throw new Exception("文件路径不存在！");
        }

        // 获取源文件的文件名
        StringBuilder fileName = new StringBuilder(srcPath.substring(srcPath.lastIndexOf("\\") + 1));
        // 判断是否存在相同的文件名的文件
        File[] listFiles = target.listFiles();
        for (File file : listFiles) {
            if (fileName.toString().equals(file.getName())) {
                fileName.append("_1");
            }
        }
        String newFileName = targetPath + File.separator + fileName;
        File targetFile = new File(newFileName);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(targetFile);
            // 从in中批量读取字节，放入到buf这个字节数组中，
            // 从第0个位置开始放，最多放buf.length个 返回的是读到的字节的个数
            byte[] buf = new byte[8 * 1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                System.out.println("关闭输入流错误！");
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                System.out.println("关闭输出流错误！");
            }
        }
    }

    /**
     * 将内容写入指定路径的文件。
     */
    public static boolean writeFile(String path, String content) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取文本文件的内容，返回一个字符串。
     */
    public static String readTxt(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));       // 构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {       // 使用readLine方法，一次读一行
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    /**
     * 第二种读取方式，避免乱码
     */
    public static String readTxt(File file, Integer type) {
        StringBuilder content = new StringBuilder();
        try {
            String code = resolveCode(file.getAbsolutePath());
            InputStream is = Files.newInputStream(file.toPath());
            InputStreamReader isr = new InputStreamReader(is, code);
            BufferedReader br = new BufferedReader(isr);
            String str;
            while (null != (str = br.readLine())) {
                content.append(str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("读取文件:" + file.getAbsolutePath() + "失败!");
        }
        return content.toString();

    }

    /**
     * 解析文件的编码格式。
     */
    public static String resolveCode(String path) throws Exception {
        InputStream inputStream = Files.newInputStream(Paths.get(path));
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2)
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1)
            code = "Unicode";
        else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
            code = "UTF-8";
        inputStream.close();
        logger.info(code);
        return code;
    }

    /**
     * 保存上传的文件。
     */
    public static String saveFile(MultipartFile file) {
        File saveFile = new File(filePath);
        if (!saveFile.exists()) saveFile.mkdirs();
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String resultName = filePath + DateUtil.format(new Date(), "yyyy-MM-dd HH-mm-ss") + suffixName;
        try {
            file.transferTo(new File(resultName));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return resultName;
    }

    /**
     * 将文件转换成Base64编码的字符串
     */
    public static String fileToBase64(File tempFile) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(tempFile);
            bis = new BufferedInputStream(fis);
            bos = new ByteArrayOutputStream(fis.available());
            byte[] bytes = new byte[1024];
            int temp;
            while ((temp = bis.read(bytes, 0, bytes.length)) != -1) {
                bos.write(bytes, 0,temp);
            }
            byte[] tempFileBytes = bos.toByteArray();
            return Base64Utils.encodeToString(tempFileBytes);
        } catch (Exception e) {
            log.error("文件转换base64错误，错误信息：", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("关闭bos流错误，错误信息：", e);
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("关闭bis流错误，错误信息：", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("关闭fis流错误，错误信息：", e);
                }
            }
        }
        return "";
    }

    /**
     * 创建文件。
     */
    public static File createFile(String path) throws Exception{
        File file = new File(path);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        return file;
    }

    /**
     * 复制文件夹。
     */
    public static void copyDirectory(File sourceDir, File destinationDir) throws IOException {
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }

        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File destinationFile = new File(destinationDir, file.getName());
                if (file.isDirectory()) {
                    copyDirectory(file, destinationFile);
                } else {
                    copyFile(file, destinationFile);
                }
            }
        }
    }

    /**
     * 复制文件。
     */
    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
