package edu.wpi.agileAngels.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EmergencyController {

  @FXML Button helpButton;
  @FXML Label helpMessage;

  @FXML
  public void displayHelpMessage() {
    helpMessage.setText("Help is on the way to your location.");
  }
}
