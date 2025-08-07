package com.example.testspringboot.excel.poiExcel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/23 17:19
 */
public class Tests {

    private static final String path = "E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\excel\\easyExcel.xlsx";

    @Test
    public void writeExcel() throws Exception {
        Workbook workbook = new XSSFWorkbook(); // 创建工作簿
        Sheet sheet = workbook.createSheet("Sheet1"); // 创建工作表

        CellStyle style = workbook.createCellStyle(); // 创建单元格样式
        Font font = workbook.createFont(); // 创建字体
        font.setColor(IndexedColors.RED.getIndex()); // 设置字体颜色为红色
        style.setFont(font); // 将字体应用到样式

        Row row = sheet.createRow(0); // 创建行
        Cell cell = row.createCell(0); // 创建单元格
        cell.setCellValue("Hello, World!"); // 设置单元格值
        cell.setCellStyle(style); // 将样式应用到单元格

        FileOutputStream fileOut = new FileOutputStream(path); // 创建文件输出流
        workbook.write(fileOut); // 写入文件
        fileOut.close(); // 关闭文件输出流
    }

    @Test
    public void readExcel() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(path);

        // 创建Workbook
        Workbook workbook = new XSSFWorkbook(fileInputStream);

        // 获取第一个Sheet
        Sheet sheet = workbook.getSheetAt(0);

        // 遍历每一行数据
        int rowIndex = 0;
        for (Row row : sheet) {
            // 如果是第一行，跳过
            if (rowIndex++ == 0) {
                continue;
            }

            // 遍历每一列数据
            for (Cell cell : row) {
                String cellValue = null;
                // 这里只处理了字符串和数字两种数据类型，实际使用时需要根据需要处理更多类型
                if (cell.getCellType() == CellType.STRING) {
                    cellValue = cell.getStringCellValue();
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }

        workbook.close();
        fileInputStream.close();
    }
}
