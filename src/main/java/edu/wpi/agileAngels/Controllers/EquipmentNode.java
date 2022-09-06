package edu.wpi.agileAngels.Controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.agileAngels.Database.Location;
import edu.wpi.agileAngels.Database.LocationDAOImpl;
import edu.wpi.agileAngels.Database.MedicalEquip;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;

public class EquipmentNode {

  private MedicalEquip medEquip;
  private Location location;
  private EquipmentNodeManager equipmentNodeManager;
  private JFXButton button = new JFXButton();
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  HashMap<String, Location> locationsHash = locDAO.getAllLocations();

  double xOffset;
  double yOffset;
  double buttonX;
  double buttonY;

  Location closest;

  public EquipmentNode(MedicalEquip medEquip, EquipmentNodeManager equipmentNodeManager)
      throws SQLException {
    this.medEquip = medEquip;
    this.equipmentNodeManager = equipmentNodeManager;
    this.location = medEquip.getLocation();

    button.setLayoutX(getPaneXfromcoords(this.location.getXCoord()));
    button.setLayoutY(getPaneYfromcoords(this.location.getYCoord()));
    button.setText(String.valueOf(medEquip.getType().charAt(0)));
    button.setOnAction(
        (ActionEvent event2) -> {
          isClicked();
        });

    button.setStyle("-fx-background-color: #b3b3b3;-fx-font-size: 8");
    button.setPrefSize(30, 26);

    button.setStyle(
        "-fx-font-size: 12; -fx-background-radius: 0 15 15 15; -fx-background-color: #b3b3b3; -fx-text-fill: white");

    button
        .hoverProperty()
        .addListener(
            l -> {
              button.setPrefSize(250, 50);
              button.setStyle(
                  "-fx-font-size: 15; -fx-background-color: #b3b3b3; -fx-background-radius: 0 25 25 25; -fx-text-alignment: left; -fx-text-fill: white");
              button.setAlignment(Pos.CENTER_LEFT);
              button.setText(getMedEquip().getType());
              button.setViewOrder(-1000);
            });

    button.setOnMouseExited(
        l -> {
          button.setPrefSize(30, 28);
          button.setStyle(
              "-fx-font-size: 12; -fx-background-color: #b3b3b3 ;-fx-background-radius: 0 15 15 15; -fx-text-alignment: left; -fx-text-fill: white");
          button.setAlignment(Pos.CENTER);
          button.setText(String.valueOf(getMedEquip().getType().charAt(0)));
          button.setViewOrder(-100);
        });
  }

  public void placeOnClosestNode(MouseEvent mouseEvent) {

    double smallest = 0;

    for (Location location : equipmentNodeManager.getLocationsList()) {
      double dist =
          dist(
              equipmentNodeManager.getMapXCoordFromClick(mouseEvent),
              location.getXCoord(),
              equipmentNodeManager.getMapYCoordFromClick(mouseEvent),
              location.getYCoord());

      if (((dist < smallest) && (this.location.getFloor().equals(location.getFloor())))
          || smallest == 0) {
        smallest = dist;
        closest = location;
      }
    }
    button.setLayoutX(getPaneXfromcoords(closest.getXCoord()));
    button.setLayoutY(getPaneYfromcoords(closest.getYCoord()));
    equipmentNodeManager.editEquipmentLocation(this, closest);
  }

  public void isClicked() {
    equipmentNodeManager.loadNode(this);
  }

  private double dist(double x1, double x2, double y1, double y2) {
    return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
  }

  public double getPaneXfromcoords(double x) {
    return ((x - equipmentNodeManager.getCroppedMapXOffset())
        / (equipmentNodeManager.getCroppedMapWidth() / equipmentNodeManager.getImagePaneWidth()));
  }

  public double getPaneYfromcoords(double y) {
    return ((y - equipmentNodeManager.getCroppedMapYOffset())
        / (equipmentNodeManager.getCroppedMapWidth() / equipmentNodeManager.getImagePaneWidth()));
  }

  public void resetLocation() {
    double x = getPaneXfromcoords(this.location.getXCoord());
    double y = getPaneYfromcoords(this.location.getYCoord());
    button.setLayoutX(x);
    button.setLayoutY(y);
  }

  public MedicalEquip getMedEquip() {
    return medEquip;
  }

  public JFXButton getButton() {
    return this.button;
  }

  public Location getLocation() {
    return this.location;
  }

  public String getFloor() {
    return this.location.getFloor();
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getID() {
    String str = medEquip.getID() + " " + medEquip.getType();
    return str;
  }

  public String getClean() {
    String str = "Dirty";
    if (medEquip.isClean()) {
      str = "Clean";
    }
    return str;
  }

  public String getStatus() {
    String status = medEquip.getStatus();
    if (status.equals("available")) {
      status = "Available";
    } else if (status.equals("inUse")) {
      status = "In Use";
    }
    return status;
  }

  public void updateLocation() {

    Location location = this.getMedEquip().getLocation();
    this.location = location;
    this.resetLocation();
  }
}
