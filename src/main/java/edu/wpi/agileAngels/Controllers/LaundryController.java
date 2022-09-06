package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.*;
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
import javax.swing.*;

public class LaundryController implements Initializable {

  @FXML AnchorPane anchor;
  @FXML VBox popOut;
  @FXML HBox tableHBox;
  @FXML Label notStartedNumber, inProgressNumber, completedNumber, laundryIDLabel;
  @FXML
  MenuButton laundryLocation,
      laundryType,
      laundryStatus,
      laundryEmployee,
      employeeFilter,
      statusFilter;
  @FXML private TextField laundryDescription, employeeFilterField, statusFilterField;
  @FXML
  private TableColumn nameColumn,
      typeColumn,
      locationColumn,
      employeeColumn,
      statusColumn,
      descriptionColumn;
  // todo add edit/delete?
  @FXML
  Button addButton,
      editButton,
      deleteButton,
      cancelRequest,
      submitRequest,
      clearRequest,
      deleteRequest;
  // @FXML private Label giftConfirm;
  private RequestDAOImpl laundryRequestImpl =
      RequestDAOImpl.getInstance(
          "LaundryRequest"); // instance of RequestDAOImpl to access functions

  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private HashMap<String, Location> locationsHash = locDAO.getAllLocations();
  private ArrayList<Location> locationsList = new ArrayList<>(locationsHash.values());
  HashMap<String, String> locationIDsByLongName = new HashMap<>();

  private EmployeeManager employeeDAO = EmployeeManager.getInstance();
  private HashMap<String, Employee> employeesHash = employeeDAO.getAllEmployees();

  @FXML private TableView laundryTable;
  private AppController appController = AppController.getInstance();

  private static ObservableList<Request> laundryData =
      FXCollections.observableArrayList(); // list of requests

  public LaundryController() throws SQLException {}

  // private String[] locations = new String[9999];
  // private String[] employees = new String[9999];
  private String[] types = {"Whites", "Colors", "Infected", "Towels/Blankets"};
  private String[] status = {"NotStarted", "InProgress", "Complete"};

  private int statusNotStarted, statusInProgress, statusComplete;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hidePopout();

    statusNotStarted = 0;
    statusInProgress = 0;
    statusComplete = 0;

    for (Location loc : locationsHash.values()) {
      locationIDsByLongName.put(loc.getLongName(), loc.getNodeID());
    }

    // availableColumn.setCellValueFactory(new PropertyValueFactory<>("attribute1"));
    employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    /*int count = 0;
    for (Map.Entry<String, Employee> entry : employeesHash.entrySet()) {
      Employee emp = entry.getValue();
      employees[count] = emp.getName();
      count++;
    }
    count = 0;
    for (Location loc : locationsList) {
      locations[count] = loc.getLongName();
      count++;
    }*/

    laundryData.clear();
    for (Map.Entry<String, Request> entry : laundryRequestImpl.getAllRequests().entrySet()) {
      Request req = entry.getValue();
      laundryData.add(req);
    }

    for (Location loc : locationsList) {
      MenuItem item = new MenuItem(loc.getLongName());
      item.setOnAction(this::locationMenu);
      laundryLocation.getItems().add(item);
    }

    // Populates employees dropdown
    for (Map.Entry<String, Employee> entry : employeesHash.entrySet()) {
      Employee emp = entry.getValue();
      MenuItem item = new MenuItem(emp.getName());
      item.setOnAction(this::employeeMenu);
      laundryEmployee.getItems().add(item);
    }

