package com.ozz.utils.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.ozz.utils.db.jdbc.base.JdbcOracleBatchUtil;
import com.ozz.utils.db.jdbc.base.JdbcSqlserverBatchUtil;

public abstract class JdbcBatchUtil {

  public static JdbcBatchUtil getOracleInstance() {
    return new JdbcOracleBatchUtil();
  }

  public static JdbcBatchUtil getSqlserverInstance() {
    return new JdbcSqlserverBatchUtil();
  }

  public abstract Map<String, ColumnInfo> queryColumnInfo(Connection conn, String tableName) throws SQLException;

  public abstract void setParameter(PreparedStatement ps, int parameterIndex, String value, ColumnInfo colInfo) throws SQLException;

}
