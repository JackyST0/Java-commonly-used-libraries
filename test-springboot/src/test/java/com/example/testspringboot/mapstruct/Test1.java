package com.example.testspringboot.mapstruct;

import com.example.testspringboot.entity.po.Student1;
import com.example.testspringboot.entity.vo.StudentVO1;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 9:38
 */
public class Test1 {

    public static void main(String[] args) {
        Student1 student1 = new Student1("诸葛亮", 22);
        StudentVO1 studentVO1 = StudentMapper.INSTANCE.toStudentVO1(student1);
        System.out.println(studentVO1);
    }
}
