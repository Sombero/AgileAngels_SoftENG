package edu.wpi.agileAngels.Database;

import edu.wpi.agileAngels.Adb;
import java.sql.*;
import java.util.HashMap;

public class MedicalEquipmentTable implements TableI {

  /**
   * Adds a MedicalEquip to the table
   *
   * @param obj MedicalEquip
   * @return True if successful, false if not
   */
  @Override
  public boolean add(Object obj) {
    try {
      if (!(obj instanceof MedicalEquip)) {
        return false;
      }
      MedicalEquip medE = (MedicalEquip) obj;
      String add = "INSERT INTO MedicalEquipment(ID,Type,Clean,Location,Status)VALUES(?,?,?,?,?)";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(add);
      preparedStatement.setString(1, medE.getID());
      preparedStatement.setString(2, medE.getType());
      if (medE.isClean()) {
        preparedStatement.setString(3, "Clean");
      } else {
        preparedStatement.setString(3, "Dirty");
      }
      preparedStatement.setString(4, medE.getLocation().getNodeID());
      preparedStatement.setString(5, medE.getStatus());
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Deletes a MedicalEquip from the table
   *
   * @param str MedicalEquip ID
   * @return True if successful, false if not
   */
  @Override
  public boolean delete(String str) {
    try {
      String delete = "DELETE FROM MedicalEquipment WHERE ID = ?";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(delete);
      preparedStatement.setString(1, str);
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Updates the MedicalEquip entry by ID
   *
   * @param obj MedicalEquip
   * @return True if successful, false if not
   */
  @Override
  public boolean update(Object obj) {
    try {
      if (!(obj instanceof MedicalEquip)) {
        return false;
      }
      MedicalEquip medE = (MedicalEquip) obj;
      String update =
          "UPDATE MedicalEquipment SET Type = ?, Clean = ?, Location = ?, Status = ? WHERE ID = ?";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(update);
      preparedStatement.setString(1, medE.getType());
      if (medE.isClean()) {
        preparedStatement.setString(2, "Clean");
      } else {
        preparedStatement.setString(2, "Dirty");
      }
      preparedStatement.setString(3, medE.getLocation().getNodeID());
      preparedStatement.setString(4, medE.getStatus());
      preparedStatement.setString(5, medE.getID());
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Creates a new MedicalEquipment table
   *
   * @return True if successful, false if not
   */
  @Override
  public boolean createTable() {
    try {
      Statement queryEquip = DBconnection.getConnection().createStatement();
      String queryEq =
          "CREATE TABLE MedicalEquipment ( "
              + "ID VARCHAR(50),"
              + "Type VARCHAR(50),"
              + "Clean VARCHAR(50),"
              + "Location VARCHAR(50),"
              + "Status VARCHAR(50),"
              + "PRIMARY KEY (ID))";

      queryEquip.execute(queryEq);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Drops an existing MedicalEquipment table from the database
   *
   * @return True if successful, false if not
   */
  @Override
  public boolean dropTable() {
    try {
      Statement dropTable = DBconnection.getConnection().createStatement();
      String queryDropMed = "DROP TABLE MedicalEquipment";
      dropTable.execute(queryDropMed);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  @Override
  public HashMap<String, Object> getData() throws SQLException {
    try {
      DBconnection.getConnection().setAutoCommit(false);
      String sql = "SELECT * FROM MedicalEquipment";
      HashMap<String, Location> locationHashMap = Adb.getLocations();

      Connection connection = DBconnection.getConnection();

      Statement statement = connection.createStatement();
      ResultSet result = statement.executeQuery(sql);
      HashMap<String, Object> empty = new HashMap<>();

      while (result.next()) {

        String name = result.getString("ID");
        String type = result.getString("Type");
        boolean clean;
        if (result.getString("Clean").compareTo("Dirty") == 0) {
          clean = false;
        } else {
          clean = true;
        }
        Location location = locationHashMap.get(result.getString("Location"));
        String status = result.getString("Status");

        MedicalEquip medicalEquip = new MedicalEquip(name, type, clean, location, status);

        Adb.addMedEquip(medicalEquip);
      }
      DBconnection.getConnection().setAutoCommit(true);
      return empty;
    } catch (SQLException sqlException) {
      DBconnection.getConnection().setAutoCommit(true);
      return null;
    }
  }
}
