package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MaintenanceController implements Initializable, PropertyChangeListener {

  @FXML AnchorPane anchor;
  @FXML VBox popOut;
  @FXML HBox tableHBox;
  @FXML MenuButton mainLocation, mainEmployee, mainStatus, employeeFilter, statusFilter;
  @FXML Button modifyButton, cancelRequest, submitRequest, clearRequest, deleteRequest;
  @FXML TableView mainTable;
  @FXML
  private TableColumn nameColumn, locationColumn, employeeColumn, statusColumn, descriptionColumn;
  @FXML TextField mainDescription, employeeFilterField, statusFilterField;
  @FXML Label notStartedNumber, inProgressNumber, completedNumber, mainIDLabel;

  // DAOs, HashMaps, and Lists required for functionality
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private EmployeeManager empDAO = EmployeeManager.getInstance();
  private RequestDAOImpl mainRequestImpl = RequestDAOImpl.getInstance("MaintenanceRequest");
  private HashMap<String, Location> locationsHash = locDAO.getAllLocations();
  private ArrayList<Location> locationsList = new ArrayList<>(locationsHash.values());
  private HashMap<String, Employee> employeeHash = empDAO.getAllEmployees();
  private static ObservableList<Request> maintenanceData = FXCollections.observableArrayList();
  HashMap<String, String> locationIDsByLongName = new HashMap<>();

  private int statusNotStarted, statusInProgress, statusComplete;

  private AppController appController = AppController.getInstance();

  public MaintenanceController() throws SQLException {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appController.addPropertyChangeListener(this);
    hidePopout();
    statusNotStarted = 0;
    statusInProgress = 0;
    statusComplete = 0;

    for (Location loc : locationsHash.values()) {
      locationIDsByLongName.put(loc.getLongName(), loc.getNodeID());
    }

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    maintenanceData.clear();
    // Populates the table from UI list
    if (maintenanceData.isEmpty()) {
      for (Map.Entry<String, Request> entry : mainRequestImpl.getAllRequests().entrySet()) {
        Request req = entry.getValue();
        maintenanceData.add(req);
      }
    }
    dashboardLoad();
    mainTable.setItems(maintenanceData);

    // Populates locations dropdown
    for (Location loc : locationsList) {
      MenuItem item = new MenuItem(loc.getLongName());
      item.setOnAction(this::mainLocationMenu);
      mainLocation.getItems().add(item);
    }

    // Populates employees dropdown
    for (Map.Entry<String, Employee> entry : employeeHash.entrySet()) {
      Employee emp = entry.getValue();
      MenuItem item = new MenuItem(emp.getName());
      item.setOnAction(this::mainEmployeeMenu);
      mainEmployee.getItems().add(item);
    }
    clear();
    setColor(appController.color);
  }

  public void hidePopout() {
    try {
      tableHBox.getChildren().remove(popOut);
    } catch (NullPointerException e) {

    }
  }

  public void showPopout() {
    if (tableHBox.getChildren().get(0) != popOut) {
      tableHBox.getChildren().add(0, popOut);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
    appController.displayAlert();
  }

  void updateFilters() {
    employeeFilter.getItems().clear();
    for (Request r : maintenanceData) {
      CheckMenuItem emp = new CheckMenuItem(r.getEmployee().getName());
      emp.setSelected(true);
      emp.setOnAction(
          (ActionEvent event) -> {
            submitFilter();
          });
      employeeFilter.getItems().add(emp);
    }
  }

  @FXML
  void submitFilter() {
    ObservableList<Request> employeeFilteredList = FXCollections.observableArrayList();
    ObservableList<Request> statusFilterdList = FXCollections.observableArrayList();

    for (MenuItem menuItem : employeeFilter.getItems()) {
      if (((CheckMenuItem) menuItem).isSelected()) {
        for (Request r : maintenanceData) {
          if (((CheckMenuItem) menuItem).getText().equals(r.getEmployee().getName())) {
            employeeFilteredList.add(r);
          }
        }
      }
    }
    for (MenuItem menuItem : statusFilter.getItems()) {
      if (((CheckMenuItem) menuItem).isSelected()) {
        for (Request r : employeeFilteredList) {
          if (((CheckMenuItem) menuItem).getText().equals(r.getStatus())) {
            statusFilterdList.add(r);
          }
        }
      }
    }
    mainTable.setItems(statusFilterdList);
  }

  /** Puts all of the requests back on the table, "clearing the requests." */
  @FXML
  public void clearFilters() {
    // Puts everything back on table.
    for (MenuItem e : employeeFilter.getItems()) {
      ((CheckMenuItem) e).setSelected(true);
    }
    for (MenuItem e : statusFilter.getItems()) {
      ((CheckMenuItem) e).setSelected(true);
    }
    mainTable.setItems(maintenanceData);
  }

  @FXML
  public void modifyRequest(ActionEvent event) {
    showPopout();
  }

  @FXML
  public void submit(ActionEvent event) {
    String loc = locationIDsByLongName.get(mainLocation.getText());
    String emp = mainEmployee.getText();
    String stat = mainStatus.getText();
    String desc = mainDescription.getText();

    // Adding
    if (mainIDLabel.getText().equals("New Request")) {
      Request req =
          new Request(
              "",
              employeeHash.get(emp),
              locationsHash.get(loc),
              "N/A",
              "Not Started",
              desc,
              "N/A",
              "N/A");
      maintenanceData.add(req);
      mainRequestImpl.addRequest(req);
      updateDashAdding(req.getStatus());
    } else { // Editing
      Request req = mainRequestImpl.getAllRequests().get(mainIDLabel.getText());
      if (!req.getLocation().getNodeID().equals(loc)) {
        Location newLoc = locationsHash.get(loc);
        mainRequestImpl.updateLocation(req, newLoc);
      }
      if (!req.getEmployee().getName().equals(emp)) {
        mainRequestImpl.updateEmployeeName(req, emp);
      }
      if (!req.getStatus().equals(stat)) {
        updateDashAdding(stat);
        updateDashSubtracting(req.getStatus());
        mainRequestImpl.updateStatus(req, stat);
      }
      if (!req.getDescription().equals(desc)) {
        mainRequestImpl.updateDescription(req, desc);
      }

      for (int i = 0; i < maintenanceData.size(); i++) {
        if (maintenanceData.get(i).getName().equals(req.getName())) {
          maintenanceData.set(i, req);
        }
      }
    }
    clear();
    hidePopout();
  }

  @FXML
  public void cancel(ActionEvent event) {
    clear();
    hidePopout();
  }

  @FXML
  public void delete(ActionEvent event) {

    try {
      String id = ((Request) mainTable.getSelectionModel().getSelectedItem()).getName();
      updateDashSubtracting(mainRequestImpl.getAllRequests().get(id).getStatus());
      // removes the request from the table and dropdown
      for (int i = 0; i < maintenanceData.size(); i++) {
        if (maintenanceData.get(i).getName().equals(id)) {
          maintenanceData.remove(i);
          // labID.getItems().remove(i);
        }
      }
      // delete from hash map and database table
      mainRequestImpl.deleteRequest(mainRequestImpl.getAllRequests().get(id));

    } catch (NullPointerException e) {
      mainTable.getSelectionModel().clearSelection();
    }
    clear();
    hidePopout();
  }

  @FXML
  public void clear() {
    mainIDLabel.setText("ID");
    mainLocation.setText("Location");
    mainEmployee.setText("Employee");
    mainStatus.setText("Status");
    mainDescription.setText("");
    mainDescription.setPromptText("Description");
    updateFilters();
  }

  @FXML
  public void mainLocationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    mainLocation.setText(button.getText());
  }

  @FXML
  public void mainEmployeeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    mainEmployee.setText(button.getText());
  }

  @FXML
  public void mainStatusMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    mainStatus.setText(button.getText());
  }

  /**
   * This is the cleaner version of Justin's dashboard code. Note that it may need a for loop as
   * shown on line 83/84 if used elsewhere. Note: Unlikely.
   */
  @FXML
  private void dashboardLoad() {
    if (notStartedNumber.getText().equals("-")
        && inProgressNumber.getText().equals("-")
        && completedNumber.getText().equals("-")) {

      Iterator var3 = mainRequestImpl.getAllRequests().entrySet().iterator();
      while (var3.hasNext()) {
        Map.Entry<String, Request> entry = (Map.Entry) var3.next();
        Request object = (Request) entry.getValue();
        if (entry.getValue().getStatus().equals("inProgress")
            || entry.getValue().getStatus().equals("In Progress")) {
          statusInProgress++;
        }
        if (entry.getValue().getStatus().equals("notStarted")
            || entry.getValue().getStatus().equals("Not Started")) {
          statusNotStarted++;
        }
        if (entry.getValue().getStatus().equals("Complete")
            || entry.getValue().getStatus().equals("complete")) {
          statusComplete++;
        }
      }
      setDashboard(statusNotStarted, statusInProgress, statusComplete);
    }
  }

  /**
   * Will set the dashboard's numbers to the certain types of statuses.
   *
   * @param notStarted Requests not started
   * @param inProgress Requests in progress
   * @param complete Requests completed
   */
  @FXML
  private void setDashboard(int notStarted, int inProgress, int complete) {
    String notStart = Integer.toString(notStarted);
    String inProg = Integer.toString(inProgress);
    String comp = Integer.toString(complete);
    notStartedNumber.setText(notStart);
    inProgressNumber.setText(inProg);
    completedNumber.setText(comp);
  }

  /** Populates fields once a node id is chosen when editing an existing request. */
  private void populate() {
    showPopout();
    Request req = ((Request) mainTable.getSelectionModel().getSelectedItem());
    mainIDLabel.setText(req.getName());
    mainLocation.setText(req.getLocation().getLongName());
    mainEmployee.setText(req.getEmployee().getName());
    mainStatus.setText(req.getStatus());
    mainDescription.setText(req.getDescription());
  }

  public void menuItemSelected(ActionEvent actionEvent) {}

  private void updateDashAdding(String status) {
    if (status.equals("not started")
        || status.equals("Not Started")
        || status.equals("notStarted")) {
      statusNotStarted++;
    }
    if (status.equals("in progress")
        || status.equals("In Progress")
        || status.equals("inProgress")) {
      statusInProgress++;
    }
    if (status.equals("complete") || status.equals("Complete")) {
      statusComplete++;
    }
    setDashboard(statusNotStarted, statusInProgress, statusComplete);
  }

  private void updateDashSubtracting(String status) {
    if (status.equals("not started")
        || status.equals("Not Started")
        || status.equals("notStarted")) {
      statusNotStarted--;
    }
    if (status.equals("in progress")
        || status.equals("In Progress")
        || status.equals("inProgress")) {
      statusInProgress--;
    }
    if (status.equals("complete") || status.equals("Complete")) {
      statusComplete--;
    }
    setDashboard(statusNotStarted, statusInProgress, statusComplete);
  }

  @FXML
  public void loadRequest(MouseEvent mouseEvent) {
    try {
      if (mouseEvent.getButton() == MouseButton.PRIMARY) {
        populate();
        deleteRequest.setVisible(true);
        mainStatus.setVisible(true);
      }
    } catch (NullPointerException e) {
      hidePopout();
    }
  }

  public void newRequest(ActionEvent event) {
    deleteRequest.setVisible(false);
    mainStatus.setVisible(false);
    showPopout();
    clear();
    mainIDLabel.setText("New Request");
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

    } else if (color.equals("purple")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestPurpleTest.css");
    } else if (color.equals("yellow")) {
      anchor.getStylesheets().removeAll();
      anchor
          .getStylesheets()
          .add("/edu/wpi/agileAngels/views/stylesheets/ColorSchemes/styleRequestYellowTest.css");
    }
  }
}
