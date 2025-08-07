package com.example.testspringboot.util;

import com.example.testspringboot.exception.HttpConnectException;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/14 13:28
 */
public class HttpUtil {
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    public static void main(String[] args) throws Exception {
        String get = sendGet("https://www.baidu.com/");
        System.out.println(get);

        String post = sendPost("https://www.baidu.com/");
        System.out.println(post);
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendGet(String url) throws Exception {

        SSLContext sslcontext = createIgnoreVerifySSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager).build();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                ContentType contentType = ContentType.getOrDefault(entity);
                Charset charset = contentType.getCharset();
                if (charset == null) {
                    charset = StandardCharsets.UTF_8;       // 默认使用 UTF-8
                }
                result = EntityUtils.toString(entity, charset);
            }
        } catch (IOException e) {
            if (e.getMessage().contains("401")) {
                throw new HttpConnectException.UnauthorizedException(url);
            }
            if (e.getMessage().contains("403")) {
                throw new HttpConnectException.ForbiddenException(url);
            }
            if (e.getMessage().contains("405")) {
                throw new HttpConnectException.NotAllowedException(url);
            }
            if (e.getMessage().contains("501")) {
                throw new HttpConnectException.UnknownRequestException(url);
            }
            throw e;
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.close();
        }
        return result;
    }

    /**
     * 发送HttpPost请求，参数为map
     *
     * @param url
     * @param map
     * @return
     */
    public static String sendPost(String url, Map<String, String> map) throws Exception {
        SSLContext sslcontext = createIgnoreVerifySSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager).build();
        List<NameValuePair> formparams = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity1 = response.getEntity();
        String result = null;
        try {
            ContentType contentType = ContentType.getOrDefault(entity1);
            Charset charset = contentType.getCharset();
            if (charset == null) {
                charset = StandardCharsets.UTF_8;       // 默认使用 UTF-8
            }
            result = EntityUtils.toString(entity1, charset);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 发送HttpPost请求，参数为json
     *
     * @param url
     * @param json
     * @return
     */
    public static String sendPost(String url, String json) throws Exception {
        SSLContext sslcontext = createIgnoreVerifySSL();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager).build();
        StringEntity se = new StringEntity(json, "utf-8");
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(se);
        httppost.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            if (charset == null) {
                charset = StandardCharsets.UTF_8;       // 默认使用 UTF-8
            }
            result = EntityUtils.toString(entity, charset);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送不带参数的HttpPost请求
     *
     * @param url
     * @return
     */
    public static String sendPost(String url) {
        HttpPost httppost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            if (charset == null) {
                charset = StandardCharsets.UTF_8;       // 默认使用 UTF-8
            }
            result = EntityUtils.toString(entity, charset);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLSv1.2");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, new java.security.SecureRandom());
        return sc;
    }
}
