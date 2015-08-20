package com.ozz.utils.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ozz.utils.beanutils.base.BeanUtilsBeanExt;
import com.ozz.utils.beanutils.base.ConvertUtilsExt;
import com.ozz.utils.beanutils.converter.BooleanConverter;
import com.ozz.utils.beanutils.converter.DateConverter;
import com.ozz.utils.beanutils.converter.LongConverter;
import com.ozz.utils.beanutils.converter.StringConverter;
import com.ozz.utils.date.DateFormatUtil;

/**
 * 拷贝属性扩展:主要实现忽略属性和特定转化规则
 * 
 * @author ozz
 * 
 */
public class BeanUtilsExt {

    public static final String DEFAULT_DATEFORMAT = DateFormatUtil.PATTERN_DATE;

    public static final Map<String, DateConverter> DATE_CONVERTER_MAP;
    public static final Map<String, StringConverter> STRING_CONVERTER_MAP;
    public static final BooleanConverter BOOLEAN_CONVERTER = new BooleanConverter();
    public static final LongConverter LONG_CONVERTER = new LongConverter();

    static {
        DATE_CONVERTER_MAP = new HashMap<String, DateConverter>();
        STRING_CONVERTER_MAP = new HashMap<String, StringConverter>();
        for (String dateFormat : DateFormatUtil.PATTERNS) {
            DATE_CONVERTER_MAP.put(dateFormat, new DateConverter(dateFormat));
            STRING_CONVERTER_MAP.put(dateFormat, new StringConverter(dateFormat));
        }
    }

    public static void copyProperties(Object dest, Object orig) {
        copyProperties(dest, orig, null, null);
    }

    public static void copyProperties(Object dest, Object orig, String[] ignoreProperties) {
        copyProperties(dest, orig, null, ignoreProperties);
    }

    public static void copyProperties(Object dest, Object orig, String dateFormat) {
        copyProperties(dest, orig, dateFormat, null);
    }

    public static void copyProperties(Object dest, Object orig, String dateFormat, String[] ignoreProperties) {
        if (dateFormat == null)
            dateFormat = DEFAULT_DATEFORMAT;
        if (!DATE_CONVERTER_MAP.containsKey(dateFormat)) {
            throw new RuntimeException("UnSupport format: '" + dateFormat + "'");
        }

        ConvertUtilsExt.register(DATE_CONVERTER_MAP.get(dateFormat), Date.class);
        ConvertUtilsExt.register(STRING_CONVERTER_MAP.get(dateFormat), String.class);

        if (ignoreProperties == null || ignoreProperties.length == 0) {
            try {
                BeanUtilsBeanExt.getInstance().copyProperties(dest, orig);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            BeanUtilsBeanExt.getInstance().copyProperties(dest, orig, ignoreProperties);
        }
    }

}
