package com.example.testspringboot.mapstruct;

import com.example.testspringboot.entity.po.Student1;
import com.example.testspringboot.entity.vo.StudentVO2;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 10:05
 */
public class Test5 {

    public static void main(String[] args) {
        Student1 student1 = new Student1("刘备", 33);
        StudentVO2 studentVO2 = new StudentVO2("赵云", 22);
        StudentMapper.INSTANCE.toStudentVO5(studentVO2, student1);
        System.out.println(studentVO2);
    }
}
