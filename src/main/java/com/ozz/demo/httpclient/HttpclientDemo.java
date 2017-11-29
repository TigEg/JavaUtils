package com.ozz.demo.httpclient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpclientDemo {
  /**
   * set proxy
   */
  public void setProxy(HttpRequestBase httpRequest) {
    HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
    httpRequest.setConfig(config);
  }

  /**
   * Get
   */
  public String doGet(String uri) throws IOException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      HttpGet httpRequest = new HttpGet(uri);
      try (CloseableHttpResponse response = httpclient.execute(httpRequest);) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          HttpEntity entity = response.getEntity();
          String responseStr = EntityUtils.toString(entity);
          EntityUtils.consume(entity);
          return responseStr;
        } else {
          throw new RuntimeException(response.getStatusLine().getStatusCode() + ":"
                                     + response.getStatusLine().getReasonPhrase());
        }
      } finally {
        httpRequest.abort();
      }
    }
  }

  /**
   * Post
   */
  public String doPost(String url, Map<String, String> params) throws IOException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      HttpPost httpRequest = new HttpPost(url);
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      for (String name : params.keySet()) {
        nvps.add(new BasicNameValuePair(name, params.get(name)));
      }
      httpRequest.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
      try (CloseableHttpResponse response = httpclient.execute(httpRequest);) {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
          HttpEntity entity = response.getEntity();
          String responseStr = EntityUtils.toString(entity);
          EntityUtils.consume(entity);
          return responseStr;
        } else {
          throw new RuntimeException(response.getStatusLine().getStatusCode() + ":"
                                     + response.getStatusLine().getReasonPhrase());
        }
      } finally {
        httpRequest.abort();
      }
    }
  }

  /**
   * 上传附件
   */
  public void upload(String url, File file) throws IOException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      // 创建请求
      HttpPost httpRequest = new HttpPost(url);

      // 添加附件
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
      multipartEntityBuilder.addBinaryBody("file", file);
      // multipartEntityBuilder.addBinaryBody("file2", file2);
      httpRequest.setEntity(multipartEntityBuilder.build());

      // 发送
      try(CloseableHttpResponse response = httpclient.execute(httpRequest);) {
        // 解析响应值
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        } else {
          throw new RuntimeException(response.getStatusLine().getStatusCode() + ":"
                                     + response.getStatusLine().getReasonPhrase());
        }
      } finally {
        httpRequest.abort();
      }
    }
  }

  public static void main(String[] args) throws IOException {
//      new HttpclientDemo().upload("http://jwapi.dev.staff.xdf.cn:8080/import_excel?accessToken=5e83a0b0-d9a8-44c2-b2de-1158b8866ccc&appId=90101&businessType=2",
//                                  new File("C:/Users/ouzezhou/Desktop/Temp/20161223/班级模板 (7).xlsx"));
    System.out.println(new HttpclientDemo().doGet("http://www.baidu.com"));
  }
}
