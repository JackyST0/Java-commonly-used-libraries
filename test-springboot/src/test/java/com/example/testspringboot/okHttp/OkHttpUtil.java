package com.example.testspringboot.okHttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/10/22 9:44
 */
@Slf4j
public class OkHttpUtil {

    private static String getUrl = "https://www.baidu.com/";
    private static String postUrl = "https://xagateway.suntekcorps.com:8443/api-1688/openApiProduct/queryProductDetail";

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        get(client);
        asyncGet(client);
        post(client);
        asyncPost(client);
    }

    /**
     * 同步get
     * @param client
     * @return void
     */
    private static void get(OkHttpClient client) {
        // 创建get请求对象
        Request request = new Request.Builder()
                .url(getUrl)
                .get()
                .header("Content-Type", "application/json")     // 设置Content-Type请求头
                .build();
        try {
            // 返回响应对象
            Response response = client.newCall(request).execute();
            // 验证响应是否成功
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // 获取响应数据
            String message = response.message();
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步get
     * @param client
     * @return void
     */
    public static void asyncGet(OkHttpClient client) {
        // 创建get请求对象
        Request request = new Request.Builder()
                .url(getUrl)
                .get()      // 不指定请求方式默认为get
                .build();
        // 异步发送请求，没有返回结果
        client.newCall(request).enqueue(new Callback() {

            // 处理成功请求
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // 验证响应是否成功
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // 获取响应数据
                String message = response.message();
                System.out.println(message);
            }

            // 处理失败请求
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.debug("Unexpected code" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public static void post(OkHttpClient client) {
//        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        MediaType mediaType = MediaType.parse(ContentType.APPLICATION_JSON.getMimeType());
        RequestBody requestBody = RequestBody.create("{\n" +
                "  \"country\": \"en\",\n" +
                "  \"offerId\": " + "773851424064" + "\n" +
                "}", mediaType);
        Request request = new Request.Builder()
                .url(postUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            // 验证响应是否成功
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // 获取响应数据
            String body = response.body().string();
            System.out.println(body);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void asyncPost(OkHttpClient client) {
        Request request = new Request.Builder()
                .url(postUrl)
                .post(RequestBody.create("{\n" +
                        "  \"country\": \"en\",\n" +
                        "  \"offerId\": " + "773851424064" + "\n" +
                        "}", MediaType.parse(ContentType.APPLICATION_JSON.getMimeType())))
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            // 请求成功
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // 验证响应是否成功
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // 获取响应数据
                String body = response.body().string();
                System.out.println(body);
            }

            // 请求异常
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.debug("Unexpected code " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
