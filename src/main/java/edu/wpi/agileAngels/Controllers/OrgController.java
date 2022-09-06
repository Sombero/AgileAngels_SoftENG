package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.Employee;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class OrgController implements Initializable {

  AppController appController = AppController.getInstance();
  EmployeeManager employeeManager = EmployeeManager.getInstance();

  @FXML AnchorPane anchor;
  @FXML HBox boss, coworkers, kiddos;

  public void initialize(URL location, ResourceBundle resources) {
    createChart(appController.getCurrentUser());
  }

  void createChart(Employee employee) {
    boss.getChildren().clear();
    coworkers.getChildren().clear();
    kiddos.getChildren().clear();

    Button bossButton = new Button(employee.getSupervisor().getName());
    bossButton.setPrefSize(100, 100);
    bossButton.setOnAction(
        event -> {
          if (employee.getSupervisor().getSupervisor() == null) {
            createChart(employee);
          } else {
            createChart(employee.getSupervisor());
          }
        });
    boss.getChildren().add(bossButton);

    for (Employee e : employee.getSupervisor().getSupervisees()) {
      Button person = new Button(e.getName());
      person.setPrefSize(100, 100);
      if (e.equals(employee)) {
        setButtonColor(person);
      }
      person.setOnAction(
          event -> {
            createChart(employeeManager.getEmployee(person.getText()));
          });
      coworkers.getChildren().add(person);
    }

    for (Employee e : employee.getSupervisees()) {
      Button kiddo = new Button(e.getName());
      kiddo.setPrefSize(100, 100);
      kiddo.setOnAction(
          event -> {
            createChart(employeeManager.getEmployee(kiddo.getText()));
          });
      kiddos.getChildren().add(kiddo);
    }
    setColor(appController.color);
  }

  public void setColor(String color) {
    if (color.equals("green")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestGreenTest.css");
    } else if (color.equals("red")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestRedTest.css");

    } else if (color.equals("blue")) {
      anchor.getStylesheets().removeAll();
      anchor.getStylesheets().add("/edu/wpi/agileAngels/views/stylesheets/styleRequest.css");
    }
  }

  // to handle new button objects to abide to color scheme
  public void setButtonColor(Button button) {
    if (appController.color.equals("blue")) {
      button.setStyle("-fx-background-color: #49A3AE");
    } else if (appController.color.equals("green")) {
      button.setStyle("-fx-background-color: #50ae49");
    } else if (appController.color.equals("red")) {
      button.setStyle("-fx-background-color: #ae4949");
    } else if (appController.color.equals("purple")) {
      button.setStyle("-fx-background-color: #9149ae");
    } else if (appController.color.equals("yellow")) {
      button.setStyle("-fx-background-color: #abae49");
    }
  }
}
