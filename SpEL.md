# SpEL

## 介绍
SpEL（Spring Expression Language）是Spring框架提供的一种功能强大的表达式语言，用于在运行时查询和操作对象。  
主要特点和用途包括：    
1. **简洁性**：SpEL表达式使用简洁的语法，使得在Spring配置和代码中能够更简洁地表达复杂的逻辑。
2. **动态性**：SpEL表达式能够在运行时动态地解析和执行，从而提供了更大的灵活性。
3. **强大的功能**：SpEL表达式支持各种功能，如方法调用、字符串模板、集合操作、逻辑运算等，使得在Spring配置和代码中能够完成复杂的逻辑处理。   

SpEL表达式的语法以“#{}”为标记，可以在Spring配置文件的XML中、注解中以及Java代码中使用。  
在XML中，可以在属性值中使用SpEL表达式，例如：
```
<bean id="person" class="com.example.Person">
    <property name="name" value="#{'John Doe'}"/>
    <property name="age" value="#{30}"/>
</bean>
```
在注解中，可以使用@Value注解来注入SpEL表达式的值，例如：
```
@Value("#{'Hello World'}")
private String message;
```
在Java代码中，可以创建Expression对象来执行SpEL表达式，例如：
```
User user = new User("张三");
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello ' + #user.name");
StandardEvaluationContext context = new StandardEvaluationContext();
context.setVariable("user", user);
String name = (String) exp.getValue(context);
System.out.println(name);
```
除了基本的表达式功能外，SpEL还提供了对集合、数组、Map等复杂数据结构的操作，以及对Spring容器中的Bean的引用和操作。这使得在Spring配置和代码中能够更方便地处理复杂的数据结构和业务逻辑。

## SpEL表达式的优势
SpEL表达式（Spring Expression Language）的优势主要体现在以下几个方面：
1. **动态性**：SpEL表达式在运行时执行，可以根据应用程序的状态和环境动态地计算值。这种动态性使得SpEL能够适应各种复杂的场景，并提供更大的灵活性。
2. **简洁性**：SpEL表达式提供了一种简洁、紧凑的语法，用于表示和操作对象。与传统的XML配置相比，SpEL表达式可以更简洁地表达相同的逻辑，从而减少了配置的复杂性。
3. **强大的功能**：SpEL表达式支持丰富的功能，包括方法调用、字符串模板、集合操作、逻辑运算等。这使得在Spring配置和代码中能够更轻松地处理复杂的逻辑和数据结构。
4. **易于集成**：SpEL表达式与Spring框架紧密集成，可以方便地与其他Spring特性结合使用，如依赖注入、AOP（面向切面编程）、事务管理等。这使得在Spring应用程序中能够更轻松地实现各种功能。
5. **灵活性**：SpEL表达式可以在多个地方使用，包括XML配置文件、注解和Java代码中。这种灵活性使得SpEL能够适应不同的开发需求和场景。
6. **可扩展性**：SpEL表达式的设计是可扩展的，可以通过自定义函数和操作符来扩展其功能。这使得开发者可以根据自己的需求定制SpEL表达式的行为。

## 语法
SpEL（Spring Expression Language）的语法包括以下几种基本表达式：
1. **字面量表达式**：
    - 支持的字面量类型包括：字符串、数字（int、long、float、double）、布尔类型、null类型。
    - 示例：`'Hello World'`, `42`, `3.14`, `true`, `null`。
2. **算数运算表达式**：
    - 支持的运算符包括：加（+）、减（-）、乘（*）、除（/）、求余（%）、幂（^）。
    - 还提供了求余（MOD）和除（DIV）两个额外运算符，它们与“%”和“/”是等价的，且不区分大小写。
    - 示例：`4 + 5`, `10 - 3`, `2 * 7`, `14 / 2`, `21 % 3`, `2 ^ 3`。
3. **关系表达式**：
    - 支持的关系运算符包括：等于（==）、不等于（!=）、大于（>）、大于等于（>=）、小于（<）、小于等于（<=）。
    - 示例：`4 > 3`, `2 != 5`, `10 >= 10`, `1 <= 2`。
4. **逻辑表达式**：
    - 支持的逻辑运算符包括：AND（&&）、OR（||）、NOT（!）。
    - 示例：`(a > b) && (c < d)`, `(x == y) || (z != w)`, `!isTrue`。
5. **正则表达式**：
    - 可以使用正则表达式进行字符串匹配。
    - 示例：`'abc'.matches('.b.')` 将返回 `true`。
6. **集合操作**：
    - 支持集合操作，如选择（?）、投影（![]）、集合的大小（size）、是否包含（in）等。
    - 示例：`list.?[#this > 3]`, `list.[#this.name]`, `list.size()`, `'item' in list`。
7. **方法调用**：
    - 可以调用对象的方法。
    - 示例：`'Hello World'.toUpperCase()`, `someObject.someMethod()`。
