package com.ozz.utils.beanutils;

import org.springframework.beans.BeanUtils;

public class BeanUtil {

  public static void copyProperties(Object source, Object target, Class<?> editable) {
    BeanUtils.copyProperties(source, target, editable);
  }

}