    clear();
    dashboardLoad();
    laundryTable.setItems(laundryData);
    setColor(appController.color);
  }

  @FXML
  /** Submits fields to a Java gifts Request Object */
  private void submitLaundry(ActionEvent event) {
    String ID = laundryIDLabel.getText();
    String type = laundryType.getText();
    String employee = laundryEmployee.getText();
    String location = locationIDsByLongName.get(laundryLocation.getText());
    String description = laundryDescription.getText();
    String status = laundryStatus.getText();

    // Adding
    if (ID.equals("New Request")) {
      String placeholder = "?";

      Request laundry =
          new Request(
              placeholder,
              employeesHash.get(employee),
              locationsHash.get(location),
              type,
              "Not Started",
              description,
              "",
              "");
      laundryRequestImpl.addRequest(laundry); // add to hashmap
      laundryData.add(laundry); // add to the UI
      laundryTable.setItems(laundryData);
      updateDashAdding(laundry.getStatus());

    } else { // Editing
      editLaundryRequest(ID, type, employee, location, description, status);
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

  void updateFilters() {
    employeeFilter.getItems().clear();
    ArrayList<String> list = new ArrayList<>();
    for (Request r : laundryData) {
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
        for (Request r : laundryData) {
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
    laundryTable.setItems(statusFilterdList);
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
    laundryTable.setItems(laundryData);
  }

  @FXML
  private void editLaundryRequest(
      String editString,
      String typeString,
      String employeeString,
      String locationString,
      String descriptionString,
      String statusString) {

    Request found = null;
    int num = 0;
    for (int i = 0; i < laundryData.size(); i++) {
      Request device = laundryData.get(i);
      if (0 == laundryIDLabel.getText().compareTo(device.getName())) {
        found = device;
        num = i;
      }
    }
    Employee emp = employeeDAO.getEmployee(employeeString);
    Location loc = locDAO.getLocation(locationString);
    if (found != null) {
      if (!typeString.isEmpty()) {
        // String type = typeButtonText.getText();
        found.setType(typeString);
        // LaundryrequestImpl.updateType(found, typeString);
      }
      if (!locationString.isEmpty()) {
        // String location = equipLocation.getText();
        found.setLocation(loc);
        // LaundryrequestImpl.updateLocation(found, locationsHash.get(locationString));
      }
      if (!employeeString.isEmpty()) {
        // String employee = emp.getText();
        found.setEmployee(emp);
        // LaundryrequestImpl.updateEmployeeName(found, employeeString);
      }
      if (!statusString.isEmpty()) {
        updateDashSubtracting(found.getStatus());
        updateDashAdding(statusString);
        // String employee = emp.getText();
        found.setStatus(statusString);
        //  LaundryrequestImpl.updateStatus(found, statusString);
      }
      if (!descriptionString.isEmpty()) {
        // String description = emp.getText();
        found.setDescription(descriptionString);
        // LaundryrequestImpl.updateStatus(found, descriptionString);
      }
      laundryData.set(num, found);

      laundryTable.setItems(laundryData);
    }
  }

  public void clearPage(ActionEvent event) {
    appController.clearPage();
  }

  public void laundryTypeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    laundryType.setText(button.getText());
  }

  @FXML
  public void locationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    laundryLocation.setText(button.getText());
  }

  @FXML
  public void employeeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    laundryEmployee.setText(button.getText());
  }

  @FXML
  public void statusMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    laundryStatus.setText(button.getText());
  }

  @FXML
  public void clear() {
    laundryIDLabel.setText("ID");
    laundryType.setText("Type");
    laundryEmployee.setText("Employee");
    laundryLocation.setText("Location");
    laundryStatus.setText("Status");
    laundryDescription.setText("");
    laundryDescription.setPromptText("Description");
    updateFilters();
  }

  @FXML
  public void cancel(ActionEvent event) {
    clear();
    hidePopout();
  }

  @FXML
  public void delete(ActionEvent event) {

    try {
      String id = ((Request) laundryTable.getSelectionModel().getSelectedItem()).getName();
      // removes the request from the table and dropdown
      for (int i = 0; i < laundryData.size(); i++) {
        if (laundryData.get(i).getName().equals(id)) {
          laundryData.remove(i);
        }
      }
      updateDashSubtracting(laundryRequestImpl.getAllRequests().get(id).getStatus());
      // delete from hash map and database table
      laundryRequestImpl.deleteRequest(laundryRequestImpl.getAllRequests().get(id));

    } catch (NullPointerException e) {
      laundryTable.getSelectionModel().clearSelection();
    }
    clear();
    hidePopout();
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

      Iterator var3 = laundryRequestImpl.getAllRequests().entrySet().iterator();
      while (var3.hasNext()) {
        Map.Entry<String, Request> entry = (Map.Entry) var3.next();
        Request object = (Request) entry.getValue();
        if (entry.getValue().getStatus().equals("InProgress")
            || entry.getValue().getStatus().equals("In Progress")) {
          statusInProgress++;
        }
        if (entry.getValue().getStatus().equals("NotStarted")
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
    Request req = ((Request) laundryTable.getSelectionModel().getSelectedItem());
    laundryIDLabel.setText(req.getName());
    laundryLocation.setText(req.getLocation().getLongName());
    laundryEmployee.setText(req.getEmployee().getName());
    laundryStatus.setText(req.getStatus());
    laundryDescription.setText(req.getDescription());
    laundryType.setText(req.getType());
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

  @FXML
  public void newRequest() {
    deleteRequest.setVisible(false);
    laundryStatus.setVisible(false);
    showPopout();
    clear();
    laundryIDLabel.setText("New Request");
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
        laundryStatus.setVisible(true);
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
