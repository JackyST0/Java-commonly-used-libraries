# Java Excel

## Apache POI
这是一个成熟且功能丰富的库，可以用于读取和写入Microsoft Office格式的文件，包括Excel。它提供了大量的功能和选项，可以处理复杂的Excel文件和操作。但是，其API相对复杂，对于初学者来说，可能需要一些时间来熟悉和理解。
- ### 简单使用
    - #### 写入
        ```
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
        ```

    - #### 读取
        ```
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
        ```

## EasyExcel
EasyExcel是一个基于Apache POI的库，主要目标是简化Excel的读写操作。它的API设计得更为简单易用，而且对于大数据量的处理，EasyExcel提供了低内存占用的解决方案。但是，由于它是一个相对较新的库，可能不包含Apache POI所有的功能。
- ### 简单使用
    - #### 实体类
        ```
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
            private String name;
            @ExcelProperty(value = "年龄")
            private Integer age;
            @ExcelProperty(value = "职业")
            private String occupation;
        }
        ```

    - #### 监听器
        ```
        public class ExcelListen extends AnalysisEventListener<ExcelData> {
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
        ```

    - #### 写入
        ```
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

        // 初始化要写入的数据
        private static List<ExcelData> initData() {
            List<ExcelData> dataList = new ArrayList<>();
            dataList.add(new ExcelData("John", 25, "Engineer"));
            dataList.add(new ExcelData("Alice", 30, "Manager"));
            dataList.add(new ExcelData("Bob", 28, "Developer"));

            return dataList;
        }
        ```
    
    - #### 读取
        ```
        public void readExcel() {
            // 使用 EasyExcel 读取 Excel 文件
            EasyExcel.read(path, ExcelData.class, new ExcelListen()).sheet().doRead();
        }
        ```
        
## Hutool-poi
这是Hutool工具包中的一个模块，用于简化POI的操作。Hutool-poi提供了一些便捷的方法，使得在Java中操作Excel变得更加简单。但是，与Apache POI和EasyExcel相比，它可能在功能上稍微有些不足。
- ### 简单使用
    - #### 写入
        ```
        private ExcelWriter createWriter(String path) {
            if (new File(path).exists()) {
                new File(path).delete();
            }
            return ExcelUtil.getWriter(path);
        }
        ```

        - List
            ```
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
            ```

        - Map
            ```
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
            ```

        - Bean
            ```
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
            ```

    - #### 读取
        ```
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
        ```

### 总结
总的来说，选择哪个库取决于实际的具体需求。如果需要处理复杂的Excel文件和操作，Apache POI可能是最好的选择。如果希望有一个简单易用的API，且要处理大量数据，EasyExcel可能更合适。如果已经在使用Hutool，并且需求相对简单，那么Hutool-poi可能是一个好的选择。