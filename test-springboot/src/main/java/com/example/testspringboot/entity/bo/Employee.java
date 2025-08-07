package com.example.testspringboot.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/4/18 17:48
 */
@Data
@AllArgsConstructor
public class Employee {
    private String name;
    private Date birthDate;
    private Integer payment;
    private Double bonus;
}
