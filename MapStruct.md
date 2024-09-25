# MapStruct

### 简介
MapStruct是一个用于Java的代码生成库，它的主要功能是在编译时自动生成JavaBean之间的映射代码，例如在数据传输对象（DTO）和领域模型对象之间进行转换。

### 简单使用
涉及到的实体类如下：
```
@Data
@AllArgsConstructor
public class Student1 {
    private String name;
    private Integer age;
}
```
```
@Data
@AllArgsConstructor
public class Student2 {
    private String name;
    private Integer age;
    private BigDecimal cash;
    private Date dateOfBirth;
}
```
```
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
```
```
@Data
@AllArgsConstructor
public class Wife {
    private String name;
    private Integer age;
}
```
```
@Data
public class StudentVO1 {
    private String name;
    private Integer age;
}
```
```
@Data
@AllArgsConstructor
public class StudentVO2 {
    private String nameVo;
    private Integer ageVo;
}
```
```
@Data
public class StudentVO3 {
    private String name;
    private String age;
    private String cash;
    private String dateOfBirth;
}
```

- #### 成员变量名相同时的使用
    Studnet1类的age和name与StudentVO1类的age和name对应上时
    ```
    StudentVO1 toStudentVO1(Student1 student1);
    ```

- #### 成员变量名不相同时的使用  
    Studnet1类的age和name与StudentVO2类的ageVO和nameVO对应不上时
    ```
    @Mappings({
        @Mapping(source = "name", target = "nameVo"),
        @Mapping(source = "age", target = "ageVo")})
    StudentVO2 toStudentVO2(Student1 student1);
    ```

- #### 多参数源映射  
    某些时候，我们的源不是一个，例如从数据库中查询出来了学生和老师，我们需要将老师的名字给VO2的nameVo字段，学生的年龄给VO2的ageVo字段时可以使用多参数源的映射方式。
    ```
    @Mappings({
        @Mapping(source = "teacher.teacherName", target = "nameVo"),
        @Mapping(source = "student1.age", target = "ageVo")})
    StudentVO2 toStudentVO3(Student1 student1, Teacher teacher);
    ```

- #### 多层嵌套映射
    有些时候我们需要多层映射，例如老师类中有自己的一个老婆类（男老师），然后我们需要将老师类中的老婆类的名字，赋值给VO2，而年龄则使用学生的年龄。听上去怪怪的，就像学生有了老师的老婆😂😂。
    ```
    @Mappings({
        @Mapping(source = "teacher.wife.name", target = "nameVo"),
        @Mapping(source = "student1.age", target = "ageVo")})
    StudentVO2 toStudentVO4(Student1 student1, Teacher teacher);
    ```

- #### 更新现有的Bean
    某些情况下，你需要不创建目标类型的新实例，而是更新该类型的现有实例的映射。可以通过为目标对象添加参数并使用@MappingTarget标记此参数来实现此类映射。  
    例如Student1我们将学生类的名字和年龄映射到VO2中，但是不创建新的实例。
    ```
    @Mappings({
        @Mapping(source = "name", target = "nameVo"),
        @Mapping(source = "age", target = "ageVo")})
    void toStudentVO5(@MappingTarget StudentVO2 studentVO2, Student1 student1);
    ```

- #### 映射器工厂
    前面我们在Mapper接口中代码中一直有一行代码，如下所示，是MapStruct为我们提供的映射工厂，指定接口类型后自动帮我们创建接口的实现，且保证是线程安全的单例，无需自己手动创建。
    ```
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
    ```

- #### 依赖注入
    某些时候尤其是在做项目时，我们用到了Sping，希望映射后的新实例是交给Spring管理。这时候就需要进行依赖注入了。只需要在Mapper接口中的@Mapper注解中加入componentModel = "spring"即可。
    ```
    @Mapper(componentModel = "spring")
    ```

- #### 数据类型转换
    映射属性在源对象和目标对象中具有相同的类型，这种情况不全有。例如，属性在源bean中可以是Integer类型，但在目标bean中可以是Long类型。另一个例子是对其他对象的引用，这些对象应该映射到目标模型中的相应类型。例如：Teacher类可能有一个Wife类型的属性wife，在映射VO对象时需要将其转换为StudentVO对象。  
    在许多情况下，MapStruct会自动处理类型转换。例如，如果属性在源bean中的类型为int，但在目标bean中的类型为String，则生成的代码将分别通过调用String.valueOf(int)和Integer.parseInt(String)来透明地执行转换。  
    通过案例来实现从Integer转换为String 从BigDecimal到String的转换 以及从Date到String的转换
    ```
    @Mappings({
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "age", target = "age", numberFormat = "#.00"),
        @Mapping(source = "cash", target = "cash", numberFormat = "#.#E0"),
        @Mapping(source = "dateOfBirth", target = "dateOfBirth", numberFormat = "yyyy-MM-dd")
    })
    StudentVO3 toStudentVO6(Student2 student2);
    ```

- #### 映射集合
    在映射集合的时候，我们同样可以进行类型之间的转换，如下所示使用@MapMapping注解指定输出类型即可。
    ```
    @MapMapping(valueDateFormat = "yyyy-MM-dd")
    Map<String,String> toMap(Map<String, Date> map);
    ```