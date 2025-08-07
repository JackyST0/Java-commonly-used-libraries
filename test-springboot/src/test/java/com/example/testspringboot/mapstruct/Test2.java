package com.example.testspringboot.mapstruct;

import com.example.testspringboot.entity.po.Student1;
import com.example.testspringboot.entity.vo.StudentVO2;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 9:45
 */
public class Test2 {

    public static void main(String[] args) {
        Student1 student1 = new Student1("司马懿", 22);
        StudentVO2 studentVO2 = StudentMapper.INSTANCE.toStudentVO2(student1);
        System.out.println(studentVO2);
    }
}
