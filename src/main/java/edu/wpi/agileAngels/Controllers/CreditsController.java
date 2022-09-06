package edu.wpi.agileAngels.Controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class CreditsController implements Initializable, PropertyChangeListener {

  @FXML Button apiButton2, apiButton1, creditsButton, mgbButton;

  AppController appController = AppController.getInstance();

  @Override
  public void propertyChange(PropertyChangeEvent evt) {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}
