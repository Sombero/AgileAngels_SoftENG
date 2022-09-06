package edu.wpi.agileAngels.Database;

import java.sql.SQLException;
import java.util.HashMap;

public interface TableI {
  boolean add(Object obj);

  boolean delete(String str);

  boolean update(Object obj);

  boolean createTable();

  boolean dropTable();

  HashMap<String, Object> getData() throws SQLException;
}
