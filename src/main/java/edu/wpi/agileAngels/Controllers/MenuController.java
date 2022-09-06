package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.DBconnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MenuController implements Initializable {

  private @FXML Button back,
      close,
      serviceRequest,
      labRequest,
      map,
      dashboard,
      homeImage,
      emergency,
      test,
      pageTitle,
      aboutUs,
      covid;

  @FXML MenuButton userButton;

  private @FXML Pane menuPane;
  private @FXML AnchorPane anchor;
  @FXML ContextMenu contextMenu;

  AppController appController = AppController.getInstance();

  public MenuController() {
    appController.setCurrentMenuController(this);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    anchor.setPickOnBounds(false);

    setColor(appController.color);
  }

  @FXML
  private void closeApp() {
    DBconnection.shutdown();
    Platform.exit();
  }

  @FXML
  public void back() {
    appController.pageHistory.pop();
    appController.loadPage(appController.pageHistory.peek());
  }

  @FXML
  private void menuItem(ActionEvent event) {
    if (event.getSource() == serviceRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/serviceRequest-view.fxml");
    } else if (event.getSource() == emergency) {
      appController.loadPage("/edu/wpi/agileAngels/views/emergency-view.fxml");
    } else if (event.getSource() == dashboard) {
      appController.loadPage("/edu/wpi/agileAngels/views/NEWdashboard.fxml");
    } else if (event.getSource() == aboutUs) {
      appController.loadPage("/edu/wpi/agileAngels/views/aboutUs-view.fxml");
    } else if (event.getSource() == covid) {
      appController.loadPage("/edu/wpi/agileAngels/views/covid-view.fxml");
    }
  }

  public void changeTitle(String page) {
    pageTitle.setText(page);
  }

  public void hideButtons() {
    back.setVisible(false);
    close.setVisible(false);
  }

  public void profile(ActionEvent event) {
    appController.loadPage("/edu/wpi/agileAngels/views/profile-view.fxml");
  }

  public void goHome(ActionEvent event) {
    appController.loadPage("/edu/wpi/agileAngels/views/NEWdashboard.fxml");
  }

  public void setUserInitials(String initialsMaker) {
    userButton.setText(initialsMaker);
  }

  public void logout() {
    appController.setUser(null);
    appController.loadPage("/edu/wpi/agileAngels/views/login.fxml");
  }

  public void setColor(String newColor) {
    if (newColor.equals("blue")) {
      anchor.getStylesheets().removeAll();
      anchor.getStylesheets().add("/edu/wpi/agileAngels/views/stylesheets/style.css");
    } else if (newColor.equals("green")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleGreen.css");
    } else if (newColor.equals("red")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRed.css");
    } else if (newColor.equals("purple")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/stylePurple.css");
    } else if (newColor.equals("yellow")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleYellow.css");
    }
  }

  public void showContextMenu(ActionEvent event) {}
}
