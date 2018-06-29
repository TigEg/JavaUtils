package com.ozz.demo.security.encrypt.asymmetric;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class RSACoderTest {
  private String publicKey;
  private String privateKey;

  public static void main(String[] args) {
    RSACoderTest test = new RSACoderTest();
    test.setUp();

    test.testEncryptByPublicKey();
    String encodedData = test.testEncryptByPrivateKey();
    test.testSign(encodedData);
  }

  public void setUp() {
    Map<String, Object> keyMap = RSACoder.initKey();

    publicKey = RSACoder.getPublicKey(keyMap);
    privateKey = RSACoder.getPrivateKey(keyMap);
    System.out.println("公钥: " + publicKey);
    System.out.println("私钥： " + privateKey);
  }

  public void testEncryptByPublicKey() {
    System.out.println("--------\n公钥加密——私钥解密");
    String inputStr = "abc";

    String encodedData = RSACoder.encryptByPublicKey(inputStr, publicKey);
    System.out.println("密文: " + new String(encodedData));

    String outputStr = RSACoder.decryptByPrivateKey(encodedData, privateKey);

    System.out.println("加密前: " + inputStr + "\n" + "解密后: " + outputStr);

    if(!StringUtils.equals(inputStr, outputStr)) {
      throw new RuntimeException("解密数据错误");
    }
  }

  public String testEncryptByPrivateKey() {
    System.out.println("--------\n私钥加密——公钥解密");
    String inputStr = "def";

    String encodedData = RSACoder.encryptByPrivateKey(inputStr, privateKey);
    System.out.println("密文: " + new String(encodedData));

    String outputStr = RSACoder.decryptByPublicKey(encodedData, publicKey);

    System.out.println("加密前: " + inputStr + "\n" + "解密后: " + outputStr);

    if(!StringUtils.equals(inputStr, outputStr)) {
      throw new RuntimeException("解密数据错误");
    }
    return encodedData;
  }

  public void testSign(String encodedData) {
    System.out.println("--------\n私钥签名——公钥验证签名");
    // 产生签名
    String sign = RSACoder.sign(encodedData.getBytes(), privateKey);
    System.out.println("签名:\r" + sign);

    // 验证签名
    boolean status = RSACoder.verify(encodedData.getBytes(), publicKey, sign);
    System.out.println("状态:\r" + status);

    if(!status) {
      throw new RuntimeException("验证签名错误");
    }
  }

}
