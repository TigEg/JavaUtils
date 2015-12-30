package com.ozz.sample.json;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用jackson框架实现json、对象转化
 * 
 * 注:注解忽略不可识别的属性注解 @JsonIgnoreProperties(ignoreUnknown = true)
 * 
 * @author ouzezh
 *
 *
 */
public class JsonUtil {

  public String toJson(Object bean) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    try {
      return objectMapper.writeValueAsString(bean);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T formJson(String json, Class<T> c) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    T bean;
    try {
      bean = objectMapper.readValue(json, c);
      return bean;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
