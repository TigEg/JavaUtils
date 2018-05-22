package com.ozz.demo.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozz.demo.json.model.Item;
import com.ozz.demo.json.model.Page;

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
  @Test
  public void test() throws Exception {
    try {
      // 链接
      String rowsJson = "[{\"id\":\"id1\",\"name\":\"name1\"},{\"id\":\"id2\",\"name\":\"name2\"}]";
      List<Item> rows = getObjectMapper().readValue(rowsJson, new TypeReference<List<Item>>() {});
      System.out.println(rows);
      // 复杂对象
      Page<Item> page = getObjectMapper().readValue("{\"rows\":"+rowsJson+"}", new TypeReference<Page<Item>>() {});
      System.out.println(page);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

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

  public <T> T formJson(String json, TypeReference<T> typeReference) throws JsonParseException, JsonMappingException, IOException {
    return getObjectMapper().readValue(json, typeReference);
  }
}
