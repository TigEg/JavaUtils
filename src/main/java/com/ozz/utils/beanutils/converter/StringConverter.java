package com.ozz.utils.beanutils.converter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;

import com.ozz.utils.beanutils.BeanUtilsExt;

/**
 * 
 * @author ozz
 * 
 */
public class StringConverter implements Converter {

    private static SimpleDateFormat simpleDateFormat;

    public StringConverter() {
        simpleDateFormat = new SimpleDateFormat(BeanUtilsExt.DEFAULT_DATEFORMAT);
    }

    public StringConverter(String dateFormat) {
        simpleDateFormat = new SimpleDateFormat(dateFormat);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return value;
        } else if (value instanceof Date) {
            if (value instanceof Timestamp) {
                String str = value.toString();
                return str.substring(0, str.lastIndexOf("."));
            } else {
                return simpleDateFormat.format(value);
            }
        } else if (value instanceof Boolean) {
            return (Boolean) value == true ? "1" : "0";
        } else if (value instanceof Long) {
            return String.valueOf(value);
        } else if (value instanceof Integer) {
            return String.valueOf(value);
        } else {
            return null;
        }
    }

}