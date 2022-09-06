package edu.wpi.agileAngels.Database;

import edu.wpi.agileAngels.Adb;
import java.sql.*;
import java.util.HashMap;

public class LocationsTable implements TableI {

  /**
   * Adds a new location to the location table.
   *
   * @param obj Location object
   * @return True if added, false otherwise.
   */
  @Override
  public boolean add(Object obj) {
    try {
      if (!(obj instanceof Location)) {
        return false;
      }
      Location loc = (Location) obj;
      String add =
          "INSERT INTO Locations(NodeID,xcoord,ycoord,Floor,building,nodeType,longName,shortName)VALUES(?,?,?,?,?,?,?,?)";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(add);
      preparedStatement.setString(1, loc.getNodeID());
      preparedStatement.setDouble(2, loc.getXCoord());
      preparedStatement.setDouble(3, loc.getYCoord());
      preparedStatement.setString(4, loc.getFloor());
      preparedStatement.setString(5, loc.getBuilding());
      preparedStatement.setString(6, loc.getNodeType());
      preparedStatement.setString(7, loc.getLongName());
      preparedStatement.setString(8, loc.getShortName());
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Deletes a location from the location table.
   *
   * @param str NodeID for a location object
   * @return True if added, false otherwise.
   */
  @Override
  public boolean delete(String str) {
    try {
      PreparedStatement preparedStatement =
          DBconnection.getConnection().prepareStatement("DELETE FROM Locations WHERE NodeID = ?");
      preparedStatement.setString(1, str);
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Updates location in table with new attributes.
   *
   * @param obj Location
   * @return True if added, false otherwise.
   */
  @Override
  public boolean update(Object obj) {
    try {
      if (!(obj instanceof Location)) {
        return false;
      }
      Location loc = (Location) obj;
      String id = loc.getNodeID();
      PreparedStatement preparedStatement =
          DBconnection.getConnection()
              .prepareStatement(
                  "UPDATE Locations SET xcoord = ?, ycoord = ?, floor = ?, building = ?, nodetype = ?, longname = ?, shortname = ? WHERE NodeID = ?");
      preparedStatement.setDouble(1, loc.getXCoord());
      preparedStatement.setDouble(2, loc.getYCoord());
      preparedStatement.setString(3, loc.getFloor());
      preparedStatement.setString(4, loc.getBuilding());
      preparedStatement.setString(5, loc.getNodeType());
      preparedStatement.setString(6, loc.getLongName());
      preparedStatement.setString(7, loc.getShortName());
      preparedStatement.setString(8, id);
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Creates a new location table.
   *
   * @return True if added, false otherwise.
   */
  @Override
  public boolean createTable() {
    try {
      Statement query = DBconnection.getConnection().createStatement();
      String queryLocations =
          "CREATE TABLE Locations( "
              + "NodeID VARCHAR(50),"
              + "xcoord VARCHAR(50),"
              + "ycoord VARCHAR(50),"
              + "Floor VARCHAR(50),"
              + "building VARCHAR(50),"
              + "NodeType VARCHAR(50),"
              + "longName VARCHAR(50),"
              + "shortName VARCHAR(50))";
      query.execute(queryLocations);
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Drops the location table.
   *
   * @return True if successful, false otherwise.
   */
  @Override
  public boolean dropTable() {
    try {
      Statement droptable = DBconnection.getConnection().createStatement();
      String dropLoc = "DROP TABLE Locations";
      droptable.execute(dropLoc);
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  @Override
  public HashMap<String, Object> getData() throws SQLException {
    Adb.readCSVLocations();
    return Adb.getLocations();
  }
}
