package com.ozz.utils.beanutils.base;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ozz.utils.beanutils.BeanUtilsExt;

public class BeanUtilsBeanExt extends BeanUtilsBean {
  protected Logger log = LoggerFactory.getLogger(getClass());

  @SuppressWarnings("rawtypes")
  private static final ContextClassLoaderLocal BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
    protected Object initialValue() {
      BeanUtilsBeanExt beanUtils = new BeanUtilsBeanExt();

      // beanUtils.getConvertUtils().register(BeanUtilsExt.dateConverter,Date.class);
      // beanUtils.getConvertUtils().register(BeanUtilsExt.stringConverter,String.class);
      beanUtils.getConvertUtils().register(BeanUtilsExt.BOOLEAN_CONVERTER, Boolean.class);
      beanUtils.getConvertUtils().register(BeanUtilsExt.LONG_CONVERTER, Long.class);

      return beanUtils;
    }
  };

  public static BeanUtilsBeanExt getInstance() {
    return (BeanUtilsBeanExt) BEANS_BY_CLASSLOADER.get();
  }

  @SuppressWarnings("rawtypes")
  public void copyProperties(Object dest, Object orig, String[] ignoreProperties) {

    try {
      // Validate existence of the specified beans
      if (dest == null) {
        throw new IllegalArgumentException("No destination bean specified");
      }
      if (orig == null) {
        throw new IllegalArgumentException("No origin bean specified");
      }
      if (log.isDebugEnabled()) {
        log.debug("BeanUtils.copyProperties(" + dest + ", " + orig + ")");
      }

      List<String> ignorePropList = ignoreProperties == null ? new ArrayList<String>() : Arrays.asList(ignoreProperties);
      // Copy the properties, converting as necessary
      if (orig instanceof DynaBean) {
        DynaProperty[] origDescriptors = ((DynaBean) orig).getDynaClass().getDynaProperties();
        for (int i = 0; i < origDescriptors.length; i++) {
          String name = origDescriptors[i].getName();
          if (!ignorePropList.contains(name)) {
            // Need to check isReadable() for WrapDynaBean
            // (see Jira issue# BEANUTILS-61)
            if (getPropertyUtils().isReadable(orig, name) && getPropertyUtils().isWriteable(dest, name)) {
              Object value = ((DynaBean) orig).get(name);
              copyProperty(dest, name, value);
            }
          }
        }
      } else if (orig instanceof Map) {
        Iterator entries = ((Map) orig).entrySet().iterator();
        while (entries.hasNext()) {
          Map.Entry entry = (Map.Entry) entries.next();
          String name = (String) entry.getKey();
          if (!ignorePropList.contains(name)) {
            if (getPropertyUtils().isWriteable(dest, name)) {
              copyProperty(dest, name, entry.getValue());
            }
          }
        }
      } else /* if (orig is a standard JavaBean) */{
        PropertyDescriptor[] origDescriptors = getPropertyUtils().getPropertyDescriptors(orig);
        for (int i = 0; i < origDescriptors.length; i++) {
          String name = origDescriptors[i].getName();
          if (!ignorePropList.contains(name)) {
            if ("class".equals(name)) {
              continue; // No point in trying to set an object's
                        // class
            }
            if (getPropertyUtils().isReadable(orig, name) && getPropertyUtils().isWriteable(dest, name)) {
              try {
                Object value;
                try {
                  value = getPropertyUtils().getSimpleProperty(orig, name);
                } catch (IllegalAccessException e) {
                  throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                  throw new RuntimeException(e);
                }
                copyProperty(dest, name, value);
              } catch (NoSuchMethodException e) {
                // Should not happen
              }
            }
          }
        }
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
