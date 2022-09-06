package edu.wpi.agileAngels.Controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class AboutUsController implements Initializable, PropertyChangeListener {

  @FXML
  Button basharButton,
      aadhyaButton,
      aaronButton,
      aliButton,
      danielButton,
      harmoniButton,
      jakobButton,
      joeButton,
      justinButton,
      taliaButton;
  @FXML
  Pane basharBubble,
      aahdyaBubble, // null
      aaronBubble,
      aliBubble,
      danielBubble,
      harmoniBubble,
      jakobBubble,
      joeBubble,
      justinBubble,
      taliaBubble;

  AppController appController = AppController.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appController.addPropertyChangeListener(this);

    basharBubble.setVisible(false);
    aahdyaBubble.setVisible(false);
    aaronBubble.setVisible(false);
    aliBubble.setVisible(false);
    danielBubble.setVisible(false);
    harmoniBubble.setVisible(false);
    jakobBubble.setVisible(false);
    joeBubble.setVisible(false);
    justinBubble.setVisible(false);
    taliaBubble.setVisible(false);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
  }

  public void showBasharText(ActionEvent actionEvent) {
    basharBubble.setVisible(true);
  }

  public void showAliText(ActionEvent actionEvent) {
    aliBubble.setVisible(true);
  }

  public void showTaliaText(ActionEvent actionEvent) {
    taliaBubble.setVisible(true);
  }

  public void showAadhyaText(ActionEvent actionEvent) {
    aahdyaBubble.setVisible(true);
  }

  public void showHarmoniText(ActionEvent actionEvent) {
    harmoniBubble.setVisible(true);
  }

  public void showJoeText(ActionEvent actionEvent) {
    joeBubble.setVisible(true);
  }

  public void showJakobText(ActionEvent actionEvent) {
    jakobBubble.setVisible(true);
  }

  public void showJustinText(ActionEvent actionEvent) {
    justinBubble.setVisible(true);
  }

  public void showDanielText(ActionEvent actionEvent) {
    danielBubble.setVisible(true);
  }

  public void showAaronText(ActionEvent actionEvent) {
    aaronBubble.setVisible(true);
  }
}
