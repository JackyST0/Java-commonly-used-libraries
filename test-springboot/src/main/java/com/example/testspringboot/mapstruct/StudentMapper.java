package com.example.testspringboot.mapstruct;

import com.example.testspringboot.entity.po.Student1;
import com.example.testspringboot.entity.po.Student2;
import com.example.testspringboot.entity.po.Teacher;
import com.example.testspringboot.entity.vo.StudentVO1;
import com.example.testspringboot.entity.vo.StudentVO2;
import com.example.testspringboot.entity.vo.StudentVO3;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/9/25 9:30
 */
//@Mapper(componentModel = "spring") // 依赖注入
@Mapper
public interface StudentMapper {

    // 映射器工厂
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    // 当成员变量名相同时的使用
    StudentVO1 toStudentVO1(Student1 student1);

    // 成员变量名不相同时的使用
    @Mappings({
            @Mapping(source = "name", target = "nameVo"),
            @Mapping(source = "age", target = "ageVo")})
    StudentVO2 toStudentVO2(Student1 student1);

    // 多参数源映射
    @Mappings({
            @Mapping(source = "teacher.teacherName", target = "nameVo"),
            @Mapping(source = "student1.age", target = "ageVo")})
    StudentVO2 toStudentVO3(Student1 student1, Teacher teacher);

    // 多层嵌套映射
    @Mappings({
            @Mapping(source = "teacher.wife.name", target = "nameVo"),
            @Mapping(source = "student1.age", target = "ageVo")})
    StudentVO2 toStudentVO4(Student1 student1, Teacher teacher);

    // 更新现有的Bean
    @Mappings({
            @Mapping(source = "name", target = "nameVo"),
            @Mapping(source = "age", target = "ageVo")})
    void toStudentVO5(@MappingTarget StudentVO2 studentVO2, Student1 student1);

    // 数据类型转换
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "age", target = "age", numberFormat = "#.00"),
            @Mapping(source = "cash", target = "cash", numberFormat = "#.#E0"),
            @Mapping(source = "dateOfBirth", target = "dateOfBirth", numberFormat = "yyyy-MM-dd")
    })
    StudentVO3 toStudentVO6(Student2 student2);
}
