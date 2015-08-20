package com.ozz.utils.beanutils.base;

import org.apache.commons.beanutils.ConvertUtilsBean;

/**
 * 
 * 
 * @author ozz
 */
public class ConvertUtilsBeanExt extends ConvertUtilsBean {

    protected static ConvertUtilsBean getInstance() {
        return BeanUtilsBeanExt.getInstance().getConvertUtils();
    }

}
