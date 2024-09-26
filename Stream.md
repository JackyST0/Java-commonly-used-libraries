# Java 8 Stream

### 简介
Java 8 是一个非常成功的版本，这个版本新增的Stream，配合同版本出现的 Lambda ，给我们操作集合（Collection）提供了极大的便利。

### Stream初相识
概括讲，可以将Stream流操作分为3种类型：
- 创建Stream
- Stream中间处理
- 终止Stream  

每个Stream管道操作类型都包含若干API方法，先列举下各个API方法的功能介绍。
- 开始管道  
    主要负责新建一个Stream流，或者基于现有的数组、List、Set、Map等集合类型对象创建出新的Stream流。
    | API | 功能说明 |
    | ---- | ---- |
    | stream() | 创建出一个新的stream串行流对象 |
    | parallelStream() | 创建出一个可并行执行的stream流对象 |
    | Stream.of() | 通过给定的一系列元素创建一个新的Stream串行流对象 |

- 中间管道  
    负责对Stream进行处理操作，并返回一个新的Stream对象，中间管道操作可以进行叠加。
    | API | 功能说明 |
    | ---- | ---- |
    | filter() | 按照条件过滤符合要求的元素， 返回新的stream流 |
    | map() | 将已有元素转换为另一个对象类型，一对一逻辑，返回新的stream流 |
    | flatMap() | 将已有元素转换为另一个对象类型，一对多逻辑，即原来一个元素对象可能会转换为1个或者多个新类型的元素，返回新的stream流 |
    | limit() | 仅保留集合前面指定个数的元素，返回新的stream流 |
    | skip() | 跳过集合前面指定个数的元素，返回新的stream流 |
    | concat() | 将两个流的数据合并起来为1个新的流，返回新的stream流 |
    | distinct() | 对Stream中所有元素进行去重，返回新的stream流 |
    | sorted() | 对stream中所有的元素按照指定规则进行排序，返回新的stream流 |
    | peek() | 对stream流中的每个元素进行逐个遍历处理，返回处理后的stream流 |

- 终止管道  
    顾名思义，通过终止管道操作之后，Stream流将会结束，最后可能会执行某些逻辑处理，或者是按照要求返回某些执行后的结果数据。
    | API | 功能说明 |
    | ---- | ---- |
    | count() | 返回stream处理后最终的元素个数 |
    | max() | 返回stream处理后的元素最大值 |
    | min() | 返回stream处理后的元素最小值 |
    | findFirst() | 找到第一个符合条件的元素时则终止流处理 |
    | findAny() | 找到任何一个符合条件的元素时则退出流处理，这个对于串行流时与findFirst相同，对于并行流时比较高效，任何分片中找到都会终止后续计算逻辑 |
    | anyMatch() | 返回一个boolean值，类似于isContains(),用于判断是否有符合条件的元素 |
    | allMatch() | 返回一个boolean值，用于判断是否所有元素都符合条件 |
    | noneMatch() | 返回一个boolean值， 用于判断是否所有元素都不符合条件 |
    | collect() | 将流转换为指定的类型，通过Collectors进行指定 |
    | toArray() | 将流转换为数组 |
    | iterator() | 将流转换为Iterator对象 |
    | foreach() | 无返回值，对元素进行逐个遍历，然后执行给定的处理逻辑 |

### Stream初使用  
这是后面案例中使用的员工类：
```
@Data
@AllArgsConstructor
public class Person {
    private String name;  // 姓名
    private int salary; // 薪资
    private int age; // 年龄
    private String sex; //性别
    private String area;  // 地区

    // 构造方法
    public Person(String name, int salary, String sex,String area) {
        this.name = name;
        this.salary = salary;
        this.sex = sex;
        this.area = area;
    }
}
```

- 遍历/匹配（foreach/find/match）  
Stream也是支持类似集合的遍历和匹配元素的，只是Stream中的元素是以Optional类型存在的。Stream的遍历、匹配非常简单。
    ```
    List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
    // 遍历输出符合条件的元素
    list.stream().filter(x -> x > 6).forEach(System.out::println);
    // 匹配第一个
    Optional<Integer> findFirst = list.stream().filter(x -> x > 6).findFirst();
    // 匹配任意（适用于并行流）
    Optional<Integer> findAny = list.parallelStream().filter(x -> x > 6).findAny();
    // 是否包含符合特定条件的元素
    boolean anyMatch = list.stream().anyMatch(x -> x > 6);
    System.out.println("匹配第一个值：" + findFirst.get());    // 匹配第一个值：7
    System.out.println("匹配任意一个值：" + findAny.get());    // 匹配任意一个值：8
    System.out.println("是否存在大于6的值：" + anyMatch);    // 是否存在大于6的值：true
    ```

