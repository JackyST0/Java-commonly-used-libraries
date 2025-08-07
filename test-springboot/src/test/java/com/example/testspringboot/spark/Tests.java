package com.example.testspringboot.spark;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import scala.Tuple2;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class Tests {

    @Resource
    private SparkSession sparkSession;

    @Resource
    private JavaSparkContext javaSparkContext;

    // 读取 txt 文件
    @Test
    public void testSparkText() {
        String file = "E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\spark\\word.txt";
        JavaRDD<String> fileRDD =  javaSparkContext.textFile(file);
        System.out.println("fileRDD:" + fileRDD.collect());
        JavaRDD<String> wordsRDD = fileRDD.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
        System.out.println("wordsRDD:" + wordsRDD.collect());
        JavaPairRDD<String, Integer> wordAndOneRDD = wordsRDD.mapToPair(word -> new Tuple2<>(word, 1));
        System.out.println("wordAndOneRDD:" + wordAndOneRDD.collect());
        JavaPairRDD<String, Integer> wordAndCountRDD = wordAndOneRDD.reduceByKey(Integer::sum);
        System.out.println("wordAndCountRDD:" + wordAndCountRDD.collect());

        //输出结果
        List<Tuple2<String, Integer>> result = wordAndCountRDD.collect();
        result.forEach(System.out::println);
    }

    // 读取 csv 文件
    @Test
    public void testSparkCsv() {
        String file = "E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\spark\\test.csv";
        JavaRDD<String> fileRDD = javaSparkContext.textFile(file);
        System.out.println("fileRDD:" + fileRDD.collect());
        JavaRDD<String> wordsRDD = fileRDD.flatMap(line -> Arrays.asList(line.split(",")).iterator());
        System.out.println("wordsRDD:" + wordsRDD.collect());

        //输出结果
        System.out.println(wordsRDD.collect());
    }

    // 读取 MySQL 数据库表
    @Test
    public void testSparkMysql() throws IOException {
        Dataset<Row> jdbcDF = sparkSession.read()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/advt?serverTimezone=UTC")
                .option("dbtable", "(SELECT * FROM advt) test")
                .option("user", "root")
                .option("password", "root")
                .option("driver", "com.mysql.cj.jdbc.Driver")
                .load();

        jdbcDF.printSchema();
        jdbcDF.show();

        //转化为RDD
        JavaRDD<Row> rowJavaRDD = jdbcDF.javaRDD();
        System.out.println(rowJavaRDD.collect());

        List<Row> list = rowJavaRDD.collect();
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\spark\\test.txt"));
        for (Row row : list) {
            bw.write(row.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    // 读取 Json 文件
    @Test
    public void testSparkJson() {
        Dataset<Row> df = sparkSession.read().json("E:\\local-projects\\test-springboot\\src\\main\\resources\\file\\spark\\test.json");
        df.printSchema();

        df.createOrReplaceTempView("t");
        Dataset<Row> row = sparkSession.sql("select age,name from t where age > 3");

        JavaRDD<Row> rowJavaRDD = row.javaRDD();
        System.out.println(rowJavaRDD.collect());
    }

}
