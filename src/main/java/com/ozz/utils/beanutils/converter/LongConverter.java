package com.ozz.utils.beanutils.converter;

import org.apache.commons.beanutils.Converter;

/**
 * 
 * @author ozz
 * 
 */
public class LongConverter implements Converter {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Object convert(Class type, Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Long) {
      return value;
    } else if (value instanceof String) {
      String str = (String) value;
      if (str.length() > 0) {
        return Long.parseLong(str);
      } else {
        return null;
      }
    } else if (value instanceof Integer) {
      return ((Integer) value).longValue();
    } else if (value instanceof Boolean) {
      return ((Boolean) value) ? new Long(1) : new Long(0);
    } else {
      return null;
    }
  }

}
