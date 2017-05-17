package com.ozz.demo.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {

  public Connection getConnection(String url, String username, String password) throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }
}
