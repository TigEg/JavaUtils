package com.ozz.demo.beanutils;

import org.springframework.beans.BeanUtils;

public class BeanUtil {

  public void copyProperties(Object source, Object target, Class<?> editable) {
    BeanUtils.copyProperties(source, target, editable);
  }
}
