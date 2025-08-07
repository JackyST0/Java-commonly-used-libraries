package com.example.testspringboot.mapstruct;

import com.example.testspringboot.entity.po.Student1;
import com.example.testspringboot.entity.po.Teacher;
import com.example.testspringboot.entity.vo.StudentVO2;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 9:49
 */
public class Test3 {

    public static void main(String[] args) {
        Student1 student1 = new Student1("马谡", 33);
        Teacher teacher = new Teacher("诸葛亮", 22);
        StudentVO2 studentVO2 = StudentMapper.INSTANCE.toStudentVO3(student1, teacher);
        System.out.println(studentVO2);
    }
}
