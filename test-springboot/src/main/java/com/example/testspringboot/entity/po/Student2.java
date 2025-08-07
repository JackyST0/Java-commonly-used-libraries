package com.example.testspringboot.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 10:10
 */
@Data
@AllArgsConstructor
public class Student2 {
    private String name;
    private Integer age;
    private BigDecimal cash;
    private Date dateOfBirth;
}
