package edu.wpi.agileAngels.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class LoginController implements Initializable {
  @FXML private TextField username;
  @FXML private Label invalid;
  @FXML private Button login;
  @FXML private PasswordField passwordBox;

  AppController appController = AppController.getInstance();

  private EmployeeManager employeeManager = EmployeeManager.getInstance();

  public LoginController() throws SQLException {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    employeeManager.addEmployee("wong", "wong", "Off Duty", 5);
    //    employeeManager.addEmployee("Admin", "Admin", "Off Duty", 4);
    //    employeeManager.addEmployee("Nurse", "Nurse", "Off Duty", 2);
    //    employeeManager.addEmployee("Justin", "Password", "L2", 2);
    //    employeeManager.addEmployee("Staff", "Staff", "Off Duty", 3);
    //    employeeManager.addEmployee("Jakob Sperry", "", "Off Duty", 1);
  }

  /**
   * Is the login textfields that take in a username and password and checks to see if it matches
   * with anything from the database / hashmap.
   *
   * @throws IOException
   */
  @FXML
  private void login() throws IOException {

    if (employeeManager.getName(username.getText())
        && passwordBox.getText().equals(employeeManager.getPassword(username.getText()))) {
      appController.setUser(employeeManager.getEmployee(username.getText()));
      appController.loadPage("/edu/wpi/agileAngels/views/NEWdashboard.fxml");

    } else {
      invalid.setTextFill(Color.rgb(220, 80, 80));
      invalid.setText("Invalid username or password.");
      passwordBox.clear();
    }
  }

  /**
   * Duplicate enter code, but will work fine for now but won't be in diagram.
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void loginWithEnter(KeyEvent event) throws IOException {
    if (event.getCode().equals(KeyCode.ENTER)) {
      login();
    }
  }

  /**
   * Creates the initial(s) of the given string. If only 1 name is given, 1 intial will return. If
   * 2+ names, 2 initials. Has no defense against illegal characters...
   *
   * @return The initial(s) of the given string
   */
  public void closeApp(ActionEvent event) {
    appController.closeApp();
  }

  public void clearPage(ActionEvent event) throws IOException {
    appController.clearPage();
  }
}

  /*
  Maybe make a way to turn password text into asterisks as it's being typed, and a way to turn it back when "show pw" is clicked.
   */
