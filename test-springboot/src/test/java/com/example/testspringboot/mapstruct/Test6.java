package com.example.testspringboot.mapstruct;

import com.example.testspringboot.entity.po.Student2;
import com.example.testspringboot.entity.vo.StudentVO3;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 10:15
 */
public class Test6 {

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("0.3");
        Date date = new Date();
        Student2 student2 = new Student2("刘备", 33, bigDecimal, date);
        StudentVO3 studentVO3 = StudentMapper.INSTANCE.toStudentVO6(student2);
        System.out.println(studentVO3);
    }
}
