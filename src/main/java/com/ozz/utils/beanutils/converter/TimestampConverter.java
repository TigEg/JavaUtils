package com.ozz.utils.beanutils.converter;

import java.sql.Timestamp;

import org.apache.commons.beanutils.Converter;

/**
 * 
 * @author ozz
 * 
 */
public class TimestampConverter implements Converter {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Object convert(Class type, Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Timestamp) {
      return value;
    } else if (value instanceof String) {
      return Timestamp.valueOf(value.toString());
    } else {
      return null;
    }
  }

}
