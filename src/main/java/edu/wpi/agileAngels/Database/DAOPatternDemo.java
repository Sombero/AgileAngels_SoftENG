package edu.wpi.agileAngels.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

// test DAO methods
// Sandbox is fun!
public class DAOPatternDemo {
  private static Connection connection;
  private static LocationDAO locationDao;

  public static void main(String[] args) throws SQLException {
    {
      connection = DriverManager.getConnection("jdbc:derby:myDB;create=true");

      HashMap<String, Location> data = locationDao.getAllLocations();

      Location loc1 = data.get("FHALL02401");
      String backupOne = loc1.getLongName();
      String backupTwo = loc1.getBuilding();
      // String backupThree = loc1.getYCoord();
      // String backupFour = loc1.getXCoord();
      String backupFive = loc1.getFloor();
      String backupSix = loc1.getNodeType();
      String backupSeven = loc1.getShortName();
      locationDao.updateLocationLongName(data.get("FHALL02401"), "UPDATED?");
      locationDao.updateLocationBuilding(data.get("FHALL02401"), "UPDATED?");
      // locationDao.updateLocationXCoord(data.get("FHALL02401"), "UPDATED?");
      // locationDao.updateLocationYCoord(data.get("FHALL02401"), "UPDATED?");
      locationDao.updateLocationFloor(data.get("FHALL02401"), "UPDATED?");
      locationDao.updateLocationType(data.get("FHALL02401"), "UPDATED?");
      locationDao.updateLocationShortName(data.get("FHALL02401"), "UPDATED?");
      locationDao.updateLocationLongName(loc1, backupOne);
      locationDao.updateLocationBuilding(loc1, backupTwo);
      // locationDao.updateLocationYCoord(loc1, backupThree);
      // locationDao.updateLocationXCoord(loc1, backupFour);
      locationDao.updateLocationFloor(loc1, backupFive);
      locationDao.updateLocationType(loc1, backupSix);
      locationDao.updateLocationShortName(loc1, backupSeven);
    }
  }
}
