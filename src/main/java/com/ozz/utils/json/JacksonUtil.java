package com.ozz.utils.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
public class JacksonUtil {

	public String toJson(Object bean) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return objectMapper.writeValueAsString(bean);
	}

	public <T> T formJson(String json, Class<T> c) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		T bean = objectMapper.readValue(json, c);
		return bean;
	}

}