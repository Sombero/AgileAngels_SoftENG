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

public class GiftsController implements Initializable, PropertyChangeListener {

  @FXML AnchorPane anchor;
  @FXML VBox popOut;
  @FXML HBox tableHBox;
  @FXML MenuButton giftLocation, giftEmployee, giftStatus, giftType, employeeFilter, statusFilter;
  @FXML Button modifyButton, cancelRequest, submitRequest, clearRequest, deleteRequest;
  @FXML TableView giftTable;
  @FXML
  private TableColumn nameColumn,
      availableColumn,
      typeColumn,
      locationColumn,
      employeeColumn,
      statusColumn,
      senderColumn,
      descriptionColumn;
  @FXML
  TextField giftDescription, employeeFilterField, statusFilterField, giftSender, giftRecipient;
  @FXML Label notStartedNumber, inProgressNumber, completedNumber, giftIDLabel;

  HashMap<String, String> locationIDsByLongName = new HashMap<>();

  // DAOs, HashMaps, and Lists required for functionality
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private EmployeeManager empDAO = EmployeeManager.getInstance();

  private HashMap<String, Location> locationsHash = locDAO.getAllLocations();
  private ArrayList<Location> locationsList = new ArrayList<>(locationsHash.values());
  private HashMap<String, Employee> employeeHash = empDAO.getAllEmployees();
  private static ObservableList<Request> giftData = FXCollections.observableArrayList();
  private int statusNotStarted, statusInProgress, statusComplete;
  private AppController appController = AppController.getInstance();
  private RequestDAOImpl giftRequestImpl = RequestDAOImpl.getInstance("GiftRequest");

  public GiftsController() throws SQLException {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hidePopout();

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
    availableColumn.setCellValueFactory(new PropertyValueFactory<>("attribute1"));
    senderColumn.setCellValueFactory(new PropertyValueFactory<>("attribute2"));

    // Populates the table from UI list
    giftData.clear();
    for (Map.Entry<String, Request> entry : giftRequestImpl.getAllRequests().entrySet()) {
      Request req = entry.getValue();
      giftData.add(req);
    }

    dashboardLoad();
    giftTable.setItems(giftData);

    for (Location loc : locationsList) {
      MenuItem item = new MenuItem(loc.getLongName());
      item.setOnAction(this::mainLocationMenu);
      giftLocation.getItems().add(item);
    }

    // Populates employees dropdown
    for (Map.Entry<String, Employee> entry : employeeHash.entrySet()) {
      Employee emp = entry.getValue();
      MenuItem item = new MenuItem(emp.getName());
      item.setOnAction(this::mainEmployeeMenu);
      giftEmployee.getItems().add(item);
    }
    clear();
    setColor(appController.color);
  }

  void updateFilters() {
    employeeFilter.getItems().clear();
    ArrayList<String> list = new ArrayList<>();
    for (Request r : giftData) {
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
        for (Request r : giftData) {
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
    giftTable.setItems(statusFilterdList);
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
    giftTable.setItems(giftData);
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

  public void showPopout() {
    if (tableHBox.getChildren().get(0) != popOut) {
      tableHBox.getChildren().add(0, popOut);
    }
  }

  @FXML
  public void newRequest() {
    deleteRequest.setVisible(false);
    giftStatus.setVisible(false);
    showPopout();
    clear();
    giftIDLabel.setText("New Request");
  }

  @FXML
  public void submit(ActionEvent event) {
    String loc = locationIDsByLongName.get(giftLocation.getText());
    String emp = giftEmployee.getText();
    String stat = giftStatus.getText();
    String type = giftType.getText();
    String desc = giftDescription.getText();
    String send = giftSender.getText();
    String rec = giftRecipient.getText();

    // Adding
    if (giftIDLabel.getText().equals("New Request")) {
      Request req =
          new Request(
              "",
              employeeHash.get(emp),
              locationsHash.get(loc),
              type,
              "Not Started",
              desc,
              rec,
              send);
      giftData.add(req);
      giftRequestImpl.addRequest(req);
      updateDashAdding(req.getStatus());
    } else { // Editing
      Request req = giftRequestImpl.getAllRequests().get(giftIDLabel.getText());
      if (!req.getLocation().getNodeID().equals(loc)) {
        Location newLoc = locationsHash.get(loc);
        giftRequestImpl.updateLocation(req, newLoc);
      }
      if (!req.getEmployee().getName().equals(emp)) {
        giftRequestImpl.updateEmployeeName(req, emp);
      }
      if (!req.getType().equals(type)) {
        giftRequestImpl.updateType(req, type);
      }
      if (!req.getStatus().equals(stat)) {
        updateDashAdding(stat);
        updateDashSubtracting(req.getStatus());
        giftRequestImpl.updateStatus(req, stat);
      }
      if (!req.getDescription().equals(desc)) {
        giftRequestImpl.updateDescription(req, desc);
      }
      if (!req.getAttribute2().equals(send)) {
        giftRequestImpl.updateAttribute2(req, send);
      }
      if (!req.getAttribute1().equals(rec)) {
        giftRequestImpl.updateAttribute1(req, rec);
      }
      for (int i = 0; i < giftData.size(); i++) {
        if (giftData.get(i).getName().equals(req.getName())) {
          giftData.set(i, req);
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
  public void delete() {

    try {
      String id = ((Request) giftTable.getSelectionModel().getSelectedItem()).getName();

      // removes the request from the table and dropdown
      for (int i = 0; i < giftData.size(); i++) {
        if (giftData.get(i).getName().equals(id)) {
          giftData.remove(i);
        }
      }
      updateDashSubtracting(giftRequestImpl.getAllRequests().get(id).getStatus());
      // delete from hash map and database table
      giftRequestImpl.deleteRequest(giftRequestImpl.getAllRequests().get(id));

    } catch (NullPointerException e) {
      giftTable.getSelectionModel().clearSelection();
    }
    clear();
    hidePopout();
  }

  @FXML
  public void clear() {
    giftIDLabel.setText("ID");
    giftLocation.setText("Location");
    giftEmployee.setText("Employee");
    giftStatus.setText("Status");
    giftType.setText("Type");
    giftDescription.setText("");
    giftDescription.setPromptText("Description");
    giftSender.setText("");
    giftRecipient.setText("");
    updateFilters();
  }

  @FXML
  public void mainLocationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    giftLocation.setText(button.getText());
  }

  @FXML
  public void mainEmployeeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    giftEmployee.setText(button.getText());
  }

  @FXML
  public void giftStatusMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    giftStatus.setText(button.getText());
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

      Iterator var3 = giftRequestImpl.getAllRequests().entrySet().iterator();
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
    Request req = ((Request) giftTable.getSelectionModel().getSelectedItem());
    giftIDLabel.setText(req.getName());
    giftLocation.setText(req.getLocation().getLongName());
    giftEmployee.setText(req.getEmployee().getName());
    giftStatus.setText(req.getStatus());
    giftType.setText(req.getType());
    giftDescription.setText(req.getDescription());
    giftRecipient.setText(req.getAttribute1());
    giftSender.setText(req.getAttribute2());
  }

  public void menuItemSelected(ActionEvent actionEvent) {}

  public void giftTypeMenu(ActionEvent actionEvent) {
    MenuItem button = (MenuItem) actionEvent.getSource();
    giftType.setText(button.getText());
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
        giftStatus.setVisible(true);
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
