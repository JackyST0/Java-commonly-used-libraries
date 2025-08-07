package com.example.testspringboot.httpClient;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2024/10/22 16:22
 */
@Slf4j
public class HttpClientUtil {

    private static String getUrl = "https://www.baidu.com/";
    private static String postUrl = "https://xagateway.suntekcorps.com:8443/api-1688/openApiProduct/queryProductDetail";

    public static void main(String[] args) {
        try {
            // 创建客户端对象
            CloseableHttpClient client = HttpClients.createDefault();
            get(client);
            post(client);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get(CloseableHttpClient client) {
        try {
            // httpGet不能携带参数，如果需要参数只能通过拼接
            HttpGet httpGet = new HttpGet(getUrl);
            CloseableHttpResponse response = client.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void post(CloseableHttpClient client) {
        try {
            HttpPost httpPost = new HttpPost(postUrl);
            // 设置header信息
            httpPost.setHeader("Content-Type", "application/json");
            // 构建json参数
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("country", "en");
            jsonParam.put("offerId", "773851424064");
            // 创建StringEntity对象，用于包含参数信息
            StringEntity entity = new StringEntity(jsonParam.toString(), "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            // 将参数信息添加到method对象中
            httpPost.setEntity(entity);
            // 执行post请求，并获取返回信息
            CloseableHttpResponse response = client.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
