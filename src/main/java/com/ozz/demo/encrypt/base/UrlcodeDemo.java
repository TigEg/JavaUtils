package com.ozz.demo.encrypt.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL编码
 */
public abstract class UrlcodeDemo {
  public static String encode(String s) throws UnsupportedEncodingException {
    return URLEncoder.encode(s, "UTF-8");
  }

  public static String decode(String s) throws UnsupportedEncodingException {
    return URLDecoder.decode(s, "UTF-8");
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    String src = "你好 World!";
    System.out.println("原文：" + src);

    String encode = encode(src);
    System.out.println("加密：" + encode);

    String decode = decode(encode);
    System.out.println("解密：" + decode);
  }
}
