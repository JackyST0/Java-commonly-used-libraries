package com.example.testspringboot.entity.bo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.example.testspringboot.util.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/12/23 13:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// 设置Excel列的宽度
@ColumnWidth(value = 18)
// 设置Excel表头行的高度
@HeadRowHeight(20)
// 设置Excel表头的字体样式
@HeadFontStyle(fontHeightInPoints = 10)
// 设置Excel表头的样式
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
// 设置Excel内容的样式
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class ExcelData {

    @ExcelProperty(value = "姓名")
    @ExcelUtil.ExcelField(value = "姓名")
    private String name;

    @ExcelProperty(value = "年龄")
    @ExcelUtil.ExcelField(value = "年龄")
    private Integer age;

    @ExcelProperty(value = "职业")
    @ExcelUtil.ExcelField(value = "职业")
    private String occupation;
}
