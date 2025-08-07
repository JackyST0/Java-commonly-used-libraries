package com.example.testspringboot.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 9:46
 */
@Data
@AllArgsConstructor
public class Teacher {
    private String teacherName;
    private Integer teacherAge;
    private Wife wife;

    public Teacher(String teacherName, Integer teacherAge) {
        this.teacherName = teacherName;
        this.teacherAge = teacherAge;
    }
}
