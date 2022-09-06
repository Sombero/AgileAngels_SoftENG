package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.DBconnection;
import edu.wpi.agileAngels.Database.Location;
import edu.wpi.agileAngels.Database.LocationDAOImpl;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

// Page that displayed locations in a table
// Not really using this rn
// TODO implement this somewhere else
public class LocationController implements Initializable {
  /** Displays all locations in a table */
  @FXML
  private TableColumn nodeIDColumn,
      xCoordColumn,
      yCoordColumn,
      floorColumn,
      buildingColumn,
      nodeTypeColumn,
      longNameColumn,
      shortNameColumn;

  private ObservableList<Location> locationData = FXCollections.observableArrayList();
  private LocationDAOImpl locationDAO = LocationDAOImpl.getInstance();
  private Connection connection;

  @FXML private TableView locationTable;

  SimpleListProperty location;

  public LocationController() throws SQLException {}

  public void submitData() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    connection = DBconnection.getConnection();

    // Implement DAO here.

    HashMap<String, Location> data = locationDAO.getAllLocations();
    for (Map.Entry<String, Location> entry : data.entrySet()) {
      Location object = entry.getValue();
      locationData.add(object);
    }

    // no need to add them to the table since the FXMLLoader is ready doing that
    nodeIDColumn.setCellValueFactory(new PropertyValueFactory<>("NodeID"));
    nodeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
    xCoordColumn.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
    yCoordColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));
    floorColumn.setCellValueFactory(new PropertyValueFactory<>("building"));
    buildingColumn.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    longNameColumn.setCellValueFactory(new PropertyValueFactory<>("longName"));
    shortNameColumn.setCellValueFactory(new PropertyValueFactory<>("shortName"));
    locationTable.setItems(locationData);
  }
}