- 筛选（filter）  
筛选，是按照一定的规则校验流中的元素，将符合条件的元素提取到新的流中的操作。
    ```
    List<Integer> list = Arrays.asList(6, 7, 3, 8, 1, 2, 9);
    Stream<Integer> stream = list.stream();
    stream.filter(x -> x > 7).forEach(System.out::println);
    ```

- 聚合（max/min/count)   
max、min、count这些字眼你一定不陌生，没错，在mysql中我们常用它们进行数据统计。Java stream中也引入了这些概念和用法，极大地方便了我们对集合、数组的数据统计工作。
    ```
    List<String> list = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");
    Optional<String> max = list.stream().max(Comparator.comparing(String::length));
    Optional<String> min = list.stream().min(Comparator.comparing(String::length));
    System.out.println("最长的字符串：" + max.get());    // 最长的字符串：weoujgsd
    System.out.println("最长的字符串：" + min.get());    // 最长的字符串：pot
    ```
    ```
    List<Integer> list = Arrays.asList(7, 6, 4, 8, 2, 11, 9);
    long count = list.stream().filter(x -> x > 6).count();
    System.out.println("list中大于6的元素个数：" + count);    // list中大于6的元素个数：4
    ```

- 映射(map/flatMap)  
映射，可以将一个流的元素按照一定的映射规则映射到另一个流中。分为map和flatMap：
    - map：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
        ```
        String[] strArr = {"abcd", "bcdd", "defde", "fTr"};
        List<String> strList = Arrays.stream(strArr).map(String::toUpperCase).collect(Collectors.toList());

        List<Integer> intList = Arrays.asList(1, 3, 5, 7, 9, 11);
        List<Integer> intListNew = intList.stream().map(x -> x + 3).collect(Collectors.toList());

        System.out.println("每个元素大写：" + strList);    // 每个元素大写：[ABCD, BCDD, DEFDE, FTR]
        System.out.println("每个元素+3：" + intListNew);    // 每个元素+3：[4, 6, 8, 10, 12, 14]
        ```
    - flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。
        ```
        List<String> list = Arrays.asList("m,k,l,a", "1,3,5,7");
        List<String> listNew = list.stream().flatMap(s -> {
            // 将每个元素转换成一个stream
            String[] split = s.split(",");
            Stream<String> s2 = Arrays.stream(split);
            return s2;
        }).collect(Collectors.toList());

        System.out.println("处理前的集合：" + list);    // 处理前的集合：[m,k,l,a, 1,3,5,7]
        System.out.println("处理后的集合：" + listNew);    // 处理后的集合：[m, k, l, a, 1, 3, 5, 7]
        ```

- 归约(reduce)  
归约，也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和、求乘积和求最值操作。
    ```
    List<Integer> list = Arrays.asList(1, 3, 2, 8, 11, 4);
    // 求和方式1
    Optional<Integer> sum = list.stream().reduce((x, y) -> x + y);
    // 求和方式2
    Optional<Integer> sum2 = list.stream().reduce(Integer::sum);
    // 求和方式3
    Integer sum3 = list.stream().reduce(0, Integer::sum);
    // 求乘积
    Optional<Integer> product = list.stream().reduce((x, y) -> x * y);
    // 求最大值方式1
    Optional<Integer> max = list.stream().reduce((x, y) -> x > y ? x : y);
    // 求最大值写法2
    Integer max2 = list.stream().reduce(1, Integer::max);

    System.out.println("list求和：" + sum.get() + "," + sum2.get() + "," + sum3);    // list求和：29,29,29
    System.out.println("list求积：" + product.get());    // list求积：2112
    System.out.println("list求最大值：" + max.get() + "," + max2);    // list求最大值：11,11
    ```

