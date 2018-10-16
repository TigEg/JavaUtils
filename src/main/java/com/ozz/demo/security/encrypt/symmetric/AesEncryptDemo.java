package com.ozz.demo.security.encrypt.symmetric;

import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES对称加密算法
 * 
 * @see ===========================================================================================================
 * @see 这里演示的是其Java6.0的实现,理所当然的BouncyCastle也支持AES对称加密算法
 * @see 另外,我们也可以以AES算法实现为参考,完成RC2,RC4和Blowfish算法的实现
 * @see ===========================================================================================================
 * @see 由于DES的不安全性以及DESede算法的低效,于是催生了AES算法(Advanced Encryption Standard)
 * @see 该算法比DES要快,安全性高,密钥建立时间短,灵敏性好,内存需求低,在各个领域应用广泛
 * @see 目前,AES算法通常用于移动通信系统以及一些软件的安全外壳,还有一些无线路由器中也是用AES算法构建加密协议
 * @see ===========================================================================================================
 * @see 由于Java6.0支持大部分的算法,但受到出口限制,其密钥长度不能满足需求
 * @see 所以特别需要注意的是:如果使用256位的密钥,则需要无政策限制文件(Unlimited Strength Jurisdiction Policy Files)
 * @see 不过Sun是通过权限文件local_poblicy.jar和US_export_policy.jar做的相应限制,我们可以在Sun官网下载替换文件,减少相关限制
 * @see 网址为http://www.oracle.com/technetwork/java/javase/downloads/index.html
 * @see 在该页面的最下方找到Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
 *      6,点击下载
 * @see http://download.oracle.com/otn-pub/java/jce_policy/6/jce_policy-6.zip
 * @see http://download.oracle.com/otn-pub/java/jce/7/UnlimitedJCEPolicyJDK7.zip
 * @see 然后覆盖本地JDK目录和JRE目录下的security目录下的文件即可
 * @see ===========================================================================================================
 * @see 关于AES的更多详细介绍,可以参考此爷的博客http://blog.csdn.net/kongqz/article/category/800296
 * @create Jul 17, 2012 6:35:36 PM
 * @author 玄玉(http://blog.csdn/net/jadyer)
 */
public class AesEncryptDemo { // 密钥算法
  private String KEY_ALGORITHM = "AES";

  // 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
  private String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

  /**
   * 生成密钥
   */
  public String initkey() throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM); // 实例化密钥生成器
    kg.init(128); // 初始化密钥生成器:AES要求密钥长度为128,192,256位
    SecretKey secretKey = kg.generateKey(); // 生成密钥
    return Base64.getEncoder().encodeToString(secretKey.getEncoded()); // 获取二进制密钥编码形式
  }

  /**
   * 转换密钥
   */
  private Cipher initCipher(int encryptMode, String key) throws Exception {
    SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM);

    // 使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
    Cipher cipher;
    if (CIPHER_ALGORITHM.contains("PKCS7Padding")) {
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
      cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
    } else {
      cipher = Cipher.getInstance(CIPHER_ALGORITHM); // 实例化Cipher对象，它用于完成实际的加密操作
    }

    cipher.init(encryptMode, keySpec); // 初始化Cipher对象，设置为加密模式

    return cipher;
  }

  /**
   * 加密数据
   * 
   * @param key 密钥
   * @param data 待加密数据
   * @return 加密后的数据
   */
  public String encrypt(String key, String data) throws Exception {
    Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key);
    return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes())); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
  }

  /**
   * 解密数据
   * 
   * @param key 密钥
   * @param data 待解密数据
   * @return 解密后的数据
   */
  public String decrypt(String key, String data) throws Exception {
    Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key);
    return new String(cipher.doFinal(Base64.getDecoder().decode(data))); // 执行解密操作
  }

  public static void main(String[] args) throws Exception {
    AesEncryptDemo demo = new AesEncryptDemo();

    String source = "站在云端，敲下键盘，望着通往世界另一头的那扇窗，只为做那读懂0和1的人。。";
    System.out.println("原文：" + source);

    String key = demo.initkey();
    System.out.println("密钥：" + key);

    String encryptData = demo.encrypt(key, source);
    System.out.println("加密：" + encryptData);

    String decryptData = demo.decrypt(key, encryptData);
    System.out.println("解密: " + decryptData);

    if (!source.equals(decryptData)) {
      throw new RuntimeException("解密失败...");
    }
  }
}
