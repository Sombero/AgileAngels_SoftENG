package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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

public class MorgueController implements Initializable, PropertyChangeListener {
  AppController appController = AppController.getInstance();
  // @FXML private Button addButton;
  @FXML AnchorPane anchor;
  @FXML VBox popOut;
  @FXML HBox tableHBox;
  @FXML MenuButton morgueLocation, morgueEmployee, morgueStatus, employeeFilter, statusFilter;
  @FXML Button modifyButton, cancelRequest, submitRequest, clearRequest, deleteRequest;
  @FXML TableView morgueTable;
  @FXML
  private TableColumn nameColumn,
      availableColumn,
      typeColumn,
      locationColumn,
      employeeColumn,
      statusColumn,
      descriptionColumn;
  @FXML TextField morgueDescription, employeeFilterField, statusFilterField;
  @FXML Label notStartedNumber, inProgressNumber, completedNumber, morgueIDLabel;

  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private EmployeeManager empDAO = EmployeeManager.getInstance();
  private RequestDAOImpl MorguerequestImpl = RequestDAOImpl.getInstance("MorgueRequest");
  private static ObservableList<Request> morgueData = FXCollections.observableArrayList();
  HashMap<String, Location> locationsHash = locDAO.getAllLocations();
  ArrayList<Location> locationsList = new ArrayList<>(locationsHash.values());
  private HashMap<String, Employee> employeeHash = empDAO.getAllEmployees();
  private int statusNotStarted, statusInProgress, statusComplete;
  HashMap<String, String> locationIDsByLongName = new HashMap<>();

