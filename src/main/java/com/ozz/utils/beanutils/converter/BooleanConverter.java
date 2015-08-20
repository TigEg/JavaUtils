package com.ozz.utils.beanutils.converter;

import org.apache.commons.beanutils.Converter;

/**
 * 
 * @author ozz
 * 
 */
public class BooleanConverter implements Converter {

    public BooleanConverter() {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Boolean) {
            return value;
        } else if (value instanceof String) {
            return value.equals("1") || value.equals("true");
        } else if (value instanceof Long) {
            return ((Long) value) == 1;
        } else {
            return null;
        }
    }

}