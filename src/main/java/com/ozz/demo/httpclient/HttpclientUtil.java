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
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpclientUtil {
  /**
   * set proxy
   */
  public static void setProxy(HttpRequestBase httpRequest) {
    HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
    httpRequest.setConfig(config);
  }

  /**
   * Get
   */
  public static String doGet(String uri) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      HttpGet httpRequest = new HttpGet(uri);

      return doRequest(httpclient, httpRequest);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Post
   */
  public static String doPost(String url, Map<String, String> params) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      HttpPost httpRequest = new HttpPost(url);
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      for (String name : params.keySet()) {
        nvps.add(new BasicNameValuePair(name, params.get(name)));
      }
      httpRequest.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

      return doRequest(httpclient, httpRequest);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Post
   */
  public static String doPost(String url, String body) {
    try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
      HttpPost httpRequest = new HttpPost(url);
      httpRequest.setEntity(new StringEntity(body, "UTF-8"));
      return doRequest(httpclient, httpRequest);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String doRequest(CloseableHttpClient httpclient, HttpRequestBase httpRequest) {
    try (CloseableHttpResponse response = httpclient.execute(httpRequest);) {
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        return parseResponse(response);
      } else {
        String mess = String.valueOf(response.getStatusLine().getStatusCode());
        Exception e1 = null;
        try {
          mess += "\n" + parseResponse(response);
        } catch (Exception e) {
          e1 = e;
        }
        RuntimeException e2 = new RuntimeException(mess);
        if (e1 != null) {
          e2.addSuppressed(e1);
        }
        throw e2;
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      httpRequest.abort();
    }
  }

  private static String parseResponse(CloseableHttpResponse response) throws IOException {
    HttpEntity entity = response.getEntity();
    String responseStr = EntityUtils.toString(entity);
    EntityUtils.consume(entity);
    return responseStr;
  }

  /**
   * 上传附件
   */
  public static void upload(String url, File file) {
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
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws IOException {
//      HttpclientDemo.upload("http://jwapi.dev.staff.xdf.cn:8080/import_excel?accessToken=5e83a0b0-d9a8-44c2-b2de-1158b8866ccc&appId=90101&businessType=2",
//                                  new File("C:/Users/ouzezhou/Desktop/Temp/20161223/班级模板 (7).xlsx"));
    System.out.println(HttpclientUtil.doGet("http://www.baidu.com"));
  }
}