8. **变量引用**：
    - 可以引用在表达式上下文中定义的变量。
    - 示例：`#variableName`。
9. **类型运算符**：
    - 提供了一个特殊的运算符 `T()`，用于指定一个类型字面量。
    - 示例：`T(java.lang.String).class` 将返回 `java.lang.String.class`。
10. **三元运算符**：
    - 类似于其他编程语言中的条件运算符，格式为 `condition ? valueIfTrue : valueIfFalse`。
    - 示例：`(a > b) ? a : b`。
11. **区间运算符**：
    - 使用 `between` 关键字来表示一个值是否在指定的区间内。
    - 示例：`1 between 2 and 3` 将返回 `false`，而 `2 between 2 and 3` 将返回 `true`。
12. **索引器**：
    - 可以使用方括号 `[]` 来访问数组、列表、映射（如Map）的元素。
    - 示例：`myList[0]`, `myMap['key']`。
13. **函数调用**：
    - 可以直接调用静态方法或实例方法。
    - 示例：`T(java.lang.Math).max(a, b)`, `myObject.myMethod()`。
14. **安全导航运算符**：
    - 使用 `?.` 来进行安全的导航访问，如果左侧对象为 `null` 则不会抛出异常，而是返回 `null`。
    - 示例：`user?.address?.city`。   

这些表达式可以单独使用，也可以组合起来创建更复杂的表达式。SpEL 提供了强大而灵活的表达式语言，可以在运行时动态地评估和计算表达式。

## 应用场景
SpEL表达式在Spring框架中有广泛的应用场景，以下是其中一些常见的使用场景：
1. **XML配置中的属性值**：在Spring的XML配置文件中，可以使用SpEL表达式来设置bean的属性值。例如，你可以使用SpEL表达式来引用其他bean、计算属性值或执行简单的逻辑操作。
2. **注解中的值**：在Spring的注解中，你也可以使用SpEL表达式来注入值。例如，在`@Value`注解中，你可以使用SpEL表达式来指定一个属性的值。
3. **方法参数**：在调用方法时，可以使用SpEL表达式来动态地设置方法的参数值。这可以在配置文件中或者在Java代码中实现。
4. **集合操作**：SpEL表达式提供了对集合的支持，你可以使用它来操作集合，如遍历集合、过滤元素、投影等。
5. **条件判断**：SpEL表达式支持条件运算，你可以在配置或代码中使用它来进行条件判断，根据条件的结果来设置属性值或执行不同的逻辑。
6. **属性值的动态计算**：有时你可能需要根据其他属性值或环境变量来动态计算某个属性的值。SpEL表达式可以帮助你实现这一点。
7. **简化配置**：通过使用SpEL表达式，你可以简化Spring的配置。例如，你可以使用它来避免冗长的XML配置，或者在Java配置中更简洁地表达逻辑。
8. **与Spring其他特性结合使用**：SpEL表达式可以与其他Spring特性结合使用，如AOP（面向切面编程）、事务管理等。你可以使用SpEL表达式来定义切点表达式、事务属性等。

需要注意的是，虽然SpEL表达式功能强大且灵活，但并不意味着它应该在所有场景下都被使用。在决定使用SpEL表达式之前，应该仔细考虑其是否适合当前的需求，并权衡其带来的好处和可能的复杂性。

## 符号（#、$、@）各有什么不同
### 1. `#`开头（`#{}`）     
`#{}` 用于定义 SpEL 表达式。在这个上下文中，你可以执行方法调用、访问字段、执行算术运算等。`#{}` 内的表达式会被计算，并且其结果会被返回。这通常用于动态地计算值或执行复杂的逻辑。            
示例：  
```
#{systemProperties['os.name']} // 访问系统属性
#{myBean.myMethod()} // 调用Bean的方法
#{[1, 2, 3]} // 内联列表
#{{'key':'value'}} // 内联映射
```
在 `#{}` 内部，你还可以访问 Spring 提供的内置变量和方法，例如 `#root`、`#this`、`#parameters` 等。

### 2. `@`开头（`@`）
`@`符号在SpEL中用于引用命名Bean。这是Spring EL的特定功能，它允许你直接引用Spring容器中的Bean，而不需要通过`ApplicationContext`来获取它们。  
示例：  
```
@myBean // 引用名为'myBean'的Bean
```
请注意，当你使用`@`来引用Bean时，实际上你是在告诉SpEL引擎从Spring的ApplicationContext中查找并注入这个Bean。

### 3. `$` 开头（`${}`）
在 SpEL 中，`$` 通常用于直接引用变量。这通常在从外部源（如配置文件）注入变量时使用。在 Spring 的 `@Value` 注解中，你可以使用 `$` 来引用配置文件中定义的属性。     
示例：
```
@Value("${my.property}")
private String myProperty;
```
在这个例子中，`${my.property}` 告诉 Spring 从配置文件中查找 `my.property` 的值，并将其注入到 `myProperty` 字段中。
```
my.property=Hello, world!
```

