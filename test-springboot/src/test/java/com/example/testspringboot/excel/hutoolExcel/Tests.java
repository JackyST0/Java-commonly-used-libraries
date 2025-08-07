package com.example.testspringboot.excel.hutoolExcel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.testspringboot.entity.bo.ExcelData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/23 13:49
 */
public class Tests {

    private static final String path = "E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\excel\\hutoolExcel.xlsx";

    @Test
    public void excelWriterList() {
        List<String> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
        List<String> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
        List<String> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
        List<String> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");
        List<String> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");

        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

        // 通过工具类创建writer
        ExcelWriter writer = createWriter(path);
        //通过构造方法创建writer
        //ExcelWriter writer = new ExcelWriter("E:\local-projects\test-springboot\src\main\resources\file\excel\hutoolExcel.xlsx");

        //跳过当前行，既第一行，非必须，在此演示用
        writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        writer.merge(row1.size() - 1, "测试标题");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();
    }

    @Test
    public void excelWriterMap() {
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("是否合格", true);
        row1.put("考试日期", DateUtil.date());

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33);
        row2.put("成绩", 59.50);
        row2.put("是否合格", false);
        row2.put("考试日期", DateUtil.date());

        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);

        // 通过工具类创建writer
        ExcelWriter writer = createWriter(path);
        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(row1.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

    @Test
    public void excelWriterBean() {
        ExcelData excelData1 = new ExcelData();
        excelData1.setName("张三");
        excelData1.setAge(22);
        excelData1.setOccupation("Engineer");

        ExcelData excelData2 = new ExcelData();
        excelData2.setName("李四");
        excelData2.setAge(28);
        excelData2.setOccupation("Manager");

        List<ExcelData> rows = CollUtil.newArrayList(excelData1, excelData2);

        // 通过工具类创建writer
        ExcelWriter writer = createWriter(path);
        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(ExcelData.class.getDeclaredFields().length - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

    @Test
    public void excelReaderBean() {
        //通过路径直接获取
        ExcelReader reader = ExcelUtil.getReader(path);
        //通过sheet编号获取
        reader = ExcelUtil.getReader(FileUtil.file(path), 0);
        //通过sheet名获取
        reader = ExcelUtil.getReader(FileUtil.file(path), "sheet1");
        //读取数据默认从第一行读起
        List<List<Object>> readList = reader.read();
        List<Map<String, Object>> readMap = reader.readAll();
        List<ExcelData> readBean = reader.readAll(ExcelData.class);
        System.out.println(readList);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(readMap);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(readBean);
    }

    private ExcelWriter createWriter(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }
        return ExcelUtil.getWriter(path);
    }
}
