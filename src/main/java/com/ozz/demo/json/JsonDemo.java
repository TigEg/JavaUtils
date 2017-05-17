package com.ozz.demo.json;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

  public String toJson(Object bean) throws JsonProcessingException {
    return getObjectMapper().writeValueAsString(bean);
  }

  public <T> T formJson(String json, Class<T> c) throws JsonParseException, JsonMappingException, IOException {
    return getObjectMapper().readValue(json, c);
  }

}
