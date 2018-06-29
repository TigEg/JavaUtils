package com.ozz.demo.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Provides some convenient method implementations. Other object can extend it to leverage these
 * methods.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ToStringDemo {
  private JsonDemo jsonUtil;

  /**
   * A JSON representation of the object.
   */
  @Override
  public String toString() {
    try {
      return jsonUtil.toJson(this);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
