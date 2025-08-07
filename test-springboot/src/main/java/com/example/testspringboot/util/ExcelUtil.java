package com.example.testspringboot.util;

import com.example.testspringboot.entity.bo.ExcelData;
import com.google.common.collect.Maps;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/9 17:00
 */
public class ExcelUtil {
    public static final String OFFICE_EXCEL_XLS = "xls";
    public static final String OFFICE_EXCEL_XLSX = "xlsx";

    public static void main(String[] args) {
        String file1Path = "/Users/jxtan/Document/work/财务/jumia/Export 10 13 May 2025 - 16 Jun 2025.csv";
        String file2Path = "/Users/jxtan/Document/work/财务/jumia/202505/chromeDownload/10/Export 10 13 May 2025 - 16 Jun 2025.csv";
        String mergedFilePath = "/Users/jxtan/Document/work/财务/jumia/202505/Export 10 13 May 2025 - 16 Jun 2025.csv";

        try {
            mergeCsvFiles(file1Path, file2Path, mergedFilePath);
            System.out.println("文件合并成功！输出文件位于: " + mergedFilePath);
        } catch (IOException e) {
            System.err.println("合并文件时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从Excel文件的第一个工作表中读取数据，并将数据转化为指定类型的实体列表
     */
    public static <T> List<T> readExcelToEntity(String filepath, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        try {
            List<List<String>> data = readExcel(filepath);
            List<Map<String, String>> mapList = excelDataToMap(data);
            Field[] fields = clazz.getDeclaredFields();
            for (Map<String, String> map : mapList) {
                T t = clazz.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = null;
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    if (excelField != null) {
                        if (StringUtil.isNotBlank(excelField.value())) {
                            value = map.get(excelField.value());
                        }
                    } else {
                        value = map.get(field.getName());
                    }
                    if (value != null) {
                        try {
                            value = covertValue(value, field);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (value != null) {
                        field.set(t, value);
                    }
                }
                result.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从Excel文件的指定工作表中读取数据，并将数据转化为指定类型的实体列表
     */
    public static <T> List<T> readExcelToEntity(String filepath, Class<T> clazz, Integer sheetNo) {
        List<T> result = new ArrayList<>();
        try {
            List<List<String>> data = readExcel(filepath, sheetNo);
            List<Map<String, String>> mapList = excelDataToMap(data);
            Field[] fields = clazz.getDeclaredFields();
            for (Map<String, String> map : mapList) {
                T t = clazz.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = null;
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    if (excelField != null) {
                        if (StringUtil.isNotBlank(excelField.value())) {
                            value = map.get(excelField.value());
                        }
                    } else {
                        value = map.get(field.getName());
                    }
                    if (value != null) {
                        try {
                            value = covertValue(value, field);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (value != null) {
                        field.set(t, value);
                    }
                }
                result.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将一个值转化为指定字段的类型
     */
    private static Object covertValue(Object value, Field field) {
        Object result = null;
        if (value != null) {
            String str = String.valueOf(value);
            Class<?> type = field.getType();
            if (type.equals(Double.class)) {
                result = Double.valueOf(str);
            } else if (type.equals(BigDecimal.class)) {
                result = BigDecimal.valueOf(Double.parseDouble(str));
            } else if (type.equals(Integer.class)) {
                result = (int) Double.parseDouble(str);
            } else if (type.equals(String.class)) {
                result = str.matches("\\d+\\.0") ? str.substring(0, str.length() - 2) : str;
            }
        }
        return result;
    }

    /**
     * 根据给定的文件路径读取Excel文件的第一个工作表的内容
     */
    public static List<List<String>> readExcel(String filepath) throws EncryptedDocumentException, IOException {
        return readExcel(filepath, 0);
    }
    /**
     * 根据给定的文件路径和工作表编号读取Excel文件的指定工作表的内容
     */
    public static List<List<String>> readExcel(String filepath, Integer sheetNo) throws EncryptedDocumentException, IOException {
        return readExcel(filepath, sheetNo, 0);
    }
    /**
     * 根据给定的文件路径、工作表编号和行开始编号读取Excel文件的指定工作表的内容(sheetNo设置为null则读取全文)
     */
    public static List<List<String>> readExcel(String filepath, Integer sheetNo, Integer rowStart) throws EncryptedDocumentException, IOException {
        List<List<String>> resultList = new ArrayList<>();
        Workbook workbook = getBaseWorkbook(filepath);
        if (workbook != null) {
            if (sheetNo == null) {
                int numberOfSheets = workbook.getNumberOfSheets();
                for (int i = 0; i < numberOfSheets; i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    if (sheet == null) {
                        continue;
                    }
                    resultList = readExcelSheet(sheet, rowStart);
                }
            } else {
                Sheet sheet = workbook.getSheetAt(sheetNo);
                if (sheet != null) {
                    resultList = readExcelSheet(sheet, rowStart);
                }
            }
        }
        return resultList;
    }

    /**
     * 将从Excel文件读取的数据转换为一个包含多个映射的列表，每个映射代表一行数据，其中键是列标题，值是对应的数据
     */
    public static List<Map<String, String>> excelDataToMap(List<List<String>> dataList) {
        List<Map<String, String>> result = new ArrayList<>();
        List<String> titleList = dataList.get(0);
        for (int i = 1; i < dataList.size(); i++) {
            Map<String, String> map = new HashMap<>();
            List<String> list = dataList.get(i);
            for (int j = 0; j < titleList.size(); j++) {
                map.put(titleList.get(j), j >= list.size() ? null : list.get(j));
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 根据文件路径获取Workbook对象
     */
    public static Workbook getBaseWorkbook(String filepath) throws EncryptedDocumentException, IOException {
        InputStream is = null;
        Workbook wb = null;
        if (StringUtil.isBlank(filepath)) {
            throw new IllegalArgumentException("文件路径不能为空");
        } else {
            String suffix = getSuffix(filepath);
            if (StringUtil.isBlank(suffix)) {
                throw new IllegalArgumentException("文件后缀不能为空");
            }
            if (OFFICE_EXCEL_XLS.equals(suffix) || OFFICE_EXCEL_XLSX.equals(suffix)) {
                try {
                    is = Files.newInputStream(Paths.get(filepath));
                    wb = WorkbookFactory.create(is);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (wb != null) {
                        wb.close();
                    }
                }
            } else {
                throw new IllegalArgumentException("该文件非Excel文件");
            }
        }
        return wb;
    }

    /**
     * 获取文件的后缀名
     */
    public static String getSuffix(String filepath) {
        if (StringUtil.isBlank(filepath)) {
            return "";
        }
        int index = filepath.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return filepath.substring(index + 1);
    }

    /**
     * 解析Excel单元格中的数据
     */
    public static String parseExcel(Cell cell) {
        String result;
        switch (cell.getCellType()) {
            case NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case BLANK:
                result = "";
            default:
                result = "";
                break;
        }
        return result;
    }

    /**
     * 解析Excel单元格中的数据
     */
    private static String getByCell(Cell cell) {
        if (cell == null) return "";
        String value;
        short format = cell.getCellStyle().getDataFormat();
        if (cell.getCellType() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                String formatStr;
                if (format == 14 || format == 31 || format == 57 || format == 58) {
                    //日期
                    formatStr = "yyyy/M/d";
                } else if (format == 20 || format == 32 || format == 21) {
                    //时间
                    formatStr = "H:mm:ss";
                } else {
                    formatStr = "yyyy/M/d H:mm:ss";
                }
                Date date = cell.getDateCellValue();
                if (date == null) return "";
                value = DateUtil.format(date, formatStr);
            } else {
                double dValue = cell.getNumericCellValue();
//                DecimalFormat df = new DecimalFormat("0");
                value = String.valueOf(dValue);
            }
        } else {
            DataFormatter formatter = new DataFormatter();
            value = formatter.formatCellValue(cell);
        }
        return value;
    }

    /**
     * 读取指定Sheet页的表头
     */
    public static Row readTitle(String filepath, int sheetNo) throws IOException, EncryptedDocumentException {
        Row returnRow = null;
        Workbook workbook = getBaseWorkbook(filepath);
        if (workbook != null) {
            Sheet sheet = workbook.getSheetAt(sheetNo);
            returnRow = readTitle(sheet);
        }
        return returnRow;
    }

    /**
     * 读取指定Sheet页的表头
     */
    public static Row readTitle(Sheet sheet) {
        Row returnRow = null;
        int totalRow = sheet.getLastRowNum();// 得到excel的总记录条数
        for (int i = 0; i < totalRow; i++) {// 遍历行
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            returnRow = sheet.getRow(0);
            break;
        }
        return returnRow;
    }

    /**
     * 创建一个新的Excel文件，并向其中写入数据
     */
    public static boolean writeExcel(String filepath, String sheetName, List<String> titles,
                                     List<Map<String, Object>> values) throws IOException {
        boolean success;
        OutputStream outputStream = null;
        if (StringUtil.isBlank(filepath)) {
            throw new IllegalArgumentException("文件路径不能为空");
        } else {
            String suffix = getSuffix(filepath);
            if (StringUtil.isBlank(suffix)) {
                throw new IllegalArgumentException("文件后缀不能为空");
            }
            Workbook workbook;
            if (OFFICE_EXCEL_XLS.equalsIgnoreCase(suffix)) {
                workbook = new HSSFWorkbook();
            } else {
                workbook = new XSSFWorkbook();
            }
            // 生成一个表格
            Sheet sheet;
            if (StringUtil.isBlank(sheetName)) {
                // name 为空则使用默认值
                sheet = workbook.createSheet();
            } else {
                sheet = workbook.createSheet(sheetName);
            }
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 15);
            // 生成样式
            Map<String, CellStyle> styles = createStyles(workbook);
            // 创建标题行
            Row row = sheet.createRow(0);
            // 存储标题在Excel文件中的序号
            Map<String, Integer> titleOrder = Maps.newHashMap();
            for (int i = 0; i < titles.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(styles.get("header"));
                String title = titles.get(i);
                cell.setCellValue(title);
                titleOrder.put(title, i);
            }
            // 写入正文
            Iterator<Map<String, Object>> iterator = values.iterator();
            // 行号
            int index = 1;
            while (iterator.hasNext()) {
                row = sheet.createRow(index);
                Map<String, Object> value = iterator.next();
                for (Map.Entry<String, Object> map : value.entrySet()) {
                    // 获取列名
                    String title = map.getKey();
                    // 根据列名获取序号
                    int i = titleOrder.get(title);
                    // 在指定序号处创建cell
                    Cell cell = row.createCell(i);
                    // 设置cell的样式
                    if (index % 2 == 1) {
                        cell.setCellStyle(styles.get("cellA"));
                    } else {
                        cell.setCellStyle(styles.get("cellB"));
                    }
                    // 获取列的值
                    Object object = map.getValue();
                    // 判断object的类型
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (object instanceof Double) {
                        cell.setCellValue((Double) object);
                    } else if (object instanceof Date) {
                        String time = simpleDateFormat.format((Date) object);
                        cell.setCellValue(time);
                    } else if (object instanceof Calendar) {
                        Calendar calendar = (Calendar) object;
                        String time = simpleDateFormat.format(calendar.getTime());
                        cell.setCellValue(time);
                    } else if (object instanceof Boolean) {
                        cell.setCellValue((Boolean) object);
                    } else {
                        if (object != null) {
                            cell.setCellValue(object.toString());
                        }
                    }
                }
                index++;
            }

            try {
                outputStream = Files.newOutputStream(Paths.get(filepath));
                workbook.write(outputStream);
                success = true;
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
                workbook.close();
            }
            return success;
        }
    }

    /**
     * 创建Excel单元格的样式
     */
    public static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = Maps.newHashMap();

        // 标题样式
        XSSFCellStyle titleStyle = (XSSFCellStyle) wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER); // 水平对齐
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直对齐
        titleStyle.setLocked(true); // 样式锁定
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleFont.setFontName("微软雅黑");
        titleStyle.setFont(titleFont);
        styles.put("title", titleStyle);

        // 文件头样式
        XSSFCellStyle headerStyle = (XSSFCellStyle) wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // 前景色
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 颜色填充方式
        headerStyle.setWrapText(true);
        headerStyle.setBorderRight(BorderStyle.THIN); // 设置边界
        headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        Font headerFont = wb.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        titleFont.setFontName("微软雅黑");
        headerStyle.setFont(headerFont);
        styles.put("header", headerStyle);

        Font cellStyleFont = wb.createFont();
        cellStyleFont.setFontHeightInPoints((short) 12);
        cellStyleFont.setColor(IndexedColors.BLUE_GREY.getIndex());
        cellStyleFont.setFontName("微软雅黑");

        // 正文样式A
        XSSFCellStyle cellStyleA = (XSSFCellStyle) wb.createCellStyle();
        cellStyleA.setAlignment(HorizontalAlignment.CENTER); // 居中设置
        cellStyleA.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleA.setWrapText(true);
        cellStyleA.setBorderRight(BorderStyle.THIN);
        cellStyleA.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleA.setBorderLeft(BorderStyle.THIN);
        cellStyleA.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleA.setBorderTop(BorderStyle.THIN);
        cellStyleA.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleA.setBorderBottom(BorderStyle.THIN);
        cellStyleA.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleA.setFont(cellStyleFont);
        styles.put("cellA", cellStyleA);

        // 正文样式B:添加前景色为浅黄色
        XSSFCellStyle cellStyleB = (XSSFCellStyle) wb.createCellStyle();
        cellStyleB.setAlignment(HorizontalAlignment.CENTER);
        cellStyleB.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleB.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        cellStyleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleB.setWrapText(true);
        cellStyleB.setBorderRight(BorderStyle.THIN);
        cellStyleB.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleB.setBorderLeft(BorderStyle.THIN);
        cellStyleB.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleB.setBorderTop(BorderStyle.THIN);
        cellStyleB.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleB.setBorderBottom(BorderStyle.THIN);
        cellStyleB.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyleB.setFont(cellStyleFont);
        styles.put("cellB", cellStyleB);

        return styles;
    }

    /**
     * 复制一个已存在的Excel文件的内容到一个新的Excel文件
     */
    public static void writeExcel(String srcFilepath, String desFilepath)
            throws Exception {
        FileOutputStream outputStream = null;
        String suffix = getSuffix(desFilepath);
        if (StringUtil.isBlank(suffix)) {
            throw new IllegalArgumentException("文件后缀不能为空");
        }
        Workbook workbookDes;
        if (OFFICE_EXCEL_XLS.equalsIgnoreCase(suffix)) {
            workbookDes = new HSSFWorkbook();
        } else {
            workbookDes = new XSSFWorkbook();
        }

        Workbook workbook = getBaseWorkbook(srcFilepath);
        if (workbook == null) {
            throw new Exception("文件为空");
        }
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int k = 0; k < numberOfSheets; k++) {
            Sheet sheet = workbook.getSheetAt(k);
            Sheet sheetDes = workbookDes.createSheet(sheet.getSheetName());
            int rowNos = sheet.getLastRowNum();
            for (int i = 0; i <= rowNos; i++) {
                Row row = sheet.getRow(i);
                Row rowDes = sheetDes.createRow(i);
                if (row != null) continue;
                int columNos = row.getLastCellNum();
                for (int j = 0; j < columNos; j++) {
                    Cell cell = row.getCell(j);
                    Cell cellDes = rowDes.createCell(j);
                    if (cell == null) continue;
                    cell.setCellType(CellType.STRING);
                    cellDes.setCellType(CellType.STRING);
                    cellDes.setCellValue(cell.getStringCellValue());
                }
            }
        }
        try {
            outputStream = new FileOutputStream(desFilepath);
            workbookDes.write(outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            workbookDes.close();
        }
    }

    /**
     * 根据文件路径创建一个新的工作簿
     */
    public static Workbook getWorkbook(String filepath) {
        Workbook workbook;
        if (StringUtil.isBlank(filepath)) {
            throw new IllegalArgumentException("文件路径不能为空");
        } else {
            String suffix = ExcelUtil.getSuffix(filepath);
            if (StringUtil.isBlank(suffix)) {
                throw new IllegalArgumentException("文件后缀不能为空");
            }
            if (OFFICE_EXCEL_XLS.equalsIgnoreCase(suffix)) {
                workbook = new HSSFWorkbook();
            } else {
                workbook = new XSSFWorkbook();
            }
        }
        return workbook;
    }

    /**
     * 将数据导出为Excel文件
     */
    public static <T> void exportExcel(String outFile, List<T> result) {
        HttpServletResponse response = ContextUtil.getResponse();
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(outFile.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.setContentType("application/vnd.ms-excel");
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            Workbook workbook = ExcelUtil.getWorkbook(outFile);
            List<List<String>> data = new ArrayList<>();
            if (!result.isEmpty()) {
                T t1 = result.get(0);

                Class<?> clazz = t1.getClass();
                Field[] fields = clazz.getDeclaredFields();
                boolean first = true;
                for (T t : result) {
                    if (first) {
                        List<String> list = new ArrayList<>();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            ExcelField excelField = field.getAnnotation(ExcelField.class);
                            String title;
                            if (excelField != null) {
                                if (!excelField.export()) continue;
                                title = excelField.value();
                            } else {
                                title = field.getName();
                            }
                            list.add(title);
                        }
                        data.add(list);
                        first = false;
                    }
                    List<String> list2 = new ArrayList<>();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        ExcelField excelField = field.getAnnotation(ExcelField.class);
                        if (excelField != null && !excelField.export()) continue;
                        Object o = field.get(t);
                        String value = o == null ? "" : String.valueOf(o);
                        list2.add(value);
                    }
                    data.add(list2);
                }
            }
            ExcelUtil.writeExcel(workbook, 0, "Sheet1", data);
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将数据导出为Excel文件,但是它还接受一个Class类型的参数，该参数用于在没有数据的情况下创建标题行
     */
    public static <T> void exportExcelAddClass(String outFile, List<T> result, Class bean) {
        HttpServletResponse response = ContextUtil.getResponse();
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(outFile.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.setContentType("application/vnd.ms-excel");
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            Workbook workbook = ExcelUtil.getWorkbook(outFile);
            List<List<String>> data = new ArrayList<>();
            if (!result.isEmpty()) {
                T t1 = result.get(0);

                Class<?> clazz = t1.getClass();
                Field[] fields = clazz.getDeclaredFields();
                boolean first = true;
                for (T t : result) {
                    if (first) {
                        List<String> list = new ArrayList<>();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            ExcelField excelField = field.getAnnotation(ExcelField.class);
                            String title;
                            if (excelField != null) {
                                if (!excelField.export()) continue;
                                title = excelField.value();
                            } else {
                                title = field.getName();
                            }
                            list.add(title);
                        }
                        data.add(list);
                        first = false;
                    }
                    List<String> list2 = new ArrayList<>();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        ExcelField excelField = field.getAnnotation(ExcelField.class);
                        if (excelField != null && !excelField.export()) continue;
                        Object o = field.get(t);
                        String value = o == null ? "" : String.valueOf(o);
                        list2.add(value);
                    }
                    data.add(list2);
                }
            } else {
                Field[] fields = bean.getDeclaredFields();
                List<String> list = new ArrayList<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    String title;
                    if (excelField != null) {
                        if (!excelField.export()) continue;
                        title = excelField.value();
                    } else {
                        title = field.getName();
                    }
                    list.add(title);
                }
                data.add(list);
            }
            ExcelUtil.writeExcel(workbook, 0, "Sheet1", data);
            workbook.write(out);
            out.flush();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将数据写入到指定路径的Excel文件中
     */
    public static void writeExcel (String outPath, List<List<String>> result) throws Exception {
        writeExcel(outPath, Collections.singletonList("summary"), Collections.singletonList(result));
    }

    /**
     * 在指定路径的Excel文件中创建多个工作表，并将数据写入到这些工作表中
     */
    public static void writeExcel (String outPath, List<String> sheetList, List<List<List<String>>> result) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(outPath);
        FileUtil.mkdirParentFolder(outPath);
        for (int i = 0; i < sheetList.size(); i++) {
            ExcelUtil.writeExcel(workbook, i, sheetList.get(i), result.get(i));
        }
        OutputStream out = Files.newOutputStream(Paths.get(outPath));
        workbook.write(out);
        out.close();
        workbook.close();
    }
    /**
     * 在指定的工作簿中创建一个新的工作表，然后在工作表中写入数据
     */
    public static void writeExcel(Workbook workbook, int sheetNum, String sheetName, List<List<String>> list) {
        writeExcel(workbook, sheetNum, sheetName, list, false);
    }
    public static void writeExcel (Workbook workbook, int sheetNum, String sheetName, List<List<String>> list, boolean mergeFirst) {
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetName);
        writeExcel(workbook, sheet, list, mergeFirst);
    }
    /**
     * 在指定的工作簿和工作表中写入数据。如果mergeFirst参数为true，它还会合并第一列的单元格
     */
    public static void writeExcel (Workbook workbook, Sheet sheet, List<List<String>> list, boolean mergeFirst) {
        // 生成一个表格
//        Sheet sheet = workbook.createSheet();
//        workbook.setSheetName(sheetNum, sheetName);
//        Sheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成样式
        Map<String, CellStyle> styles = ExcelUtil.createStyles(workbook);

//        List<CellRangeAddress> mergeList = getMerge(list);
//        CellRangeAddress region = new CellRangeAddress(1, 2, 0, 0);
//        sheet.addMergedRegion(region);
        // 行号
        for (int index = 0; index < list.size(); index++) {
            // 创建标题行
            Row row = sheet.createRow(index);
            List<String> list1 = list.get(index);
            for (int i = 0; i < list1.size(); i++) {
                // 根据列名获取序号
                // 在指定序号处创建cell
                Cell cell = row.createCell(i);
                // 设置cell的样式
//                if (index % 2 == 1) {
//                    cell.setCellStyle(styles.get("cellA"));
//                } else {
//                    cell.setCellStyle(styles.get("cellB"));
//                }
                // 获取列的值
                cell.setCellValue(list1.get(i));
            }

        }
        if (mergeFirst) {
            List<CellRangeAddress> mergeList = getMerge(list);
            mergeList.forEach(sheet::addMergedRegion);
        }
    }

    /**
     * 在给定的工作表中写入数据，并根据提供的合并区域列表合并单元格
     */
    public static void writeExcelMerge (Workbook workbook, Sheet sheet, List<List<String>> list, List<CellRangeAddress> mergeList) {
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 合并单元格
        mergeList.forEach(sheet::addMergedRegion);
        // 生成样式
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中

        // 行号
        for (int index = 0; index < list.size(); index++) {
            // 创建标题行
            Row row = sheet.createRow(index);
            List<String> list1 = list.get(index);
            for (int i = 0; i < list1.size(); i++) {
                // 根据列名获取序号
                // 在指定序号处创建cell
                Cell cell = row.createCell(i);
                cell.setCellStyle(style);
                // 设置cell的样式
//                if (index % 2 == 1) {
//                    cell.setCellStyle(styles.get("cellA"));
//                } else {
//                    cell.setCellStyle(styles.get("cellB"));
//                }
                // 获取列的值
                cell.setCellValue(list1.get(i));
            }
        }
    }

    /**
     * 获取需要合并的单元格区域
     */
    private static List<CellRangeAddress> getMerge (List<List<String>> list) {
        List<CellRangeAddress> result = new ArrayList<>();
        String name = "";
        int start = 0;
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).get(0);
            if (!str.equals(name)) {
                if (i - start > 1) {
                    result.add(new CellRangeAddress(start, i - 1, 0, 0));
                }
                name = str;
                start = i;
            } else if (list.size() - 1 == i) {
                if (i - start > 1) {
                    result.add(new CellRangeAddress(start, i, 0, 0));
                }
            }
        }
        return result;
    }

    /**
     * 读取Excel工作表中的数据
     */
    public static List<List<String>> readExcelSheet (Sheet sheet){
        return readExcelSheet(sheet, 1);
    }
    public static List<List<String>> readExcelSheet (Sheet sheet, boolean firstSkip){
        return readExcelSheet(sheet, firstSkip ? 1 : 0);
    }
    public static List<List<String>> readExcelSheet(Sheet sheet, Integer rowStart) {
        List<List<String>> resultList = new ArrayList<>();
        if (sheet == null) return resultList;
        int rowNos = sheet.getLastRowNum();// 得到excel的总记录条数
        for (int i = rowStart; i <= rowNos; i++) {// 遍历行
            Row row = sheet.getRow(i);
            List<String> list = new ArrayList<>();
            if (row == null || row.equals("")) continue;
            int columNos = row.getLastCellNum();// 表头总共的列数
            for (int j = 0; j < columNos; j++) {
                list.add(getByCell(row.getCell(j)));
            }
            resultList.add(list);
        }
        return resultList;
    }

    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelField {
        String value() default "";

        boolean export() default true;
    }


    /**
     * 将源excel按照特定列按顺序填充
     */
    public static <T> void fillExcel (String filePath, List<T> result) {
        if (StringUtil.isBlank(filePath)) {
            throw new RuntimeException("文件路径不能为空");
        }
        String suffix = getSuffix(filePath);
        if (StringUtil.isBlank(suffix)) {
            throw new RuntimeException("文件后缀不能为空");
        }
        if (!OFFICE_EXCEL_XLS.equals(suffix) && !OFFICE_EXCEL_XLSX.equals(suffix)) {
            throw new RuntimeException("该文件非Excel文件");
        }
        InputStream is = null;
        Workbook workbook = null;
        OutputStream out = null;
        try {
            is = Files.newInputStream(Paths.get(filePath));
            workbook = WorkbookFactory.create(is);
            out = Files.newOutputStream(Paths.get(filePath));
            if (workbook == null) {
                throw new IOException("文件为空");
            }
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            Row firstRow = sheet.getRow(firstRowNum);
            int lastCellNum = firstRow.getLastCellNum();
            Map<String, Integer> headerNumMap = new HashMap<>();
            // 找到表头对应的列在哪
            for (int i = 0; i <= lastCellNum; i++) {
                Cell cell = firstRow.getCell(i);
                String cellValue = getByCell(cell);
                headerNumMap.put(cellValue, i);
            }
            // 通过唯一标示找到对应的行数据
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                if (i > result.size()) {
                    continue;
                }
                T t = result.get(i - 1);
                Row row = sheet.getRow(i);
                Map<String, Object> keyValueMap = new HashMap<>();
                Field[] fields = t.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    String title;
                    if (excelField != null) {
                        if (!excelField.export()) {
                            continue;
                        }
                        title = excelField.value();
                    } else {
                        title = field.getName();
                    }
                    keyValueMap.put(title, field.get(t));
                }
                // 填充回去
                for (Map.Entry<String, Integer> entry : headerNumMap.entrySet()) {
                    String title = entry.getKey();
                    Integer index = entry.getValue();
                    Cell cell = row.getCell(index);
                    Object value = keyValueMap.get(title);
                    if (value != null) {
                        if (cell == null) {
                            cell = row.createCell(index);
                        }
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue(String.valueOf(value));
                    }
                }
            }
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("将源excel按照特定列按顺序填充 失败", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将数据导出到Excel文件
     */
    public static <T> void exportExcelToFile (String outFile, String sheetName, List<T> result) {
        List<Map<String, Object>> resultMap = new ArrayList<>();
        List<String> headList = Collections.emptyList();
        result = Optional.ofNullable(result).orElse(Collections.emptyList());
        if (!result.isEmpty()) {
            headList = getHeadList(result.get(0).getClass());
        }
        try {
            for (T t : result) {
                Field[] fields = t.getClass().getDeclaredFields();
                Map<String, Object> keyValueMap = new HashMap<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    ExcelField excelField = field.getAnnotation(ExcelField.class);
                    String title;
                    if (excelField != null) {
                        if (!excelField.export()) {
                            continue;
                        }
                        title = excelField.value();
                    } else {
                        title = field.getName();
                    }
                    keyValueMap.put(title, field.get(t));
                }
                resultMap.add(keyValueMap);
            }
            writeExcel(outFile, sheetName, headList, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类的表头列表
     */
    private static List<String> getHeadList (Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> headList = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            String title;
            if (excelField != null) {
                if (!excelField.export()) {
                    continue;
                }
                title = excelField.value();
            } else {
                title = field.getName();
            }
            headList.add(title);
        }
        return headList;
    }

    /**
     * 将数据写入到Excel文件
     */
    public static byte[] writeExcel1Merge (String outPath, List<String> sheetList, List<List<List<String>>> result, List<CellRangeAddress> mergeList) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(outPath);

        for (int i = 0; i < sheetList.size(); i++) {
            Sheet sheet = workbook.createSheet(sheetList.get(i));
            ExcelUtil.writeExcelMerge(workbook, sheet, result.get(i), mergeList);
        }

        //OutputStream out = new FileOutputStream(outPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        out.close();
        workbook.close();
        return out.toByteArray();
    }

    /**
     * 将数据写入到Excel文件
     */
    public static byte[] writeExcel1 (String outPath, List<String> sheetList, List<List<List<String>>> result) throws Exception {
        Workbook workbook = ExcelUtil.getWorkbook(outPath);
        //FileUtil.mkdirParentFolder(outPath);
        for (int i = 0; i < sheetList.size(); i++) {
            ExcelUtil.writeExcel(workbook, i, sheetList.get(i), result.get(i));
        }
        //OutputStream out = new FileOutputStream(outPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        out.close();
        workbook.close();
        return out.toByteArray();
    }

    public static Integer countRowsInExcel(Object fileObject) throws IOException {
        InputStream inputStream = null;

        try {
            if (fileObject instanceof File) {
                inputStream = Files.newInputStream(((File) fileObject).toPath());
            } else if (fileObject instanceof MultipartFile) {
                inputStream = ((MultipartFile) fileObject).getInputStream();
            } else {
                throw new IllegalArgumentException("Unsupported file type.");
            }

            return countRows(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static Integer countRows(InputStream inputStream) throws IOException {
        int rowCount = 0;
        try (Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100) // 每次缓存100行
                .bufferSize(4096)  // 读取缓冲区大小
                .open(inputStream)) {

            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            // 迭代每一行
            for (Row row : sheet) {
                if (!isRowEmpty(row)) {
                    rowCount++;
                }
            }
        }
        return rowCount;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                // 如果单元格是字符串类型，还需要检查它是否只包含空格
                if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) {
                    continue; // 只包含空格的字符串单元格，视为空
                }
                return false; // 只要找到一个非空单元格，就认为该行不为空
            }
        }
        return true; // 遍历完所有单元格都没有找到非空内容，则该行为空
    }

    /**
     * 合并两个CSV文件
     *
     * @param file1Path      第一个CSV文件的路径（包含表头）
     * @param file2Path      第二个CSV文件的路径（其表头将被忽略）
     * @param outputFilePath 合并后的输出文件路径
     * @throws IOException 如果发生I/O错误
     */
    public static void mergeCsvFiles(String file1Path, String file2Path, String outputFilePath) throws IOException {
        // 使用 try-with-resources 确保写入器在使用后自动关闭
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            // --- 1. 处理第一个文件 (包括表头) ---
            // 使用 try-with-resources 确保读取器在使用后自动关闭
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1Path))) {
                String line;
                while ((line = reader1.readLine()) != null) {
                    writer.write(line);
                    writer.newLine(); // 写入换行符
                }
            }

            // --- 2. 处理第二个文件 (跳过表头) ---
            try (BufferedReader reader2 = new BufferedReader(new FileReader(file2Path))) {
                // 读取并丢弃第一行（表头）
                reader2.readLine();

                String line;
                while ((line = reader2.readLine()) != null) {
                    writer.write(line);
                    writer.newLine(); // 写入换行符
                }
            }
        }
    }

}