- 收集(collect)  
collect，收集，可以说是内容最繁多、功能最丰富的部分了。从字面上去理解，就是把一个流收集起来，最终可以是收集成一个值也可以收集成一个新的集合。
    - 归集(toList/toSet/toMap)  
    因为流不存储数据，那么在流中的数据完成处理后，需要将流中的数据重新归集到新的集合里。toList、toSet和toMap比较常用，另外还有toCollection、toConcurrentMap等复杂一些的用法。
        ```
        List<Integer> list = Arrays.asList(1, 6, 3, 4, 6, 7, 9, 6, 20);
        List<Integer> listNew = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
        Set<Integer> set = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toSet());

        System.out.println("toList:" + listNew);    // toList:[6, 4, 6, 6, 20]
        System.out.println("toSet:" + set);    // toSet:[4, 20, 6]
        ```

    - 统计(count/averaging)  
    Collectors提供了一系列用于数据统计的静态方法：
        - 计数：count
        - 平均值：averagingInt、averagingLong、averagingDouble
        - 最值：maxBy、minBy
        - 求和：summingInt、summingLong、summingDouble
        - 统计以上所有：summarizingInt、summarizingLong、summarizingDouble
            ```
            List<Person> personList = new ArrayList<Person>();
            personList.add(new Person("Tom", 8900, 23, "male", "New York"));
            personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
            personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

            // 求总数
            Long count = personList.stream().collect(Collectors.counting());
            // 求平均工资
            Double average = personList.stream().collect(Collectors.averagingDouble(Person::getSalary));
            // 求最高工资
            Optional<Integer> max = personList.stream().map(Person::getSalary).collect(Collectors.maxBy(Integer::compare));
            // 求工资之和
            Integer sum = personList.stream().collect(Collectors.summingInt(Person::getSalary));
            // 一次性统计所有信息
            DoubleSummaryStatistics collect = personList.stream().collect(Collectors.summarizingDouble(Person::getSalary));

            System.out.println("员工总数：" + count);    // 员工总数：3
            System.out.println("员工平均工资：" + average);    // 员工平均工资：7900.0
            System.out.println("员工工资总和：" + sum);    // 员工工资总和：23700
            System.out.println("员工工资所有统计：" + collect);    // 员工工资所有统计：DoubleSummaryStatistics{count=3, sum=23700.000000, min=7000.000000, average=7900.000000, max=8900.000000}
            ```

    - 分组(partitioningBy/groupingBy)  
        - 分区：将stream按条件分为两个Map，比如员工按薪资是否高于8000分为两部分。
        - 分组：将集合分为多个Map，比如员工按性别分组。有单级分组和多级分组。
            ```
            List<Person> personList = new ArrayList<Person>();
            personList.add(new Person("Tom", 8900, "male", "New York"));
            personList.add(new Person("Jack", 7000, "male", "Washington"));
            personList.add(new Person("Lily", 7800, "female", "Washington"));
            personList.add(new Person("Anni", 8200, "female", "New York"));
            personList.add(new Person("Owen", 9500, "male", "New York"));
            personList.add(new Person("Alisa", 7900, "female", "New York"));

            // 将员工按薪资是否高于8000分组
            Map<Boolean, List<Person>> part = personList.stream().collect(Collectors.partitioningBy(x -> x.getSalary() > 8000));
            // 将员工按性别分组
            Map<String, List<Person>> group = personList.stream().collect(Collectors.groupingBy(Person::getSex));
            // 将员工先按性别分组，再按地区分组
            Map<String, Map<String, List<Person>>> group2 = personList.stream().collect(Collectors.groupingBy(Person::getSex, Collectors.groupingBy(Person::getArea)));
            System.out.println("员工按薪资是否大于8000分组情况：" + part);
            System.out.println("员工按性别分组情况：" + group);
            System.out.println("员工按性别、地区：" + group2);
            ```

    - 接合(joining)  
    joining可以将stream中的元素用特定的连接符（没有的话，则直接连接）连接成一个字符串。
        ```
        List<Person> personList = new ArrayList<Person>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

        String names = personList.stream().map(p -> p.getName()).collect(Collectors.joining(","));
        System.out.println("所有员工的姓名：" + names);    // 所有员工的姓名：Tom,Jack,Lily
        List<String> list = Arrays.asList("A", "B", "C");
        String string = list.stream().collect(Collectors.joining("-"));
        System.out.println("拼接后的字符串：" + string);    // 拼接后的字符串：A-B-C
        ```

    - 归约(reducing)  
    Collectors类提供的reducing方法，相比于stream本身的reduce方法，增加了对自定义归约的支持。
        ```
        List<Person> personList = new ArrayList<Person>();
        personList.add(new Person("Tom", 8900, 23, "male", "New York"));
        personList.add(new Person("Jack", 7000, 25, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 21, "female", "Washington"));

        // 每个员工减去起征点后的薪资之和（这个例子并不严谨，但一时没想到好的例子）
        Integer sum = personList.stream().collect(Collectors.reducing(0, Person::getSalary, (i, j) -> (i + j - 5000)));
        System.out.println("员工扣税薪资总和：" + sum);    // 员工扣税薪资总和：8700

        // stream的reduce
        Optional<Integer> sum2 = personList.stream().map(Person::getSalary).reduce(Integer::sum);
        System.out.println("员工薪资总和：" + sum2.get());    // 员工薪资总和：23700
        ```

    - 排序(sorted)  
    sorted，中间操作。有两种排序：
        - sorted()：自然排序，流中元素需实现Comparable接口
        - sorted(Comparator com)：Comparator排序器自定义排序
            ```
            List<Person> personList = new ArrayList<Person>();

            personList.add(new Person("Sherry", 9000, 24, "female", "New York"));
            personList.add(new Person("Tom", 8900, 22, "male", "Washington"));
            personList.add(new Person("Jack", 9000, 25, "male", "Washington"));
            personList.add(new Person("Lily", 8800, 26, "male", "New York"));
            personList.add(new Person("Alisa", 9000, 26, "female", "New York"));

            // 按工资升序排序（自然排序）
            List<String> newList = personList.stream().sorted(Comparator.comparing(Person::getSalary)).map(Person::getName)
                    .collect(Collectors.toList());
            // 按工资倒序排序
            List<String> newList2 = personList.stream().sorted(Comparator.comparing(Person::getSalary).reversed())
                    .map(Person::getName).collect(Collectors.toList());
            // 先按工资再按年龄升序排序
            List<String> newList3 = personList.stream()
                    .sorted(Comparator.comparing(Person::getSalary).thenComparing(Person::getAge)).map(Person::getName)
                    .collect(Collectors.toList());
            // 先按工资再按年龄自定义排序（降序）
            List<String> newList4 = personList.stream().sorted((p1, p2) -> {
                if (p1.getSalary() == p2.getSalary()) {
                    return p2.getAge() - p1.getAge();
                } else {
                    return p2.getSalary() - p1.getSalary();
                }
            }).map(Person::getName).collect(Collectors.toList());

            System.out.println("按工资升序排序：" + newList);    // 按工资升序排序：[Lily, Tom, Sherry, Jack, Alisa]
            System.out.println("按工资降序排序：" + newList2);    // 按工资降序排序：[Sherry, Jack, Alisa, Tom, Lily]
            System.out.println("先按工资再按年龄升序排序：" + newList3);    // 先按工资再按年龄升序排序：[Lily, Tom, Sherry, Jack, Alisa]
            System.out.println("先按工资再按年龄自定义降序排序：" + newList4);    // 先按工资再按年龄自定义降序排序：[Alisa, Jack, Sherry, Tom, Lily]
            ```

    - 提取/组合  
    流也可以进行合并、去重、限制、跳过等操作。
        ```
        String[] arr1 = { "a", "b", "c", "d" };
        String[] arr2 = { "d", "e", "f", "g" };

        Stream<String> stream1 = Stream.of(arr1);
        Stream<String> stream2 = Stream.of(arr2);
        // concat:合并两个流 distinct：去重
        List<String> newList = Stream.concat(stream1, stream2).distinct().collect(Collectors.toList());
        // limit：限制从流中获得前n个数据
        List<Integer> collect = Stream.iterate(1, x -> x + 2).limit(10).collect(Collectors.toList());
        // skip：跳过前n个数据
        List<Integer> collect2 = Stream.iterate(1, x -> x + 2).skip(1).limit(5).collect(Collectors.toList());

        System.out.println("流合并：" + newList);    // 流合并：[a, b, c, d, e, f, g]
        System.out.println("limit：" + collect);    // limit：[1, 3, 5, 7, 9, 11, 13, 15, 17, 19]
        System.out.println("skip：" + collect2);    // skip：[3, 5, 7, 9, 11]
        ```

    