package com.ozz.demo.json;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用jackson框架实现json、对象转化
 * 
 * 注解忽略不可识别的属性注解 @JsonIgnoreProperties(ignoreUnknown = true)
 * 
 * 注解忽略空值 @JsonInclude(Include.NON_NULL)
 * 
 * @author ouzezh
 *
 *
 */
public class JsonDemo {

  private ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));// 日期格式
    return objectMapper;
  }

  public String toJson(Object bean) {
    try {
      return getObjectMapper().writeValueAsString(bean);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T formJson(String json, Class<T> c) {
    T bean;
    try {
      bean = getObjectMapper().readValue(json, c);
      return bean;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
