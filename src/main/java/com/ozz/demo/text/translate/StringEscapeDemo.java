package com.ozz.demo.text.translate;

import org.apache.commons.text.StringEscapeUtils;

public class StringEscapeDemo {

  public String escapeHtml(String input) {
    return StringEscapeUtils.escapeHtml4(input);
  }

  public String unescapeHtml(String input) {
    return StringEscapeUtils.unescapeHtml4(input);
  }
}
