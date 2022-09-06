package edu.wpi.agileAngels.Database;

import java.sql.SQLException;
import java.util.HashMap;

// includes the four DOA methods
public interface LocationDAO {

  public HashMap<String, Location> getAllLocations();

  public Location getLocation(String NodeID);

  public void updateLocationType(Location location, String newLocationType);

  public void updateLocationFloor(Location location, String newLocationFloor);

  public void updateLocationBuilding(Location location, String newLocationBuilding);

  public void updateLocationLongName(Location location, String newLocationLongName);

  public void updateLocationShortName(Location location, String newLocationShortName);

  public void updateLocationXCoord(Location location, Double newLocationXCoord);

  public void updateLocationYCoord(Location location, Double newLocationYCoord);

  public void deleteLocation(Location location);

  public void addLocation(Location location) throws SQLException;
}
