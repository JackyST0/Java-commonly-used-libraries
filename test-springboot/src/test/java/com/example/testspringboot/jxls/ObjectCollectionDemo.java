package com.example.testspringboot.jxls;

import com.example.testspringboot.entity.bo.Employee;
import com.example.testspringboot.util.JxlsUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/4/18 18:01
 */
@Slf4j
public class ObjectCollectionDemo {

    public static void main(String[] args) throws ParseException, IOException {
        log.info("Running Object Collection demo");

        List<Employee> employees = generateSampleEmployeeData();
        OutputStream os = Files.newOutputStream(Paths.get("jxls.xlsx"));
        Map<String , Object> model= new HashMap<>();
        model.put("employees", employees);
        model.put("nowDate", new Date());
        model.put("sheetNames", Arrays.asList("1","2","3"));
        JxlsUtils.exportExcel("jxls.xlsx", os, model);
        os.close();
    }

    public static List<Employee> generateSampleEmployeeData() throws ParseException {
        List<Employee> employees = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
        employees.add( new Employee("Elsa", dateFormat.parse("1970-Jul-10"), 1500, 0.15) );
        employees.add( new Employee("Oleg", dateFormat.parse("1973-Apr-30"), 2300, 0.25) );
        employees.add( new Employee("Neil", dateFormat.parse("1975-Oct-05"), 2500, 0.00) );
        employees.add( new Employee("Maria", dateFormat.parse("1978-Jan-07"), 1700, 0.15) );
        employees.add( new Employee("John", dateFormat.parse("1969-May-30"), 2800, 0.20) );
        return employees;
    }
}