**总结：**
- `#{}` 用于编写 SpEL 表达式，执行方法调用、计算值等。
- `$` 通常用于引用配置文件中定义的属性，在 `@Value` 注解等场景中使用。
- `@`用于直接引用Spring容器中的Bean。     

需要注意的是，`$` 在SpEL表达式中并不总是用于引用变量。在某些情况下，`$` 也可以用于表示字面量或特定的 SpEL 功能。但是，在大多数情况下，`$` 在 SpEL 中与变量引用相关，而 `#` 则用于定义表达式。

## Spring中@Value() 的用法
`@Value` 是Spring框架中的一个注解，它主要用于从配置文件中注入属性值到Bean的字段、方法参数或构造函数中。以下是 `@Value` 注解的一些常见用法：
### 1. 直接注入简单类型的值
你可以使用 `@Value` 注解直接将配置文件中定义的属性值注入到Bean的字段中。例如：
```
@Value("Hello World")
private String greeting;
```
在这个例子中，`greeting` 字段将被赋值为 “Hello World”。

### 2. 注入来自配置文件的值
你可以使用占位符语法 `${...}` 来引用配置文件（如 `application.properties` 或 `application.yml`）中的值。例如：
```
@Value("${app.name}")
private String appName;
```
在这个例子中，`appName` 字段将被赋值为 `app.name` 在配置文件中定义的值。

### 3. 注入系统属性：
使用 `#{...}` 语法，你可以注入系统属性。例如：
```
@Value("#{systemProperties['os.name']}")
private String osName;
```
这里，`osName` 字段将被赋值为系统的 `os.name` 属性。

### 4. 注入环境变量
同样使用 `#{...}` 语法，你可以注入环境变量。例如：
```
@Value("#{environment['MY_ENV_VAR']}")
private String myEnvVar;
```
这里，`myEnvVar` 字段将被赋值为 `MY_ENV_VAR` 环境变量的值。

### 5. 注入表达式的结果
你可以在 `#{...}` 中使用SpEL表达式来计算值。例如：
```
@Value("#{10 * 2}")
private int result;
```
这里，`result` 字段将被赋值为 `10 * 2` 的计算结果，即 20。

### 6. 注入数组、集合和Map
你可以使用 `@Value` 注解来注入数组、集合和Map。例如：
```
@Value("#{'red', 'green', 'blue'}")
private List<String> colors;

@Value("#{{'key1':'value1', 'key2':'value2'}}")
private Map<String, String> configMap;
```
在这个例子中，`colors` 列表将被赋值为包含 `'red'`, `'green'`, 和 `'blue'` 的列表，`configMap` 将被赋值为包含 `'key1': 'value1'` 和 `'key2': 'value2'` 的映射。

### 7. 使用默认值
当配置文件中没有定义某个属性时，你可以为 `@Value` 注解提供一个默认值。例如：
```
@Value("${app.version:1.0.0}")
private String appVersion;
```
如果 `app.version` 没有在配置文件中定义，`appVersion` 将被赋值为默认值 “1.0.0”。
```
请注意，为了使 @Value 注解能够工作，你的类需要被Spring容器管理，即它应该是一个Bean（可以通过
@Component、@Service、@Repository 或 @Controller
等注解来标识）。此外，如果你在静态变量上使用
@Value，那么它可能无法正常工作，因为静态变量在类加载时初始化，而这时Spring可能还没有解析 @Value
注解。为了避免这种情况，你应该在实例变量上使用 @Value，并通过setter方法或构造函数注入来设置静态变量的值。
```

## 使用示例
### 1. 简单表达式
```
ExpressionParser parser = new SpelExpressionParser();
// 简单的字符串表达式
Expression exp1 = parser.parseExpression("'Hello World'");
String message = (String) exp1.getValue();
System.out.println(message); // 输出: Hello World

// 算术表达式
Expression exp2 = parser.parseExpression("10 * 2");
int result = (int) exp2.getValue();
System.out.println(result); // 输出: 20
```

### 2. 从对象中取值
```
ExpressionParser parser = new SpelExpressionParser();
StandardEvaluationContext context = new StandardEvaluationContext();

Expression exp1 = parser.parseExpression("#user.name");
context.setVariable("user", new User("Alice"));
String userName = (String) exp1.getValue(context);
System.out.println(userName); // 输出: Alice

List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
// 过滤出长度大于4的名字
Expression exp3 = parser.parseExpression("#names.?[#this.length > 4]");
context.setVariable("names", names);
List<String> filteredNames = (List<String>) exp3.getValue(context);
System.out.println(filteredNames); // 输出: [Charlie]
```

### 3. 读取系统环境变量
```
public void systemProperties() {
    Properties properties = System.getProperties();
    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        System.out.println(entry.getKey() + "===" + entry.getValue());
    }
}
```
```
// 输出 zh
@Value("#{systemProperties['user.language']}")
```

