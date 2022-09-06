package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.Location;
import edu.wpi.agileAngels.Database.LocationDAOImpl;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.input.MouseEvent;

public class LocationNodeManager {

  public MapsController mapsController;
  private LocationDAOImpl locationDAO = LocationDAOImpl.getInstance();
  private HashMap<String, LocationNode> nodes = new HashMap<>();
  HashMap<String, Location> locationsHash = locationDAO.getAllLocations();
  ArrayList<Location> locationsList = new ArrayList<Location>(locationsHash.values());
  private int[][] typeCounts = new int[15][6];
  private HashMap<String, Integer> floorsAndTypes = new HashMap<>();

  public LocationNodeManager(MapsController mapsController) throws SQLException {
    this.mapsController = mapsController;

    // initialize list of location floors and types
    floorsAndTypes.put("PATI", 0);
    floorsAndTypes.put("STOR", 1);
    floorsAndTypes.put("DIRT", 2);
    floorsAndTypes.put("HALL", 3);
    floorsAndTypes.put("ELEV", 4);
    floorsAndTypes.put("REST", 5);
    floorsAndTypes.put("STAI", 6);
    floorsAndTypes.put("DEPT", 7);
    floorsAndTypes.put("LABS", 8);
    floorsAndTypes.put("INFO", 9);
    floorsAndTypes.put("CONF", 11);
    floorsAndTypes.put("EXIT", 11);
    floorsAndTypes.put("RETL", 12);
    floorsAndTypes.put("SERV", 13);
    floorsAndTypes.put("BATH", 14);
    floorsAndTypes.put("2", 0);
    floorsAndTypes.put("3", 1);
    floorsAndTypes.put("4", 2);
    floorsAndTypes.put("5", 3);
    floorsAndTypes.put("L1", 4);
    floorsAndTypes.put("L2", 5);

    // initialize counts for each type of location to zero
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 6; j++) {
        typeCounts[i][j] = 0;
      }
    }
  }

  public double getCroppedMapXOffset() {
    return mapsController.getCroppedMapXOffset();
  }

  public double getCroppedMapYOffset() {
    return mapsController.getCroppedMapYOffset();
  }

  public double getCroppedMapWidth() {
    return mapsController.getCroppedMapWidth();
  }

  public double getImagePaneWidth() {
    return mapsController.getImagePaneWidth();
  }

  public double getMapXCoordFromClick(MouseEvent click) {
    return mapsController.getMapXCoordFromClick(click);
  }

  public double getMapYCoordFromClick(MouseEvent click) {
    return mapsController.getMapYCoordFromClick(click);
  }

  public void setCoordsOnMouseEvent(MouseEvent click) {
    mapsController.setCoordsOnMouseEvent(click);
  }

  void deleteNode(String nodeID) {
    nodes.remove(nodeID);
    HashMap<String, Location> locationsHash = locationDAO.getAllLocations();
    Location location = locationsHash.get(nodeID);
    locationDAO.deleteLocation(location);
  }

  // gets all locations from the DB and creates nodes from them
  void createNodesFromDB() {
    HashMap<String, Location> locationsHash = locationDAO.getAllLocations();
    ArrayList<Location> locationsList = new ArrayList<Location>(locationsHash.values());
    for (Location location : locationsList) {
      typeCounts[floorsAndTypes.get(location.getNodeType())][
              floorsAndTypes.get(location.getFloor())] +=
          1;
      mapsController.displayLocationNode(
          addNode(location, mapsController.appController.getCurrentUser().getPermissionLevel()));
    }
  }

  Integer getTypeCount(String type, String floor) {
    typeCounts[floorsAndTypes.get(type)][floorsAndTypes.get(floor)] += 1;
    return typeCounts[floorsAndTypes.get(type)][floorsAndTypes.get(floor)];
  }

  LocationNode addNode(Location location, int permission) {
    LocationNode locationNode = new LocationNode(location, this, permission);
    nodes.put(locationNode.getNodeID(), locationNode);

    // add the new location to the database
    locationDAO.addLocation(location);

    return locationNode;
  }

  void editNode(LocationNode locationNode, String longName, String type) {
    // edit the corresponding location in the backend
    locationDAO.updateLocationLongName(locationNode.getLocation(), longName);
    locationDAO.updateLocationType(locationNode.getLocation(), type);
  }

  // gets called on button press and gets the node data
  void loadNode(LocationNode locationNode) {
    mapsController.populateLocationNodeData(locationNode);
  }

  public void setDraggedNodeCoords(MouseEvent mouseEvent) {
    mapsController.setCoordsOnMouseEvent(mouseEvent);
  }

  public MapsController getMapsController() {
    return mapsController;
  }

  public double getScale() {
    return mapsController.getScale();
  }

  public void setVisibilityAll(boolean b) {
    for (LocationNode node : nodes.values()) {
      node.getButton().setVisible(b);
    }
  }

  public void setVisibilityOfType(String type, Boolean b) {
    for (LocationNode node : nodes.values()) {
      if (node.getNodeType().equals(type)) {
        node.getButton().setVisible(b);
      }
    }
  }
}
