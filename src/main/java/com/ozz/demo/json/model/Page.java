package com.ozz.demo.json.model;

import java.util.List;

public class Page<T> extends BaseObject {
  private List<T> rows;
  public List<T> getRows() {
    return rows;
  }
  public void setRows(List<T> rows) {
    this.rows = rows;
  }
}
