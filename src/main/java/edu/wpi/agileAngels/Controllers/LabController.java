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

// similar to equip controller
public class LabController implements Initializable, PropertyChangeListener {

  @FXML AnchorPane anchor;
  @FXML VBox popOut;
  @FXML HBox tableHBox;
  @FXML MenuButton labLocation, labEmployee, labStatus, labType, employeeFilter, statusFilter;
  @FXML Button newRequest, cancelRequest, submitRequest, deleteRequest;
  @FXML TableView labTable;
  @FXML
  private TableColumn nameColumn,
      typeColumn,
      locationColumn,
      employeeColumn,
      statusColumn,
      descriptionColumn;
  @FXML TextField labDescription;
  @FXML Label notStartedNumber, inProgressNumber, completedNumber, labID2;

  private RequestDAOImpl labRequestImpl = RequestDAOImpl.getInstance("LabRequest");
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private HashMap<String, Location> locationsHash = locDAO.getAllLocations();
  private ArrayList<Location> locationsList = new ArrayList<>(locationsHash.values());
  private EmployeeManager empDAO = EmployeeManager.getInstance();
  private HashMap<String, Employee> employeeHash = empDAO.getAllEmployees();
  private static ObservableList<Request> labData = FXCollections.observableArrayList();
  private int statusNotStarted, statusInProgress, statusComplete;

  HashMap<String, String> locationIDsByLongName = new HashMap<>();

  AppController appController = AppController.getInstance();

  public Boolean filtered = false;

  public LabController() throws SQLException {}

