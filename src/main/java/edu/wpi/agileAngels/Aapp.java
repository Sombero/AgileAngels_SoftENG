package edu.wpi.agileAngels;

import edu.wpi.agileAngels.Controllers.AppController;
import edu.wpi.agileAngels.Database.Employee;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Aapp extends Application {

  AppController appController = AppController.getInstance();

  @Override
  public void init() {
    log.info("Starting Up");
  }

  public Adb adb;

  // Creates and displays default scene
  @Override
  public void start(Stage primaryStage) throws IOException, SQLException {
    adb = new Adb(); // ADB class
    adb.initialize();
    appController.setUser(new Employee("Login", "", "", 0, "", null, null, null, null));
    appController.init(primaryStage);
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
