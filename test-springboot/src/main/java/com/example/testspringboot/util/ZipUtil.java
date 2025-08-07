package com.example.testspringboot.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 *
 * @author sun.kai
 * 2016年8月14日
 */
public class ZipUtil {
    private static final int BUFFER = 8192;

    private static File zipFile;

    public static void main(String[] args) throws Exception {
        compress("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\zip\\",
                "简历.zip",
                Arrays.asList("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\waterMark\\图片简历(水印).png",
                        "E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\waterMark\\附件简历(水印).pdf"));

        deCompress("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\zip\\简历.zip",
                "E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\zip\\");
    }

    /**
     * 压缩单个或多文件方法
     *
     * @param zipPath     压缩后的文件夹位置
     * @param FileName    压缩后的文件名称
     * @param srcPathName 要压缩文件的全路径
     */
    public static void compress(String zipPath, String FileName, List<String> srcPathName) {
        File f = new File(zipPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        //压缩后的文件对象
        zipFile = new File(zipPath + FileName);

        try {
            //创建写出流操作
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            for (String srcPath : srcPathName) {
                //创建需要压缩的文件对象
                File file = new File(srcPath);
                if (!file.exists()) {
                    throw new RuntimeException(srcPath + "不存在！");
                }
                /*
                 * (1)如果在zip压缩文件中不需要一级文件目录，定义String basedir = "";
                 * 下面的compress方法中当判断文件file是目录后不需要加上basedir = basedir + file.getName() + File.separator;
                 * (2)如果只是想在压缩后的zip文件里包含一级文件目录，不包含二级以下目录，
                 * 直接在这定义String basedir = file.getName() + File.separator;
                 * 下面的compress方法中当判断文件file是目录后不需要加上basedir = basedir + file.getName() + File.separator;
                 * (3)如果想压缩后的zip文件里包含一级文件目录，也包含二级以下目录，即zip文件里的目录结构和原文件一样
                 * 在此定义String basedir = "";
                 * 下面的compress方法中当判断文件file是目录后需要加上basedir = basedir + file.getName() + File.separator;
                 */
                //String basedir = file.getName() + File.separator;
                String basedir = "";
                compress(file, out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        /*
         * 判断是目录还是文件
         */
        if (file.isDirectory()) {
            basedir += file.getName() + File.separator;
            compressDirectory(file, out, basedir);
            System.out.println("压缩目录：" + basedir + file.getName());
        } else {
            System.out.println("压缩文件：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            /* 递归 */
            compress(file, out, basedir);
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    Files.newInputStream(file.toPath()));
            //创建Zip实体，并添加进压缩包
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            //读取待压缩的文件并写进压缩包里
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解压缩
     *
     * @param sourceFile 要解压缩的文件的路径
     * @param destDir    解压缩后的目录路径
     */
    public static void deCompress(String sourceFile, String destDir) {
        //创建需要解压缩的文件对象
        File file = new File(sourceFile);
        if (!file.exists()) {
            throw new RuntimeException(sourceFile + "不存在！");
        }
        //创建解压缩的文件目录对象
        File destDirectory = new File(destDir);
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }
        /*
         * 保证文件夹路径最后是"/"或者"\"
         * charAt()返回指定索引位置的char值
         */
        char lastChar = destDir.charAt(destDir.length() - 1);
        if (lastChar != '/' && lastChar != '\\') {
            //在最后加上分隔符
            destDir += File.separator;
        }
        unzip(sourceFile, destDir);
    }

    /**
     * 解压方法
     * 需要ant.jar
     */
    private static void unzip(String sourceZip, String destDir) {
        Project p = new Project();
        Expand e = new Expand();
        e.setProject(p);
        e.setSrc(new File(sourceZip));
        e.setOverwrite(false);
        e.setDest(new File(destDir));
        e.execute();
    }
}
