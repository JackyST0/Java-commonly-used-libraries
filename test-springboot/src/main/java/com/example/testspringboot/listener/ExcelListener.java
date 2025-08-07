package com.example.testspringboot.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.testspringboot.entity.bo.ExcelData;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/23 14:04
 */
public class ExcelListener extends AnalysisEventListener<ExcelData> {
    @Override
    public void invoke(ExcelData excelData, AnalysisContext analysisContext) {
        // 数据处理逻辑，可以将数据存储到数据库或进行其他操作
        System.out.println("Read data: " + excelData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 所有数据解析完成后的操作
    }
}
