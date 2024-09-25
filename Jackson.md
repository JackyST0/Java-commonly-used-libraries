# Jackson

### 描述
Jackson 是一个非常流行且高效的基于 java 的库，用于将 java 对象序列化或映射到 JSON，反之亦然。

### 三种处理 JSON 的方法
Jackson 提供了三种处理 JSON 的替代方法：
- 流式处理 API -- 将 JSON 内容作为离散事件读取和写入。JsonParser 读取数据，而 Json生成器写入数据。它是三者中最强大的方法，开销最低，读/写操作速度最快。它类似于用于 XML 的斯塔克斯解析器。
- 树模型 -- 准备 JSON 文档的内存中树表示形式。对象映射器生成 JsonNode 节点的树。这是最灵活的方法。它类似于 XML 的 DOM 解析器。
- 数据绑定 -- 使用属性访问器或使用注释将 JSON 与 POJO（普通旧 Java 对象）相互转换。它有两种类型。
    - 简单数据绑定 -- 将 JSON 与 Java 映射、列表、字符串、数字、布尔值和空对象相互转换。
    - 完整数据绑定 -- 将 JSON 与任何 JAVA 类型相互转换。

### ObjectMapper 类
Jackson库中的ObjectMapper类是最常用的类，它提供了许多用于处理JSON的方法。以下是一些常用的方法：
- writeValueAsString(Object obj): 将Java对象转换为JSON字符串。
    ```
    String json = objectMapper.writeValueAsString(object);
    ```

- readValue(String content, Class<T> valueType): 将JSON字符串转换为Java对象。
    ```
    MyObject obj = objectMapper.readValue(json, MyObject.class);
    ```

- writeValue(File resultFile, Object value): 将Java对象写入到文件中。
    ```
    objectMapper.writeValue(new File("output.json"), object);
    ```

- readValue(File src, Class<T> valueType): 从文件中读取JSON数据，并转换为Java对象。
    ```
    MyObject obj = objectMapper.readValue(new File("input.json"), MyObject.class);
    ```

- writeValue(OutputStream out, Object value): 将Java对象写入到输出流中。
    ```
    objectMapper.writeValue(System.out, object);
    ```

- readValue(InputStream src, Class<T> valueType): 从输入流中读取JSON数据，并转换为Java对象。
    ```
    MyObject obj = objectMapper.readValue(System.in, MyObject.class);
    ```

- convertValue(Object fromValue, Class<T> toValueType): 将一个Java对象转换为另一个Java对象。
    ```
    MyObject2 obj2 = objectMapper.convertValue(obj1, MyObject2.class);
    ```

- readTree(String content): 将JSON字符串转换为JsonNode对象，可以用于处理复杂的JSON结构。
    ```
    JsonNode node = objectMapper.readTree(json);
    ```

### JsonNode 类
JsonNode 是 Jackson 库中表示 JSON 对象的类，以下是一些常用的方法：
- get(String fieldName): 根据字段名获取字段的值。
- path(String fieldName): 根据字段名获取字段的值，如果字段不存在，返回一个 MissingNode，而不是 null。
- findValue(String fieldName): 递归搜索所有子节点，找到第一个匹配字段名的节点。
- findPath(String fieldName): 递归搜索所有子节点，找到所有匹配字段名的节点。
- findValues(String fieldName): 递归搜索所有子节点，找到所有匹配字段名的节点，并返回一个 List。
- isArray(): 检查此节点是否为一个 JSON 数组。
- isObject(): 检查此节点是否为一个 JSON 对象。
- isValueNode(): 检查此节点是否表示一个 JSON 值（字符串、数字、布尔值或 null）。
- asText(): 将此节点的值转换为字符串。
- asInt(), asLong(), asDouble(), asBoolean(): 将此节点的值转换为相应的基本类型。
- size(): 如果此节点是一个数组或对象，返回其元素或字段的数量。
- elements(): 如果此节点是一个数组或对象，返回其元素或字段的迭代器。
- fields(): 如果此节点是一个对象，返回其字段的迭代器。

### 注解
在 Jackson 库中，有很多注解可以帮助我们更好地处理 JSON 数据，以下是一些常用的注解：
- @JsonProperty: 用于指定 JSON 属性的名称。
- @JsonIgnore: 标记一个属性或方法不被序列化或反序列化。
- @JsonInclude: 用于指定当序列化时哪些属性应该被包含。例如，你可以指定仅序列化那些非空的属性。
- @JsonFormat: 用于指定日期/时间的格式。
- @JsonTypeInfo, @JsonSubTypes, @JsonTypeName: 用于处理多态类型的序列化和反序列化。
- @JsonView: 用于定义在序列化时哪些属性应该被包含，基于不同的视图。
- @JsonCreator: 用于指定用于反序列化的构造函数或工厂方法。
- @JsonSetter: 用于指定用于反序列化的 setter 方法。
- @JsonValue: 用于指定用于序列化的 getter 方法。
- @JsonAnyGetter and @JsonAnySetter: 用于处理动态属性。
- @JsonDeserialize and @JsonSerialize: 用于指定自定义的反序列化和序列化类。