package com.ozz.demo.security.encrypt.digest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 数据摘要
 */
public class DigestDemo {
  public String digest(Path path) throws IOException {
    try (InputStream input = Files.newInputStream(path)) {
      return DigestUtils.md5Hex(input);
      // return DigestUtils.sha1Hex(input);;
    }
  }

  public String digest(String data) throws IOException {
    return DigestUtils.md5Hex(data);
    // return DigestUtils.sha1Hex(input);;
  }
}