  public MorgueController() throws SQLException {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appController.addPropertyChangeListener(this);
    hidePopout();
    statusNotStarted = 0;
    statusInProgress = 0;
    statusComplete = 0;
    locDAO.getAllLocations();
    empDAO.getAllEmployees();

    for (Location loc : locationsHash.values()) {
      locationIDsByLongName.put(loc.getLongName(), loc.getNodeID());
    }

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("attribute2"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    availableColumn.setCellValueFactory(new PropertyValueFactory<>("attribute1"));

    morgueData.clear();
    for (Map.Entry<String, Request> entry : MorguerequestImpl.getAllRequests().entrySet()) {
      Request req = entry.getValue();
      morgueData.add(req);
    }

    dashboardLoad();
    morgueTable.setItems(morgueData);
    setColor(appController.color);

    morgueLocation.getItems().clear();
    for (Location loc : locationsList) {
      MenuItem item = new MenuItem(loc.getLongName());
      item.setOnAction(this::locationMenu);
      morgueLocation.getItems().add(item);
    }
    // Populates employees dropdown
    morgueEmployee.getItems().clear();
    for (Map.Entry<String, Employee> entry : employeeHash.entrySet()) {
      Employee emp = entry.getValue();
      MenuItem item = new MenuItem(emp.getName());
      item.setOnAction(this::employeeMenu);
      morgueEmployee.getItems().add(item);
    }
    clear();
  }

  void updateFilters() {
    employeeFilter.getItems().clear();
    ArrayList<String> list = new ArrayList<>();
    for (Request r : morgueData) {
      if (!list.contains(r.getEmployee().getName())) {
        CheckMenuItem emp = new CheckMenuItem(r.getEmployee().getName());
        emp.setSelected(true);
        emp.setOnAction(
            (ActionEvent event) -> {
              submitFilter();
            });
        employeeFilter.getItems().add(emp);
        list.add(r.getEmployee().getName());
      }
    }
    clearFilters();
  }

  @FXML
  void submitFilter() {
    ObservableList<Request> employeeFilteredList = FXCollections.observableArrayList();
    ObservableList<Request> statusFilterdList = FXCollections.observableArrayList();

    for (MenuItem menuItem : employeeFilter.getItems()) {
      if (((CheckMenuItem) menuItem).isSelected()) {
        for (Request r : morgueData) {
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
    morgueTable.setItems(statusFilterdList);
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
    morgueTable.setItems(morgueData);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
    appController.displayAlert();
  }

  @FXML
  public void submit(ActionEvent event) throws SQLException {
    String loc = locationIDsByLongName.get(morgueLocation.getText());
    String emp = morgueEmployee.getText();
    String stat = morgueStatus.getText();
    String desc = morgueDescription.getText();
    ZoneId z = ZoneId.of("America/Montreal");
    LocalDate today = LocalDate.now(z);
    LocalTime currentTime = LocalTime.now(z);
    String date = today.toString();
    String time = currentTime.toString().substring(0, 8);

    // Adding
    if (morgueIDLabel.getText().equals("New Request")) {
      Request req =
          new Request(
              "",
              employeeHash.get(emp),
              locationsHash.get(loc),
              "N/A",
              "Not Started",
              desc,
              date,
              time);

      MorguerequestImpl.addRequest(req);
      morgueData.add(req);
      morgueTable.setItems(morgueData);
      updateDashAdding(req.getStatus());

    } else { // Editing
      Request req = MorguerequestImpl.getAllRequests().get(morgueIDLabel.getText());
      if (!req.getLocation().getNodeID().equals(loc)) {
        Location newLoc = locationsHash.get(loc);
        MorguerequestImpl.updateLocation(req, newLoc);
      }
      if (!req.getEmployee().getName().equals(emp)) {
        MorguerequestImpl.updateEmployeeName(req, emp);
      }
      if (!req.getStatus().equals(stat)) {
        updateDashAdding(stat);
        updateDashSubtracting(req.getStatus());
        MorguerequestImpl.updateStatus(req, stat);
      }
      if (!req.getDescription().equals(desc)) {
        MorguerequestImpl.updateDescription(req, desc);
      }

      for (int i = 0; i < morgueData.size(); i++) {
        if (morgueData.get(i).getName().equals(req.getName())) {
          morgueData.set(i, req);
        }
      }
    }
    clear();
    hidePopout();
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

  @FXML
  public void clear() {
    morgueIDLabel.setText("ID");
    morgueLocation.setText("Location");
    morgueEmployee.setText("Employee");
    morgueStatus.setText("Status");
    morgueDescription.setText("");
    morgueDescription.setPromptText("Patient Name");

    updateFilters();
  }

  @FXML
  public void cancel(ActionEvent event) {
    clear();
    hidePopout();
  }

  @FXML
  public void delete(ActionEvent event) throws SQLException {

    String id = morgueIDLabel.getText();
    updateDashSubtracting(MorguerequestImpl.getAllRequests().get(id).getStatus());
    // removes the request from the table and dropdown
    // delete from hash map and database table
    MorguerequestImpl.deleteRequest(MorguerequestImpl.getAllRequests().get(id));
    clear();
    hidePopout();
  }

  @FXML
  public void locationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    morgueLocation.setText(button.getText());
  }

  @FXML
  public void employeeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    morgueEmployee.setText(button.getText());
  }

  @FXML
  public void statusMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    morgueStatus.setText(button.getText());
  }

  @FXML
  private void dashboardLoad() {
    if (notStartedNumber.getText().equals("-")
        && inProgressNumber.getText().equals("-")
        && completedNumber.getText().equals("-")) {

      Iterator var3 = MorguerequestImpl.getAllRequests().entrySet().iterator();
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

  @FXML
  private void setDashboard(int notStarted, int inProgress, int complete) {
    String notStart = Integer.toString(notStarted);
    String inProg = Integer.toString(inProgress);
    String comp = Integer.toString(complete);
    notStartedNumber.setText(notStart);
    inProgressNumber.setText(inProg);
    completedNumber.setText(comp);
  }

  private void populate() {
    showPopout();
    Request req = ((Request) morgueTable.getSelectionModel().getSelectedItem());
    morgueIDLabel.setText(req.getName());
    morgueLocation.setText(req.getLocation().getLongName());
    morgueEmployee.setText(req.getEmployee().getName());
    morgueStatus.setText(req.getStatus());
    morgueDescription.setText(req.getDescription());
  }

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

  public void loadRequest(MouseEvent mouseEvent) {
    try {
      if (mouseEvent.getButton() == MouseButton.PRIMARY) {
        populate();
        deleteRequest.setVisible(true);
        morgueStatus.setVisible(true);
      }
    } catch (NullPointerException e) {
      hidePopout();
    }
  }

  @FXML
  public void newRequest() {
    deleteRequest.setVisible(false);
    morgueStatus.setVisible(false);
    showPopout();
    clear();
    morgueIDLabel.setText("New Request");
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
