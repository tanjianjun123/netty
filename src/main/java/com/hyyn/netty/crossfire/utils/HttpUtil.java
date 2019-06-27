package com.hyyn.netty.crossfire.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: tanjianjun
 * @Date: 2019/5/30 13:42
 * @Version 1.0
 */
public class HttpUtil {
    public static HttpClient client;

    static {
        client = HttpClientBuilder.create().build();

    }

    private static String ENCODE = "UTF-8";


    public static String post(String url, Map<String, String> map)
            throws Exception {
        // 处理请求地址
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);

        // 添加参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String str : map.keySet()) {
            params.add(new BasicNameValuePair(str, map.get(str)));
        }
        post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
        // 执行请求
        HttpResponse response = client.execute(post);

        if (response.getStatusLine().getStatusCode() == 200) {
            // 处理请求结果
            StringBuffer buffer = new StringBuffer();
            InputStream in = null;
            try {
                in = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            } finally {
                // 关闭流
                if (in != null) {
                    in.close();
                }
            }

            return buffer.toString();
        } else {
            return null;
        }

    }

    public static String post(String url, String str)
            throws Exception {
        // 处理请求地址
        URI uri = new URI(url);
        HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(str, "utf-8"));
        // 执行请求
        HttpResponse response = client.execute(post);

        if (response.getStatusLine().getStatusCode() == 200) {
            // 处理请求结果
            StringBuffer buffer = new StringBuffer();
            InputStream in = null;
            try {
                in = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

            } finally {
                // 关闭流
                if (in != null) {
                    in.close();
                }
            }

            return buffer.toString();
        } else {
            return null;
        }

    }

    public static String postJson(String url, String str) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000).setSocketTimeout(10000).build();
        post.setConfig(requestConfig);
        StringEntity entity = new StringEntity(str, "utf-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String result = null;
        if (response.getStatusLine().getStatusCode() == 200) {
            StringBuilder buffer = new StringBuilder();
            try (InputStream in = response.getEntity().getContent()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }
            result = buffer.toString();
        }
        client.close();
        return result;
    }

}
