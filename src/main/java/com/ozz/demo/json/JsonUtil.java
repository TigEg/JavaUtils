package com.ozz.demo.json;

import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
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
public class JsonUtil {
  private static ThreadLocal<ObjectMapper> objectMapper = new ThreadLocal<ObjectMapper>() {
    @Override
    protected ObjectMapper initialValue() {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));// 日期格式
      return objectMapper;
    }
  };

  public static void main(String[] args) {
    // 链接
    String rowsJson = "[{\"id\":\"id1\",\"name\":\"name1\"},{\"id\":\"id2\",\"name\":\"name2\"}]";
    List<Item> rows = formJson(rowsJson, new TypeReference<List<Item>>() {});
    System.out.println(rows);
    // 复杂对象
    Page<Item> page = formJson("{\"rows\":" + rowsJson + "}", new TypeReference<Page<Item>>() {});
    System.out.println(page);
  }

  public static String toJson(Object bean) {
    try {
      return objectMapper.get().writeValueAsString(bean);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T formJson(String json, Class<T> c) {
    try {
      return objectMapper.get().readValue(json, c);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 解析复杂格式
   */
  public static <T> T formJson(String json, TypeReference<T> typeReference) {
    try {
      return objectMapper.get().readValue(json, typeReference);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
