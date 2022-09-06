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

public class PatientTransportController implements Initializable, PropertyChangeListener {

  @FXML AnchorPane anchor;
  @FXML VBox popOut;
  @FXML HBox tableHBox;
  @FXML
  MenuButton transportLocation,
      transportEmployee,
      transportStatus,
      transportType,
      transportDestination,
      employeeFilter,
      statusFilter;
  @FXML Button cancelRequest, submitRequest, clearRequest, deleteRequest;
  @FXML TableView transportTable;
  @FXML
  private TableColumn nameColumn,
      destinationColumn,
      typeColumn,
      locationColumn,
      employeeColumn,
      statusColumn,
      descriptionColumn;
  @FXML TextField transportDescription, employeeFilterField, statusFilterField;
  @FXML Label notStartedNumber, inProgressNumber, completedNumber, transportIDLabel;

  // DAOs, HashMaps, and Lists required for functionality

  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private EmployeeManager empDAO = EmployeeManager.getInstance();
  private RequestDAOImpl transportDAOImpl =
      RequestDAOImpl.getInstance("TransportRequest"); // looks sus
  private HashMap<String, Location> locationsHash = locDAO.getAllLocations();
  private ArrayList<Location> locationsList = new ArrayList<>(locationsHash.values());
  private HashMap<String, Employee> employeeHash = empDAO.getAllEmployees();
  private static ObservableList<Request> transportData = FXCollections.observableArrayList();
  private int statusNotStarted, statusInProgress, statusComplete;
  private AppController appController = AppController.getInstance();
  HashMap<String, String> locationIDsByLongName = new HashMap<>();
  RequestDAOImpl allReqDAO = RequestDAOImpl.getInstance("AllRequests");

  public PatientTransportController() throws SQLException {}

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
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    destinationColumn.setCellValueFactory(
        new PropertyValueFactory<>("attribute2")); // location 2 looks sus again

    // Populates the table from UI list
    transportData.clear();
    for (Map.Entry<String, Request> entry : transportDAOImpl.getAllRequests().entrySet()) {
      Request req = entry.getValue();
      transportData.add(req);
    }

    dashboardLoad();
    transportTable.setItems(transportData);

    // Populates locations dropdown
    for (Location loc : locationsList) {
      MenuItem item = new MenuItem(loc.getLongName());
      item.setOnAction(this::mainLocationMenu);
      transportLocation.getItems().add(item);
    }
    // Populates destinations dropdown
    for (Location dest : locationsList) {
      MenuItem item = new MenuItem(dest.getLongName());
      item.setOnAction(this::mainDestinationMenu);
      transportDestination.getItems().add(item);
    }

