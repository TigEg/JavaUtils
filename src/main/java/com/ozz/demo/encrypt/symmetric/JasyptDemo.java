package com.ozz.demo.encrypt.symmetric;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * 默认算法：StandardPBEByteEncryptor.DEFAULT_ALGORITHM="PBEWithMD5AndDES"
 */
public class JasyptDemo {
  public String encrypt(String password, String message) {
    PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setPassword(password);
    return encryptor.encrypt(message);
  }

  public String decrypt(String password, String encryptedMessage) {
    PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setPassword(password);
    return encryptor.decrypt(encryptedMessage);
  }

//  public static void main(String[] args) throws Exception {
//    JasyptDemo demo = new JasyptDemo();
//
//    String source = "站在云端，敲下键盘，望着通往世界另一头的那扇窗，只为做那读懂0和1的人。。";
//    System.out.println("原文：" + source);
//
//    String key = "pdg6jd6rsofdsjfo145";
//    System.out.println("密钥：" + key);
//
//    String encryptData = demo.encrypt(key, source);
//    System.out.println("加密：" + encryptData);
//
//    String decryptData = demo.decrypt(key, encryptData);
//    System.out.println("解密: " + decryptData);
//
//    if (!source.equals(decryptData)) {
//      throw new RuntimeException("解密失败...");
//    }
//  }
}
