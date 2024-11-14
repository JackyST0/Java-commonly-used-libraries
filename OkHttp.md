# OKHttp

### 简介
OkHttp是一个来自Square的HTTP客户端，用于Java和Android应用程序。它的设计是为了更快地加载资源并节省带宽。OkHttp在开源项目中被广泛使用，是Retrofit、Picasso等库的骨干。

### 优势
- 支持HTTP/2（有效使用套接字）
- 连接池（在没有HTTP/2的情况下减少请求延迟）
- GZIP压缩（缩小下载大小）
- 响应缓存（避免了重新获取相同的数据）
- 从常见的连接问题中无声恢复
- 替代IP地址检测（在IPv4和IPv6环境下）
- 支持现代TLS功能（TLS 1.3，ALPN，证书钉子）。
- 支持同步和异步调用

### 使用
- #### 获取 OkHttpClient 对象  
    在使用 OkHttp 发送 HTTP 请求时，首先需要获取一个 OkHttpClient 对象，获取 OkHttpClient 对象的方式很简单，在 OkHttp 中大量使用了 Builder 模式。获取 OkHttpClient 对象的方法如下所示：
    - #### 获取默认配置对象
        ```
        // 获取默认配置 的OkHttpClient 对象
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        ```
    - #### 获取自定配置对象
        在获取对象时可以指定，连接超时、读写超时、拦截器等配置，如下所示：
        ```
        OkHttpClient httpClient = new OkHttpClient.Builder()
        // 设置连接超时时间
        .connectTimeout(Duration.ofSeconds(30))
        // 设置读超时时间
        .readTimeout(Duration.ofSeconds(60))
        // 设置写超时时间
        .writeTimeout(Duration.ofSeconds(60))
        // 设置完整请求超时时间
        .callTimeout(Duration.ofSeconds(120))
        // 添加一个拦截器
        .addInterceptor(chain -> {
            Request request = chain.request();
            return chain.proceed(request);
        })
        // 注册事件监听器
        .eventListener(new EventListener() {
            @Override
            public void callEnd(@NotNull Call call) {
                LOGGER.info("----------callEnd--------");
                super.callEnd(call);
            }
        })
        .build();
        ```
- #### GET 请求
    GET 请求的执行步骤如下：
    - #### 构造 Request 对象
        ```
        // 构造一个 Request 对象
        Request request = new Request.Builder()
        // 标识为 GET 请求
        .get()
        // 设置请求路径
        .url("http://localhost:10010/user/map?name=zhangsan&a=a&age=20")
        // 添加头信息
        .addHeader("Content-Type", "text/plain")
        .build();
        ```
    - #### 将 Request 封装为 Call
        ```
        // 通过 HttpClient 把 Request 构造为 Call 对象
        Call call = httpClient.newCall(request);
        ```
    - #### 执行请求 (同步或异步)
        - #### 同步请求
            ```
            // 执行同步请求
            Response response = call.execute();
            ```
        - #### 异步请求
            ```
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LOGGER.error("请求 {} 出现异常 {}", call.request().url(), e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().toString();
                    LOGGER.info("请求 {} 的响应结果为 {}", call.request().url(), body);
                }
            });
            ```
- #### POST 请求
    POST 请求的构建步骤与GET 相似，区别是，在构建 Request 对象时 在post() 方法中，设置需要发送的数据，发送的数据可为目前流行的 JSON 格式数据，也可以模拟 Form表单提交的数据，操作如下所示：
    - #### 构建Form表单数据
        ```
        // 构造 Form 表单对象
        FormBody formBody = new FormBody.Builder()
            .addEncoded("name", "张三")
            .add("age", "20")
            .add("a", "ag")
            .build();
        ```
    - #### 构造 JSON 数据
        ```
        // 创建 JSON 对象
        JSONObject json = new JSONObject();
        json.put("name", "张三");
        json.put("age", 20);

        // 构造 Content-Type 头
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");

        // 构造请求数据
        RequestBody requestBody = RequestBody.create(json.toJSONString(), mediaType);
        ```
    - #### 构造Request对象
        ```
        // 构建 Request 对象
        Request request = new Request.Builder()
            // post 方法中传入 构造的对象
            .post(formBody)
            .url("http://localhost:10010/user/obj")
            .build();
        ```
    构建完Request对象后的步骤，就与GET 请求相似，构建 Call 对象然后，再发送同步或异步请求。
- #### 上传
    在OkHttp中 进行文件的上传，是相当简单的，在发送 POST 请求时，只需要构造一个MultipartBody 对象即可，MultipartBody 对象可以发送 文件数据，也可以发送基本类型的数据。
    ```
     File file = new File("F:/20150703212056_Yxi4L.png");

    // 使用 MultipartBody 构造 Request 对象
    RequestBody multipartBody = new MultipartBody.Builder()
        //一定要设置这句
        .setType(MultipartBody.FORM)
        .addFormDataPart("name", "admin")//
        .addFormDataPart("password", "admin")//
        // 添加上传文件
        .addFormDataPart("file", "20150703212056_Yxi4L.png",
                        RequestBody.create( file, MediaType.parse("image/png")))
        .build();

    // 构造 Request 对象
    Request request = new Request.Builder()
        .post(multipartBody)
        .url("http://localhost:10010/user/upload")
        .build();

    // 构造 Call 对象，并发送 同步请求
    Call call = httpClient.newCall(request);
    Response response = call.execute();
    ```

### 总结
OkHttp 作为网络请求工具，简单且功能强大，并且其大量使用了 构建者模式（构建者模式的主要优点是可以将一个复杂对象的构造过程封装起来，使得创建和表示分离，提高了系统的模块化）。

    