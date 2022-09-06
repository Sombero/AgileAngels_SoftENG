package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PumpAlertController {

  @FXML Button requestButton;

  private AppController appController = AppController.getInstance();
  private MedEquipImpl equipDAO = MedEquipImpl.getInstance();
  private HashMap<String, MedicalEquip> equipHash = equipDAO.getAllMedicalEquipment();
  private ArrayList<MedicalEquip> equipList = new ArrayList<>(equipHash.values());
  private RequestDAOImpl requestDAO = RequestDAOImpl.getInstance("MedRequest");
  private LocationDAOImpl locationDAO = LocationDAOImpl.getInstance();
  private HashMap<String, Location> locationHash = locationDAO.getAllLocations();
  private EmployeeManager employeeDAO = EmployeeManager.getInstance();
  private HashMap<String, Location> employeeHash = employeeDAO.getAllEmployees();

  public PumpAlertController() throws SQLException {}

  @FXML
  public void makeRequests() {
    String floor = appController.getPumpFloor();
    ArrayList<MedicalEquip> dirtyPumps = new ArrayList<>();

    // get all dirty pumps
    for (MedicalEquip medEquip : equipList) {
      if (!medEquip.isClean()
          && medEquip.getType().equals("InfusionPump")
          && medEquip.getLocation().getFloor().equals(floor)) {
        dirtyPumps.add(medEquip);
      }
    }

    // create requests to clean all dirty pumps
    for (MedicalEquip medEquip : dirtyPumps) {
      Request medDevice =
          new Request(
              "",
              employeeDAO.getEmployee("James"),
              medEquip.getLocation(),
              "InfusionPump",
              "Not Started",
              "describe",
              "something",
              "Clean",
              medEquip);
      requestDAO.addRequest(medDevice);

      // set status of pump
      medEquip.setStatus("inUse");
    }

    // close the alert window
    closeWindow();
  }

  public void closeWindow() {
    Stage stage = (Stage) requestButton.getScene().getWindow();
    stage.close();
  }
}
