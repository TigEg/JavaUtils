package com.ozz.utils.beanutils.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.ozz.utils.beanutils.BeanUtilsExt;

/**
 * 
 * @author ozz
 * 
 */
public class DateConverter implements Converter {

  private SimpleDateFormat simpleDateFormat;

  public DateConverter() {
    simpleDateFormat = new SimpleDateFormat(BeanUtilsExt.DEFAULT_DATEFORMAT);
  }

  public DateConverter(String dateFormat) {
    this.simpleDateFormat = new SimpleDateFormat(dateFormat);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public Object convert(Class type, Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Date) {
      return value;
    } else if (value instanceof String) {
      String valueStr = value.toString().trim();
      if (valueStr.length() > 0) {
        try {
          return simpleDateFormat.parse(value.toString());
        } catch (Exception e) {
          throw new ConversionException(e);
        }
      }
    }
    return null;
  }

}
