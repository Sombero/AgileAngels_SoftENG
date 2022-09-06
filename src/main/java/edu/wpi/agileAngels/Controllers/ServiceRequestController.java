package edu.wpi.agileAngels.Controllers;

import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.agileAngels.Database.DBconnection;
import edu.wpi.agileAngels.Database.RequestDAOImpl;
import edu.wpi.agileAngels.MenuSpeech;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

// brings you to pages
public class ServiceRequestController implements Initializable, PropertyChangeListener {
  RequestDAOImpl req = RequestDAOImpl.getInstance("AllRequests");
  @FXML AnchorPane anchor;

  @FXML
  Button equipmentRequest,
      labRequest,
      saniRequest,
      mealRequest,
      giftRequest,
      laundryRequest,
      maintenanceRequest,
      morgueRequest,
      patientTransportRequest,
      next,
      saveButton,
      uploadButton,
      MedAid;
  @FXML private JFXToggleButton toggleButton, clientToggle;

  // These are/will be the hidden labels for the toggleable switch.
  @FXML
  private Label erText,
      lrText,
      mrText,
      srText,
      mealText,
      morText,
      mgbText,
      grText,
      launText,
      ptText;

  AppController appController = AppController.getInstance();

  public ServiceRequestController() throws SQLException {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appController.addPropertyChangeListener(this);
    updateToggle();

    if (appController.getCurrentUser().getPermissionLevel() == 1) {
      equipmentRequest.setDisable(true);
      labRequest.setDisable(true);
      morgueRequest.setDisable(true);
      morgueRequest.setDisable(true);
      patientTransportRequest.setDisable(true);
      saveButton.setDisable(true);
      uploadButton.setDisable(true);
    }
    setColor(appController.color);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
    appController.displayAlert();
  }

  /**
   * showCreators() will make invisible labels displaying people's names visible again. And turn
   * them off. "I don't know why, I don't want to know why" - TF2 source code. btw don't touch this,
   * or I'll throw hands. ~<3
   */
  public void showCreators(ActionEvent event) {
    if (toggleButton.isSelected()) {
      // Will turn off names.
      erText.setText("Equip. Request: Harmoni");
      lrText.setText("Lab Request: Justin");
      mrText.setText("Main. Request: Talia");
      srText.setText("San. Request: Daniel");
      mealText.setText("Meal Request: Jakob");
      morText.setText("Morgue Request: Aaron");
      grText.setText("Gift Request: Bashar");
      launText.setText("Laundry Request: Bashar");
      ptText.setText("Patient Transport: Ali");
    } else {
      // Will turn on the names.
      erText.setText("Equipment Request");
      lrText.setText("Lab Request");
      mrText.setText("Maintenance Request");
      srText.setText("Sanitation Request");
      mealText.setText("Meal Request");
      morText.setText("Morgue Request");
      grText.setText("Gift Request");
      launText.setText("Laundry Request");
      ptText.setText("Patient Transport");
    }
  }

  /**
   * This will bring you to the various request pages when a button is clicked.
   *
   * @param event
   * @throws IOException
   */
  public void requestButton(ActionEvent event) {
    if (event.getSource() == equipmentRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/equipment-view.fxml");
    } else if (event.getSource() == labRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/lab-view.fxml");
    } else if (event.getSource() == saniRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/sanitation-view.fxml");
    } else if (event.getSource() == mealRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/mealRequest-view.fxml");
    } else if (event.getSource() == giftRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/gifts-view.fxml");
    } else if (event.getSource() == maintenanceRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/maintenance-view.fxml");
    } else if (event.getSource() == laundryRequest) {
      // todo this was the mass babes line I edited if your wondeiring or need to change it
      appController.loadPage("/edu/wpi/agileAngels/views/laundryRequest-view.fxml");
    } else if (event.getSource() == morgueRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/morgue-view.fxml");
    } else if (event.getSource() == patientTransportRequest) {
      appController.loadPage("/edu/wpi/agileAngels/views/patientTransport-view.fxml");
    } else if (event.getSource() == MedAid) {
      appController.loadPage("/edu/wpi/agileAngels/views/medAid-view.fxml");
    } else if (event.getSource() == next) {
      appController.loadPage("/edu/wpi/agileAngels/views/apiLanding-view.fxml");
    }

    /*else if (event.getSource() == testButton) {
      appController.loadPage("/edu/wpi/agileAngels/views/test-view.fxml");
    }*/
  }

  /**
   * uses the toggleStatus to update the toggle from client to embedded.
   *
   * @param event
   */
  @FXML
  private void toggleStatus(ActionEvent event) {
    DBconnection.switchConnection();
    if (clientToggle.isSelected()) {
      appController.setEmbeddedON(true);

    } else {
      appController.setEmbeddedON(false);
    }
  }

  /** This will check when initalizing what status the toggle was on. */
  private void updateToggle() {
    if (appController.isEmbeddedON()) {
      clientToggle.setSelected(appController.isEmbeddedON());

      System.out.println(appController.isEmbeddedON());
    }
  }

  @FXML
  public void saveToCSV() {
    // do things here
    req.outputCSVFile();
  }

  @FXML
  public void uploadToCSV() {
    // do things here
    System.out.println("Upload to CSV");
    req.csvRead();
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

  public void voice(ActionEvent actionEvent) {
    MenuSpeech thread = null;
    try {
      thread = new MenuSpeech();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String[] args = new String[50];
    try {
      thread.main(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
