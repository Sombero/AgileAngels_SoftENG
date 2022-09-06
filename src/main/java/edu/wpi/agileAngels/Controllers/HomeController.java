package edu.wpi.agileAngels.Controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class HomeController implements Initializable, PropertyChangeListener {

  @FXML Button serviceButton, mapButton;

  AppController appController = AppController.getInstance();

  public HomeController() throws SQLException {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appController.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
  }

  @FXML
  private void homeButton(ActionEvent event) {
    if (event.getSource() == serviceButton) {
      appController.loadPage("/edu/wpi/agileAngels/views/serviceRequest-view.fxml");
      // appController.loadPage("/edu/wpi/agileAngels/views/aboutUs-view.fxml");
    } else if (event.getSource() == mapButton) {
      appController.loadPage("/edu/wpi/agileAngels/views/NEWdashboard.fxml");
    }
  }

  @FXML
  public void goHome(ActionEvent event) {}

  @FXML
  public void menuItem(ActionEvent event) {}

  @FXML
  public void profile(ActionEvent event) {}

  @FXML
  public void closeApp(ActionEvent event) {}
}
