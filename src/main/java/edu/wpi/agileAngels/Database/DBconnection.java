package edu.wpi.agileAngels.Database;

import edu.wpi.agileAngels.Adb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton connection
// One connection, will not create multiple, eliminates repetitive code
// TODO make sure connections are good, most important part
public class DBconnection {
  private static DBConnectionEnum database;
  private static Connection connection;

  // Client-server is the default connection
  static {
    try {
      database = DBConnectionEnum.CLIENT_SERVER;
      connection = DriverManager.getConnection("jdbc:derby://localhost:1527/myCSDB;create=true");

    } catch (SQLException e) {
      System.out.println("Establishing embedded connection failed.");
      e.printStackTrace();
    }
  }

  // Switching from current connection to the other connection
  public static void switchConnection() {
    try {
      switch (database) {
        case EMBEDDED:
          connection.close();
          database = DBConnectionEnum.CLIENT_SERVER;
          connection =
              DriverManager.getConnection("jdbc:derby://localhost:1527/myCSDB;create=true");
          System.out.println("Switching to client-server.");
          Adb.resetMedEquipment();
          Adb.populateMedicalEquipment();
          Adb.resetServiceRequests();
          Adb.populateServiceRequests();
          break;
        case CLIENT_SERVER:
          connection.close();
          database = DBConnectionEnum.EMBEDDED;
          connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");
          System.out.println("Switching to embedded.");
          Adb.resetMedEquipment();
          Adb.populateMedicalEquipment();
          Adb.resetServiceRequests();
          Adb.populateServiceRequests();
          break;
        default:
          return;
      }
    } catch (SQLException sqlException) {
      System.out.println("Switching the connection failed.");
      sqlException.printStackTrace();
    }
  }

  public static Connection getConnection() {
    return connection;
  }

  /** Shuts the connection down */
  public static void shutdown() {
    try {
      connection.close();
    } catch (SQLException e) {
      System.out.println("Shutdown unsuccessful.");
    }
  }
}
