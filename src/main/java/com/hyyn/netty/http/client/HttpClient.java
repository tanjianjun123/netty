package com.hyyn.netty.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpClient {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url="http://localhost:9999";
		HttpPost post = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000).setSocketTimeout(10000).build();
		post.setConfig(requestConfig);
		StringEntity entity = new StringEntity("nihao", "utf-8");
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
		System.out.println(result);
	}
}