    // Populates employees dropdown
    for (Map.Entry<String, Employee> entry : employeeHash.entrySet()) {
      Employee emp = entry.getValue();
      MenuItem item = new MenuItem(emp.getName());
      item.setOnAction(this::mainEmployeeMenu);
      transportEmployee.getItems().add(item);
    }
    setColor(appController.color);
    clear();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
    appController.displayAlert();
  }

  public void hidePopout() {
    try {
      tableHBox.getChildren().remove(popOut);
    } catch (NullPointerException e) {

    }
  }

  void updateFilters() {
    employeeFilter.getItems().clear();
    ArrayList<String> list = new ArrayList<>();
    for (Request r : transportData) {
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
        for (Request r : transportData) {
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
    transportTable.setItems(statusFilterdList);
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
    transportTable.setItems(transportData);
  }

  public void showPopout() {
    if (tableHBox.getChildren().get(0) != popOut) {
      tableHBox.getChildren().add(0, popOut);
    }
  }

  @FXML
  public void submit(ActionEvent event) {
    String loc = locationIDsByLongName.get(transportLocation.getText());
    String destLongName = transportDestination.getText();
    String destID = locationIDsByLongName.get(destLongName);
    String emp = transportEmployee.getText();
    String stat = transportStatus.getText();
    String desc = transportDescription.getText();
    String type = transportType.getText();
    // Adding
    if (transportIDLabel.getText().equals("New Request")) {
      Request req =
          new Request(
              "",
              employeeHash.get(emp),
              locationsHash.get(loc),
              type,
              "Not Started",
              desc,
              "N/A",
              destLongName);
      transportData.add(req);
      transportDAOImpl.addRequest(req);

      updateDashAdding(req.getStatus());

      if (req.getStatus().equals("Complete")) {
        updateAssociatedRequests(loc, destID);
      }
    } else { // Editing
      Request req = transportDAOImpl.getAllRequests().get(transportIDLabel.getText());
      if (!req.getLocation().getNodeID().equals(loc)) {
        Location newLoc = locationsHash.get(loc);
        transportDAOImpl.updateLocation(req, newLoc);
      }
      if (!req.getAttribute2().equals(destLongName)) {
        transportDAOImpl.updateAttribute2(req, destLongName);
      }
      if (!req.getEmployee().getName().equals(emp)) {
        transportDAOImpl.updateEmployeeName(req, emp);
      }
      if (!req.getStatus().equals(stat)) {
        updateDashAdding(stat);
        updateDashSubtracting(req.getStatus());
        transportDAOImpl.updateStatus(req, stat);

        if (stat.equals("Complete")) {
          updateAssociatedRequests(loc, destID);
        }
      }
      if (!req.getDescription().equals(desc)) {
        transportDAOImpl.updateDescription(req, desc);
      }
      if (!req.getType().equals(desc)) {
        transportDAOImpl.updateType(req, type);
      }
      for (int i = 0; i < transportData.size(); i++) {
        if (transportData.get(i).getName().equals(req.getName())) {
          transportData.set(i, req);
        }
      }
    }

    clear();
    hidePopout();
  }

  @FXML
  public void cancel() {
    clear();
    hidePopout();
  }

  @FXML
  public void delete() {

    try {
      String id = ((Request) transportTable.getSelectionModel().getSelectedItem()).getName();

      // removes the request from the table and dropdown
      for (int i = 0; i < transportData.size(); i++) {
        if (transportData.get(i).getName().equals(id)) {
          transportData.remove(i);
        }
      }
      updateDashSubtracting(transportDAOImpl.getAllRequests().get(id).getStatus());
      // delete from hash map and database table
      transportDAOImpl.deleteRequest(transportDAOImpl.getAllRequests().get(id));

    } catch (NullPointerException e) {
      transportTable.getSelectionModel().clearSelection();
    }
    clear();
    hidePopout();
  }

  @FXML
  public void clear() {
    transportIDLabel.setText("ID");
    transportLocation.setText("Location");
    transportEmployee.setText("Employee");
    transportStatus.setText("Status");
    transportDescription.setText("");
    transportType.setText("Type");
    transportDestination.setText("Destination");
    updateFilters();
  }

  @FXML
  public void clearFilters(ActionEvent event) {
    transportTable.setItems(transportData);
    employeeFilterField.clear();
    statusFilterField.clear();
  }

  @FXML
  public void mainLocationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    transportLocation.setText(button.getText());
  }

  @FXML
  public void mainEmployeeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    transportEmployee.setText(button.getText());
  }

  @FXML
  public void mainStatusMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    transportStatus.setText(button.getText());
  }

  @FXML
  public void mainTypeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    transportType.setText(button.getText());
  }

  @FXML
  public void mainDestinationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    transportDestination.setText(button.getText());
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

      Iterator var3 = transportDAOImpl.getAllRequests().entrySet().iterator();
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
    Request req = ((Request) transportTable.getSelectionModel().getSelectedItem());
    transportIDLabel.setText(req.getName());
    transportLocation.setText(req.getLocation().getLongName());
    transportEmployee.setText(req.getEmployee().getName());
    transportStatus.setText(req.getStatus());
    transportDescription.setText(req.getDescription());
    transportType.setText(req.getType());
    transportDestination.setText(req.getAttribute2());
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

  private void updateAssociatedRequests(String oldLocID, String newLocID) {
    HashMap<String, Request> allReqsHash = allReqDAO.getAllRequests();
    for (Request req : allReqsHash.values()) {
      if (req.getLocation().getNodeID().equals(oldLocID)) {
        allReqDAO.updateLocation(req, locationsHash.get(newLocID));
      }
    }
  }

  @FXML
  public void newRequest() {
    deleteRequest.setVisible(false);
    transportStatus.setVisible(false);
    showPopout();
    clear();
    transportIDLabel.setText("New Request");
  }

  public void loadRequest(MouseEvent mouseEvent) {
    try {
      if (mouseEvent.getButton() == MouseButton.PRIMARY) {
        populate();
        deleteRequest.setVisible(true);
        transportStatus.setVisible(true);
      }
    } catch (NullPointerException e) {
      hidePopout();
    }
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
