package com.ozz.demo.encrypt.asymmetric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class RSACoderTest {
  private static String publicKey;
  private static String privateKey;

  @BeforeClass
  public static void setUp() throws Exception {
    Map<String, Object> keyMap = RSACoder.initKey();

    publicKey = RSACoder.getPublicKey(keyMap);
    privateKey = RSACoder.getPrivateKey(keyMap);
    System.out.println("公钥: " + publicKey);
    System.out.println("私钥： " + privateKey);
  }

  @Test
  public void test() throws Exception {
    System.out.println("\n公钥加密——私钥解密");
    String inputStr = "abc";

    String encodedData = RSACoder.encryptByPublicKey(inputStr, publicKey);
    System.out.println("密文: " + new String(encodedData));

    String outputStr = RSACoder.decryptByPrivateKey(encodedData, privateKey);

    System.out.println("加密前: " + inputStr + "\n" + "解密后: " + outputStr);
    assertEquals(inputStr, outputStr);
  }

  @Test
  public void testSign() throws Exception {
    System.out.println("\n私钥加密——公钥解密");
    String inputStr = "sign";

    String encodedData = RSACoder.encryptByPrivateKey(inputStr, privateKey);

    String outputStr = RSACoder.decryptByPublicKey(encodedData, publicKey);

    System.out.println("加密前: " + inputStr + "\n" + "解密后: " + outputStr);
    assertEquals(inputStr, outputStr);

    System.out.println("\n私钥签名——公钥验证签名");
    // 产生签名
    String sign = RSACoder.sign(encodedData.getBytes(), privateKey);
    System.out.println("签名:\r" + sign);

    // 验证签名
    boolean status = RSACoder.verify(encodedData.getBytes(), publicKey, sign);
    System.out.println("状态:\r" + status);
    assertTrue(status);
  }

}
