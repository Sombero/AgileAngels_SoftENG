package edu.wpi.agileAngels.Controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.agileAngels.Database.Location;
import edu.wpi.agileAngels.Database.Request;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;

public class RequestNode {

  private Request request;
  private Location location;
  private RequestNodeManager requestNodeManager;
  private JFXButton button = new JFXButton();

  double xOffset;
  double yOffset;
  double buttonX;
  double buttonY;
  Boolean dragged = false;

  Location closest;

  AppController appController = AppController.getInstance();

  public RequestNode(Request request, RequestNodeManager requestNodeManager) throws SQLException {
    this.request = request;
    this.requestNodeManager = requestNodeManager;
    this.location = request.getLocation();

    button.setLayoutX(getPaneXfromcoords(this.location.getXCoord()));
    button.setLayoutY(getPaneYfromcoords(this.location.getYCoord()));
    button.setText(String.valueOf(request.getName().charAt(0)));

    if (appController.getCurrentUser().getPermissionLevel() >= 2) {

      button.setOnMousePressed(
          (MouseEvent mouseEvent) -> {
            xOffset = (button.getLayoutX() - mouseEvent.getSceneX());
            yOffset = (button.getLayoutY() - mouseEvent.getSceneY());

            buttonX = button.getLayoutX();
            buttonY = button.getLayoutY();

            dragged = false;
          });

      button.setOnMouseDragged(
          (MouseEvent mouseEvent) -> {
            button.setStyle(
                "-fx-font-size: 12; -fx-background-color:  #49A3AE; -fx-background-radius: 0 15 15 15; -fx-text-alignment: left; -fx-text-fill: white");
            button.setPrefSize(30, 26);
            button.setAlignment(Pos.CENTER);
            button.setText(String.valueOf(request.getName().charAt(0)));
            button.setLayoutX(
                getPaneXfromcoords((requestNodeManager.getMapXCoordFromClick(mouseEvent))));
            button.setLayoutY(
                getPaneYfromcoords((requestNodeManager.getMapYCoordFromClick(mouseEvent))));
            dragged = true;
          });
      button.setOnMouseReleased(
          (MouseEvent mouseEvent) -> {
            if (dist(
                    buttonX,
                    getPaneXfromcoords(requestNodeManager.getMapXCoordFromClick(mouseEvent)),
                    buttonY,
                    getPaneYfromcoords(requestNodeManager.getMapYCoordFromClick(mouseEvent)))
                < 50) {
              button.setLayoutX(buttonX);
              button.setLayoutY(buttonY);
            } else {
              placeOnClosestNode(mouseEvent);
              requestNodeManager.setDraggedNodeCoords(mouseEvent);
            }
          });
    }

    button.setOnAction(
        (ActionEvent event2) -> {
          isClicked();
        });
    button.setPrefSize(30, 26);
    button.setStyle(
        "-fx-font-size: 12; -fx-background-radius: 0 15 15 15; -fx-background-color:  #49A3AE; -fx-text-fill: white");
    button
        .hoverProperty()
        .addListener(
            l -> {
              button.setPrefSize(150, 50);
              button.setStyle(
                  "-fx-font-size: 15; -fx-background-color: #3f8c96; -fx-background-radius: 0 25 25 25; -fx-text-alignment: left; -fx-text-fill: white");
              button.setAlignment(Pos.CENTER_LEFT);
              button.setText(request.getName());
              button.setViewOrder(-1000);
            });

    button.setOnMouseExited(
        l -> {
          button.setStyle(
              "-fx-font-size: 12; -fx-background-color: #49A3AE; -fx-background-radius: 0 15 15 15; -fx-text-alignment: left; -fx-text-fill: white");
          button.setPrefSize(30, 26);
          button.setAlignment(Pos.CENTER);
          button.setText(String.valueOf(request.getName().charAt(0)));
          button.setViewOrder(-100);
        });

    dragged = false;
  }

  public void placeOnClosestNode(MouseEvent mouseEvent) {

    double smallest = 0;

    for (Location location : requestNodeManager.getLocationsList()) {
      double dist =
          dist(
              requestNodeManager.getMapXCoordFromClick(mouseEvent),
              location.getXCoord(),
              requestNodeManager.getMapYCoordFromClick(mouseEvent),
              location.getYCoord());

      if (((dist < smallest) && (this.location.getFloor().equals(location.getFloor())))
          || smallest == 0) {
        smallest = dist;
        closest = location;
      }
    }

    button.setLayoutX(getPaneXfromcoords(closest.getXCoord()));
    button.setLayoutY(getPaneYfromcoords(closest.getYCoord()));
    requestNodeManager.editRequestLocation(this, closest);

    // requestNodeManager.updateLocation(this);
  }

  private double dist(double x1, double x2, double y1, double y2) {
    return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
  }

  public double getPaneXfromcoords(double x) {
    return ((x - requestNodeManager.getCroppedMapXOffset())
        / (requestNodeManager.getCroppedMapWidth() / requestNodeManager.getImagePaneWidth()));
  }

  public double getPaneYfromcoords(double y) {
    return ((y - requestNodeManager.getCroppedMapYOffset())
        / (requestNodeManager.getCroppedMapWidth() / requestNodeManager.getImagePaneWidth()));
  }

  public void resetLocation() {
    button.setLayoutX(getPaneXfromcoords(location.getXCoord()));
    button.setLayoutY(getPaneYfromcoords(location.getYCoord()));

    button.setText(String.valueOf(location.getNodeType().charAt(0)));
  }

  public void isClicked() {
    requestNodeManager.loadNode(this);
  }

  void setEmployee(String employee) {

    this.request.setEmployee(requestNodeManager.employeeHash.get(employee));
  }

  public String getRequestType() {
    return request.getName().substring(0, 3);
  }

  public Location getLocation() {
    return this.location;
  }

  public String getFloor() {
    return this.location.getFloor();
  }

  public Request getRequest() {
    return request;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getName() {
    return request.getName();
  }

  public String getEmployee() {
    return request.getEmployee().getName();
  }

  public String getStatus() {
    return request.getStatus();
  }

  public JFXButton getButton() {
    return button;
  }
}
