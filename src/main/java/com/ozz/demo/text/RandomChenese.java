package com.ozz.demo.text;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

public class RandomChenese {
  public static void main(String[] args) throws UnsupportedEncodingException {
    for (int i = 0; i < 3; i++) {
      System.out.println(getRandomChinese(6));
    }
  }

  public static String getRandomChinese(int length) throws UnsupportedEncodingException {
    if (length <= 0) {
      throw new RuntimeException("length ");
    }

    Pair<Integer,Integer> pair = Pair.of(0x4e00, 0x9fa5);// 基本汉字Unicode范围
    char[] charArr = new char[length];
    for (int i = 0; i < length; i++) {
      charArr[i] = (char) RandomUtils.nextInt(pair.getLeft(), pair.getRight());
    }
    return new String(charArr);
  }
}
