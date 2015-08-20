package com.ozz.utils.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.LoggerFactory;

public class JdbcUtil {

  public static Connection getConnection(String url, String username, String password) {
    Connection conn;
    try {
      conn = DriverManager.getConnection(url, username, password);
      return conn;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void closeQuietly(AutoCloseable conn) {
    if (conn == null) {
      return;
    }
    try {
      conn.close();
    } catch (Exception e) {
      LoggerFactory.getLogger(JdbcUtil.class).error("close DB Connection error", e);
    }
  }

}
