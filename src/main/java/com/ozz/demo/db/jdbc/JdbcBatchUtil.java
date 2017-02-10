package com.ozz.demo.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.ozz.demo.db.jdbc.base.ColumnInfo;
import com.ozz.demo.db.jdbc.base.JdbcOracleBatchUtil;
import com.ozz.demo.db.jdbc.base.JdbcSqlserverBatchUtil;

public abstract class JdbcBatchUtil {

  public JdbcBatchUtil getOracleInstance() {
    return new JdbcOracleBatchUtil();
  }

  public JdbcBatchUtil getSqlserverInstance() {
    return new JdbcSqlserverBatchUtil();
  }

  public abstract Map<String, ColumnInfo> queryColumnInfo(Connection conn, String tableName) throws SQLException;

  public abstract void setParameter(PreparedStatement ps, int parameterIndex, String value, ColumnInfo colInfo) throws SQLException;

}
