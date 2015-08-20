package com.ozz.utils.beanutils.base;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

/**
 * 
 * 
 * @author ozz
 */
public class ConvertUtilsExt extends ConvertUtils {

    @SuppressWarnings("rawtypes")
    public static void register(Converter converter, Class clazz) {
        ConvertUtilsBeanExt.getInstance().register(converter, clazz);
    }

}