  /**
   * Will check if the table is empty and if so will populate it.Otherwise, just calls upon the
   * database for the data. This also contains a dashboardLoad function that will make the numbers
   * on the dashboard appear.
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appController.addPropertyChangeListener(this);
    hidePopout();
    statusNotStarted = 0;
    statusInProgress = 0;
    statusComplete = 0;
    // nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    for (Location loc : locationsHash.values()) {
      locationIDsByLongName.put(loc.getLongName(), loc.getNodeID());
    }

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employee"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    labData.clear();

    for (Map.Entry<String, Request> entry : labRequestImpl.getAllRequests().entrySet()) {
      Request req = entry.getValue();
      labData.add(req);
    }
    dashboardLoad();
    labTable.setItems(labData);

    // Populates locations dropdown
    for (Location loc : locationsList) {
      MenuItem item = new MenuItem(loc.getLongName());
      item.setOnAction(this::locationMenu);
      labLocation.getItems().add(item);
    }

    // Populates employees dropdown
    for (Map.Entry<String, Employee> entry : employeeHash.entrySet()) {
      Employee emp = entry.getValue();
      MenuItem item = new MenuItem(emp.getName());
      item.setOnAction(this::employeeMenu);
      labEmployee.getItems().add(item);
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

  void updateFilters() {
    employeeFilter.getItems().clear();
    ArrayList<String> list = new ArrayList<>();
    for (Request r : labData) {
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
        for (Request r : labData) {
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
    labTable.setItems(statusFilterdList);
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
    labTable.setItems(labData);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
    appController.displayAlert();
  }

  @FXML
  public void newRequest() {
    deleteRequest.setVisible(false);
    labStatus.setVisible(false);
    showPopout();
    clear();
    labID2.setText("New Request");
  }

  @FXML
  public void submit(ActionEvent event) {
    String loc = locationIDsByLongName.get(labLocation.getText());
    String emp = labEmployee.getText();
    String stat = labStatus.getText();
    String desc = labDescription.getText();
    String type = labType.getText();

    if (labID2.getText().equals("New Request")) {
      Request req =
          new Request(
              "",
              employeeHash.get(emp),
              locationsHash.get(loc),
              type,
              "Not Started",
              desc,
              "N/A",
              "N/A");
      labData.add(req);
      labRequestImpl.addRequest(req);
      updateDashAdding(req.getStatus());
    } else { // Editing
      Request req = labRequestImpl.getAllRequests().get(labID2.getText());
      if (!req.getLocation().getNodeID().equals(loc)) {
        Location newLoc = locationsHash.get(loc);
        labRequestImpl.updateLocation(req, newLoc);
      }
      if (!req.getEmployee().getName().equals(emp)) {
        labRequestImpl.updateEmployeeName(req, emp);
      }
      if (!req.getStatus().equals(stat)) {

        updateDashAdding(stat);
        updateDashSubtracting(req.getStatus());
        labRequestImpl.updateStatus(req, stat);
      }
      if (!req.getDescription().equals(desc)) {
        labRequestImpl.updateDescription(req, desc);
      }
      if (!req.getType().equals(type)) {
        labRequestImpl.updateType(req, type);
      }

      for (int i = 0; i < labData.size(); i++) {
        if (labData.get(i).getName().equals(req.getName())) {
          labData.set(i, req);
        }
      }
    }

    clear();
    tableHBox.getChildren().remove(0);
  }

  @FXML
  public void cancel(ActionEvent event) {
    clear();
    hidePopout();
  }

  @FXML
  public void delete(ActionEvent event) {

    try {
      String id = ((Request) labTable.getSelectionModel().getSelectedItem()).getName();

      // removes the request from the table and dropdown
      for (int i = 0; i < labData.size(); i++) {
        if (labData.get(i).getName().equals(id)) {
          labData.remove(i);
          // labID.getItems().remove(i);
        }
      }
      updateDashSubtracting(labRequestImpl.getAllRequests().get(id).getStatus());
      // delete from hash map and database table
      labRequestImpl.deleteRequest(labRequestImpl.getAllRequests().get(id));

    } catch (NullPointerException e) {
      labTable.getSelectionModel().clearSelection();
    }
    clear();
    hidePopout();
  }

  @FXML
  public void clear() {
    labID2.setText("ID");
    labType.setText("Type");
    labEmployee.setText("Employee");
    labLocation.setText("Location");
    labStatus.setText("Status");
    labDescription.setText("");
    labDescription.setPromptText("Description");
    updateFilters();
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

      Iterator var3 = labRequestImpl.getAllRequests().entrySet().iterator();
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
   * Will set the dashboard's numbers to the certain types of status's, literally is just a setter
   * method to keep things clean. - Justin
   *
   * @param notStarted
   * @param inProgress
   * @param complete
   */
  @FXML
  private void setDashboard(int notStarted, int inProgress, int complete) {
    String notStart = Integer.toString(notStarted);
    String inProg = Integer.toString(inProgress);
    String comp = Integer.toString(complete);
    // Should put the numbers on the not started area on the dashboard.
    notStartedNumber.setText(notStart); // perhaps string value?
    // Should put the numbers on the in progress area of dash.
    inProgressNumber.setText(inProg);
    // Should put the numbers of the completed statuses into dash.
    completedNumber.setText(comp);
  }

  public void clearPage(ActionEvent actionEvent) {
    appController.clearPage();
  }

  public void labTypeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    labType.setText(button.getText());
  }

  @FXML
  public void locationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    labLocation.setText(button.getText());
  }

  @FXML
  public void employeeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    labEmployee.setText(button.getText());
  }

  @FXML
  public void labStatusMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    labStatus.setText(button.getText());
  }

  private void populate() {
    showPopout();
    Request req = ((Request) labTable.getSelectionModel().getSelectedItem());
    labID2.setText(req.getName());
    labLocation.setText(req.getLocation().getLongName());
    labEmployee.setText(req.getEmployee().getName());
    labStatus.setText(req.getStatus());
    labDescription.setText(req.getDescription());
    labType.setText(req.getType());
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
    /*  if (statusComplete < 0){
       statusComplete = 0;
     }

    */
    setDashboard(statusNotStarted, statusInProgress, statusComplete);
  }

  public void loadRequest(MouseEvent mouseEvent) {
    clear();
    try {
      if (mouseEvent.getButton() == MouseButton.PRIMARY) {
        populate();
        deleteRequest.setVisible(true);
        labStatus.setVisible(true);
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
