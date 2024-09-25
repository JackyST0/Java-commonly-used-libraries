# Hutool

### 简介
Hutool是一个功能丰富且易用的Java工具库，通过诸多实用工具类的使用，旨在帮助开发者快速、便捷地完成各类开发任务。 这些封装的工具涵盖了字符串、数字、集合、编码、日期、文件、IO、加密、数据库JDBC、JSON、HTTP客户端等一系列操作， 可以满足各种不同的开发需求。

### 常用工具
Hutool是一个Java工具包，它可以简化每一行代码，提高开发效率，减少项目中的代码量。以下是一些Hutool的常用方法：
- 日期时间工具 - DateUtil：可以用来解析和格式化日期或时间，比如DateUtil.date(), DateUtil.parse(), DateUtil.format(), DateUtil.between()等。
    ```
    Date date = DateUtil.date(); //获取当前日期
    String dateStr = DateUtil.formatDateTime(date); //日期格式化为字符串
    Date date2 = DateUtil.parse(dateStr); //字符串解析为日期
    ```

- 文件操作工具 - FileUtil：提供了一些文件操作的方法，比如FileUtil.file(), FileUtil.mkdir(), FileUtil.copy(), FileUtil.del(), FileUtil.rename()等。
    ```
    File file = FileUtil.file("d:/test.txt"); //创建文件对象
    FileUtil.appendUtf8String("Hello World", file); //在文件末尾追加内容
    String content = FileUtil.readUtf8String(file); //读取文件内容
    ```

- IO工具 - IoUtil：提供了一些IO操作的方法，比如IoUtil.read(), IoUtil.write(), IoUtil.copy(), IoUtil.close()等。
    ```
    InputStream in = FileUtil.getInputStream(file); //获取文件输入流
    OutputStream out = FileUtil.getOutputStream("d:/test2.txt"); //获取文件输出流
    IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE); //复制文件
    ```

- 字符串工具 - StrUtil：提供了一些字符串操作的方法，比如StrUtil.format(), StrUtil.isEmpty(), StrUtil.isNotEmpty(), StrUtil.split(), StrUtil.join()等。
    ```
    String template = "{} love {}";
    String str = StrUtil.format(template, "I", "You"); //结果："I love You"
    ```

- 集合工具 - CollUtil：提供了一些集合操作的方法，比如CollUtil.newArrayList(), CollUtil.newHashSet(), CollUtil.sort(), CollUtil.contains()等。
    ```
    List<String> list = CollUtil.newArrayList("a", "b", "c"); //创建集合
    boolean contains = CollUtil.contains(list, "a"); //判断集合是否包含元素
    ```

- 加密解密工具 - SecureUtil：提供了一些加密和解密的方法，比如SecureUtil.md5(), SecureUtil.sha1(), SecureUtil.sha256(), SecureUtil.aes(), SecureUtil.des()等。
    ```
    String str = "test string";
    String md5Str = SecureUtil.md5(str); //计算MD5
    ```

- Http工具 - HttpUtil：提供了一些HTTP操作的方法，比如HttpUtil.get(), HttpUtil.post(), HttpUtil.downloadFile(), HttpUtil.uploadFile()等。
    ```
    String content = HttpUtil.get("https://www.google.com"); //发送get请求
    String postResult = HttpUtil.post("https://www.google.com", "param1=value1&param2=value2"); //发送post请求
    ```

- Json工具 - JSONUtil：提供了一些Json操作的方法，比如JSONUtil.parse(), JSONUtil.toJsonStr(), JSONUtil.parseArray(), JSONUtil.parseObj()等。
    ```
    String jsonStr = JSONUtil.toJsonStr(list); //集合转换为Json字符串
    List<String> list2 = JSONUtil.toList(JSONUtil.parseArray(jsonStr), String.class); //Json字符串解析为集合
    ```

### 数据库（Hutool-db）
Hutool-db是一个在JDBC基础上封装的数据库操作工具类，通过包装，使用ActiveRecord思想操作数据库。在Hutool-db中，使用Entity（本质上是个Map）代替Bean来使数据库操作更加灵活，同时提供Bean和Entity的转换来提供传统ORM的兼容支持。
- 增删改查
    - 增
        ```
        Db.use().insert(
            Entity.create("user")
            .set("name", "unitTestUser")
            .set("age", 66)
        );
        ```
    - 删
        ```
        Db.use().del(
            Entity.create("user").set("name", "unitTestUser")//where条件
        );
        ```
    - 改
        ```
        Db.use().update(
            Entity.create().set("age", 88), //修改的数据
            Entity.create("user").set("name", "unitTestUser") //where条件
        );
        ```
    - 查
        - 查询全部字段
            ```
            //user为表名
            Db.use().findAll("user");
            ```
        - 条件查询
            ```
            Db.use().findAll(Entity.create("user").set("name", "unitTestUser"));
            ```
        - 模糊查询
            ```
            Db.use().findLike("user", "name", "Test", LikeType.Contains);
            或者
            List<Entity> find = Db.use().find(Entity.create("user").set("name", "like 王%"));
            ```
        - 分页查询
            ```
            //Page对象通过传入页码和每页条目数达到分页目的
            PageResult<Entity> result = Db.use().page(Entity.create("user").set("age", "> 30"), new Page(10, 20));
            ```
        - 执行SQL语句
            ```
            //查询
            List<Entity> result = Db.use().query("select * from user where age < ?", 3);
            //模糊查询
            List<Entity> result = Db.use().query("select * from user where name like ?", "王%");
            //新增
            Db.use().execute("insert into user values (?, ?, ?)", "张三", 17, 1);
            //删除
            Db.use().execute("delete from user where name = ?", "张三");
            //更新
            Db.use().execute("update user set age = ? where name = ?", 3, "张三");
            ```
        - 事务
            ```
            Db.use().tx(new TxFunc() {
                @Override
                public void call(Db db) throws SQLException {
                    db.insert(Entity.create("user").set("name", "unitTestUser"));
                    db.update(Entity.create().set("age", 79), Entity.create("user").set("name", "unitTestUser"));
                }
            });

            JDK8中可以用lambda表达式（since：5.x）：
            Db.use().tx(db -> {
                db.insert(Entity.create("user").set("name", "unitTestUser2"));
                db.update(Entity.create().set("age", 79), Entity.create("user").set("name", "unitTestUser2"));
            });
            ```