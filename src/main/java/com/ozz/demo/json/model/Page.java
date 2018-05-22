package com.ozz.demo.json.model;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Page<T> {
  private List<T> rows;
  @Override
  public String toString() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  public List<T> getRows() {
    return rows;
  }
  public void setRows(List<T> rows) {
    this.rows = rows;
  }
}
