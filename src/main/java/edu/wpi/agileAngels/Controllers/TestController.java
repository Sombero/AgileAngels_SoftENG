package edu.wpi.agileAngels.Controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

// test button on the front end
public class TestController implements Initializable, PropertyChangeListener {

  @FXML Button button, button1, button2, button3, button4;
  @FXML Circle circle1, circle2, circle3;

  ArrayList<Circle> circles = new ArrayList<>();

  private double x;
  private double y;

  Circle closest = null;

  AppController appController = AppController.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appController.addPropertyChangeListener(this);

    button.setOnMouseDragged(
        (MouseEvent mouseEvent) -> {
          button.setLayoutX(mouseEvent.getSceneX());
          button.setLayoutY(mouseEvent.getSceneY());
        });

    circles.add(circle1);
    circles.add(circle2);
    circles.add(circle3);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
    appController.displayAlert();
  }

  public void buttonPreessed(MouseEvent mouseEvent) {}

  public void buttonRelease(MouseEvent mouseEvent) {

    double dist = 0;

    for (Circle circle : circles) {
      if ((dist(button, circle) < dist) || dist == 0) {
        closest = circle;
        dist = dist(button, circle);
      }
    }

    button.setLayoutX(closest.getLayoutX());
    button.setLayoutY(closest.getLayoutY());
  }

  double dist(Node node1, Node node2) {
    return Math.sqrt(
        Math.pow((node1.getLayoutX() - node2.getLayoutX()), 2)
            + Math.pow((node1.getLayoutY() - node2.getLayoutY()), 2));
  }

  public void switchConnection(ActionEvent event) {}

  public void hover(MouseEvent mouseEvent) {
    Button activeButton = (Button) mouseEvent.getSource();
    // activeButton.setViewOrder(-1000);
    activeButton.setPrefSize(activeButton.getWidth() + 20, activeButton.getHeight() + 20);
    activeButton.setLayoutX(activeButton.getLayoutX() - 10);
    activeButton.setLayoutY(activeButton.getLayoutY() - 10);
  }

  public void unhover(MouseEvent mouseEvent) {
    Button activeButton = (Button) mouseEvent.getSource();
    activeButton.setPrefSize(activeButton.getWidth() - 20, activeButton.getHeight() - 20);
    activeButton.setLayoutX(activeButton.getLayoutX() + 10);
    activeButton.setLayoutY(activeButton.getLayoutY() + 10);

    if (activeButton == button1) {
      // activeButton.setViewOrder(-2);
    } else if (activeButton == button2) {
      // activeButton.setViewOrder(-3);
    } else if (activeButton == button3) {
      // activeButton.setViewOrder(-4);
    } else if (activeButton == button4) {
      // activeButton.setViewOrder(-5);
    }
  }
}
