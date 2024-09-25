# Fastjson 2.0
FASTJSON v2是FASTJSON项目的重要升级，目标是为下一个十年提供一个高性能的JSON库。

### 简单使用
- 将JSON解析为JSONObject
    ```
    String text = "...";
    JSONObject data = JSON.parseObject(text);

    byte[] bytes = ...;
    JSONObject data = JSON.parseObject(bytes);
    ```
    
- 将JSON解析为JSONArray
    ```
    String text = "...";
    JSONArray data = JSON.parseArray(text);
    ```

- 将JSON解析为Java对象
    ```
    String text = "...";
    User data = JSON.parseObject(text, User.class);
    ```

- 将Java对象序列化为JSON
    ```
    Object data = "...";
    String text = JSON.toJSONString(data);
    byte[] text = JSON.toJSONBytes(data);
    ```

- 获取简单属性
    ```
    String text = "{\"id\": 2,\"name\": \"fastjson2\"}";
    JSONObject obj = JSON.parseObject(text);
    int id = obj.getIntValue("id");
    String name = obj.getString("name");

    String text = "[2, \"fastjson2\"]";
    JSONArray array = JSON.parseArray(text);
    int id = array.getIntValue(0);
    String name = array.getString(1);
    ```

- 读取JavaBean
    ```
    JSONArray array = ...
    JSONObject obj = ...

    User user = array.getObject(0, User.class);
    User user = obj.getObject("key", User.class);
    ```

- 转为JavaBean
    ```
    JSONArray array = ...
    JSONObject obj = ...

    User user = obj.toJavaObject(User.class);
    List<User> users = array.toJavaList(User.class);
    ```

- 将JavaBean对象序列化为JSON
    ```
    class User {
    public int id;
    public String name;
    }

    User user = new User();
    user.id = 2;
    user.name = "FastJson2";

    String text = JSON.toJSONString(user);
    byte[] bytes = JSON.toJSONBytes(user);
    ```

### 常用方法
Fastjson2库是一个用于处理JSON数据的Java库，它提供了许多方法来解析、生成、转换和查询JSON数据。这里是一些常用的Fastjson2方法：
- JSON.parseObject(String text): 用于将JSON字符串转换为JSONObject或JavaBean。
- JSON.parseArray(String text): 用于将JSON字符串转换为JSONArray。
- JSON.toJSONString(Object object): 将JavaBean或其他对象转换为JSON字符串。
- JSONObject.getString(String key): 获取JSONObject中对应键的字符串值。
- JSONObject.getInteger(String key): 获取JSONObject中对应键的整数值。
- JSONObject.getBoolean(String key): 获取JSONObject中对应键的布尔值。
- JSONObject.getJSONArray(String key): 获取JSONObject中对应键的JSONArray。
- JSONObject.getJSONObject(String key): 获取JSONObject中对应键的JSONObject。
- JSONArray.getJSONObject(int index): 获取JSONArray中指定索引的JSONObject。
- JSONArray.getJSONArray(int index): 获取JSONArray中指定索引的JSONArray。
- JSONArray.add(Object element): 向JSONArray添加元素。
- JSONObject.put(String key, Object value): 向JSONObject添加键值对。

