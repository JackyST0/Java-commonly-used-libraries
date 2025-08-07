package com.example.testspringboot.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/26 14:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;  // 姓名
    private int salary; // 薪资
    private int age; // 年龄
    private String sex; //性别
    private String area;  // 地区

    // 构造方法
    public Person(String name, int salary, String sex,String area) {
        this.name = name;
        this.salary = salary;
        this.sex = sex;
        this.area = area;
    }
}
