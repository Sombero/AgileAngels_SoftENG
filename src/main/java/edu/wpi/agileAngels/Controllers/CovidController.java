package edu.wpi.agileAngels.Controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class CovidController implements Initializable, PropertyChangeListener {

  @FXML Button covidButton, maskButton, vacButton, houseButton, distanceButton;
  @FXML Label maskDesc, vacDesc, houseDesc, distanceDesc;
  @FXML Pane infoText;

  FadeTransition fade = new FadeTransition();
  FadeTransition fade2 = new FadeTransition();

  @Override
  public void propertyChange(PropertyChangeEvent evt) {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    maskDesc.setVisible(false);
    vacDesc.setVisible(false);
    houseDesc.setVisible(false);
    distanceDesc.setVisible(false);
    infoText.setVisible(false);

    animateInfoText();
  }

  public void animateMaskText() {
    maskDesc.setVisible(true);
    fade.setDuration(Duration.millis(1300));
    fade.setFromValue(0);
    fade.setToValue(10);
    fade.setCycleCount(1);
    fade.setNode(maskDesc);
    fade.play();
  }

  public void animateVacText() {
    vacDesc.setVisible(true);
    fade.setDuration(Duration.millis(1300));
    fade.setFromValue(0);
    fade.setToValue(10);
    fade.setCycleCount(1);
    fade.setNode(vacDesc);
    fade.play();
  }

  public void animateHouseText() {
    houseDesc.setVisible(true);
    fade.setDuration(Duration.millis(1300));
    fade.setFromValue(0);
    fade.setToValue(10);
    fade.setCycleCount(1);
    fade.setNode(houseDesc);
    fade.play();
  }

  public void animateDistanceText() {
    distanceDesc.setVisible(true);
    fade.setDuration(Duration.millis(1300));
    fade.setFromValue(0);
    fade.setToValue(10);
    fade.setCycleCount(1);
    fade.setNode(distanceDesc);
    fade.play();
  }

  public void animateInfoText() {

    infoText.setVisible(true);
    fade2.setDuration(Duration.millis(1300));
    fade2.setFromValue(0);
    fade2.setToValue(10);
    fade2.setCycleCount(1);
    fade2.setNode(infoText);
    fade2.play();

    fade2.getOnFinished();
  }
}
