package com.example.testspringboot.excel.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.example.testspringboot.entity.bo.ExcelData;
import com.example.testspringboot.listener.ExcelListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/23 13:49
 */
public class Tests {

    private static final String path = "E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\excel\\easyExcel.xlsx";

    @Test
    public void writeExcel() {
        // 准备要写入的数据
        List<ExcelData> dataList = initData();
//      // 写入 Excel 文件
//		ExcelWriter excelWriter = EasyExcel.write(fileName, ExcelData.class).build();
//		// 创建写入的 sheet
//		WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();
//		// 写入数据
//		excelWriter.write(dataList, writeSheet);

        //与上面同理
        EasyExcel.write(path, ExcelData.class).sheet("模板").doWrite(dataList);

        System.out.println("Excel 写入完成！");
    }

    @Test
    public void readExcel() {
        // 使用 EasyExcel 读取 Excel 文件
        EasyExcel.read(path, ExcelData.class, new ExcelListener()).sheet().doRead();
    }

    // 初始化要写入的数据
    private static List<ExcelData> initData() {
        List<ExcelData> dataList = new ArrayList<>();
        dataList.add(new ExcelData("John", 25, "Engineer"));
        dataList.add(new ExcelData("Alice", 30, "Manager"));
        dataList.add(new ExcelData("Bob", 28, "Developer"));

        return dataList;
    }
}
