package com.ozz.demo.httpclient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpclientDemo {
  /**
   * 上传附件
   */
  public void upload(String url, File file) throws IOException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      // 创建请求
      HttpPost httppost = new HttpPost(url);

      // 添加附件
      MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
      multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
      multipartEntityBuilder.addBinaryBody("file", file);
      httppost.setEntity(multipartEntityBuilder.build());

      // 发送
      HttpResponse response = httpclient.execute(httppost);

      // 解析响应值
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        System.out.println("服务器正常响应.....");
        HttpEntity resEntity = response.getEntity();
        System.out.println(EntityUtils.toString(resEntity));// httpclient自带的工具类读取返回数据
        System.out.println(resEntity.getContent());
        EntityUtils.consume(resEntity);
      } else {
        throw new RuntimeException(response.getStatusLine().getStatusCode() + ":"
                                   + response.getStatusLine().getReasonPhrase());
      }
    }
  }

  public static void main(String[] args) throws IOException {
    try {
      new HttpclientDemo().upload("http://jwapi.uat.staff.xdf.cn/import_excel?accessToken=5e83a0b0-d9a8-44c2-b2de-1158b8866ccc&appId=90101&businessType=2",
                                  new File("C:/Users/ouzezhou/Desktop/Temp/20161223/班级模板 (7).xlsx"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
