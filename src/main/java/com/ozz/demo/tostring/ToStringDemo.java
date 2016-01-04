package com.ozz.demo.tostring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ozz.demo.json.JsonDemo;

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
    return jsonUtil.toJson(this);
  }
}
