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

public class MealController implements Initializable, PropertyChangeListener {

  @FXML AnchorPane anchor;
  @FXML VBox popOut;
  @FXML HBox tableHBox;
  @FXML MenuButton mealLocation, mealEmployee, mealStatus, mealType, employeeFilter, statusFilter;
  @FXML Button modifyButton, cancelRequest, submitRequest, clearRequest, deleteRequest;
  @FXML TableView mealTable;
  @FXML
  private TableColumn nameColumn,
      typeColumn,
      locationColumn,
      employeeColumn,
      statusColumn,
      descriptionColumn; // , availableColumn,;
  @FXML TextField mealDescription, employeeFilterField, statusFilterField;
  @FXML Label notStartedNumber, inProgressNumber, completedNumber, mealIDLabel;

  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private EmployeeManager empDAO = EmployeeManager.getInstance();
  private RequestDAOImpl mealRequestImpl = RequestDAOImpl.getInstance("MealRequest");
  private HashMap<String, Location> locationsHash = locDAO.getAllLocations();
  private ArrayList<Location> locationsList = new ArrayList<>(locationsHash.values());
  private HashMap<String, Employee> employeeHash = empDAO.getAllEmployees();
  private static ObservableList<Request> mealData = FXCollections.observableArrayList();
  AppController appController = AppController.getInstance();
  HashMap<String, String> locationIDsByLongName = new HashMap<>();

  private int statusNotStarted, statusInProgress, statusComplete;

  public MealController() throws SQLException {}

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
    // availableColumn.setCellValueFactory(new PropertyValueFactory<>("attribute1"));

    mealData.clear();
    for (Map.Entry<String, Request> entry : mealRequestImpl.getAllRequests().entrySet()) {
      Request req = entry.getValue();
      mealData.add(req);
    }
    dashboardLoad();
    mealTable.setItems(mealData);
    setColor(appController.color);

    // Populates locations dropdown
    for (Location loc : locationsList) {
      MenuItem item = new MenuItem(loc.getLongName());
      item.setOnAction(this::mealLocationMenu);
      mealLocation.getItems().add(item);
    }

    // Populates employees dropdown
    for (Map.Entry<String, Employee> entry : employeeHash.entrySet()) {
      Employee emp = entry.getValue();
      MenuItem item = new MenuItem(emp.getName());
      item.setOnAction(this::mealEmployeeMenu);
      mealEmployee.getItems().add(item);
    }
    clear();
  }

  void updateFilters() {
    employeeFilter.getItems().clear();
    ArrayList<String> list = new ArrayList<>();
    for (Request r : mealData) {
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
        for (Request r : mealData) {
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
    mealTable.setItems(statusFilterdList);
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
    mealTable.setItems(mealData);
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
    mealStatus.setVisible(false);
    showPopout();
    clear();
    mealIDLabel.setText("New Request");
  }

  @FXML
  public void submit(ActionEvent event) {
    String loc = locationIDsByLongName.get(mealLocation.getText());
    String emp = mealEmployee.getText();
    String stat = mealStatus.getText();
    String desc = mealDescription.getText();
    String type = mealType.getText();

    // Adding
    if (mealIDLabel.getText().equals("New Request")) {
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
      mealData.add(req);
      mealRequestImpl.addRequest(req);
      updateDashAdding(req.getStatus());

    } else { // Editing
      Request req = mealRequestImpl.getAllRequests().get(mealIDLabel.getText());
      if (!req.getLocation().getNodeID().equals(loc)) {
        Location newLoc = locationsHash.get(loc);
        mealRequestImpl.updateLocation(req, newLoc);
      }
      if (!req.getEmployee().getName().equals(emp)) {
        mealRequestImpl.updateEmployeeName(req, emp);
      }
      if (!req.getStatus().equals(stat)) {
        updateDashSubtracting(req.getStatus());
        updateDashAdding(stat);
        mealRequestImpl.updateStatus(req, stat);
      }
      if (!req.getDescription().equals(desc)) {
        mealRequestImpl.updateDescription(req, desc);
      }
      if (!req.getType().equals(type)) {
        mealRequestImpl.updateType(req, type);
      }

      for (int i = 0; i < mealData.size(); i++) {
        if (mealData.get(i).getName().equals(req.getName())) {
          mealData.set(i, req);
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
      String id = ((Request) mealTable.getSelectionModel().getSelectedItem()).getName();
      updateDashSubtracting(mealRequestImpl.getAllRequests().get(id).getStatus());
      // removes the request from the table and dropdown
      for (int i = 0; i < mealData.size(); i++) {
        if (mealData.get(i).getName().equals(id)) {
          mealData.remove(i);
          mealEmployee.getItems().remove(i);
        }
      }
      mealRequestImpl.deleteRequest(mealRequestImpl.getAllRequests().get(id));
    } catch (NullPointerException e) {
      mealTable.getSelectionModel().clearSelection();
    }

    clear();
    hidePopout();
  }

  @FXML
  public void clear() {
    mealIDLabel.setText("ID");
    mealType.setText("Type");
    mealEmployee.setText("Employee");
    mealLocation.setText("Location");
    mealStatus.setText("Status");
    mealDescription.setText("");
    mealDescription.setPromptText("Description");
    updateFilters();
  }

  @FXML
  public void mealLocationMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    mealLocation.setText(button.getText());
  }

  @FXML
  public void mealEmployeeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    mealEmployee.setText(button.getText());
  }

  @FXML
  public void mealStatusMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    mealStatus.setText(button.getText());
  }

  @FXML
  public void mealTypeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    mealType.setText(button.getText());
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

      Iterator var3 = mealRequestImpl.getAllRequests().entrySet().iterator();
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
    Request req = ((Request) mealTable.getSelectionModel().getSelectedItem());
    mealIDLabel.setText(req.getName());
    mealLocation.setText(req.getLocation().getLongName());
    mealEmployee.setText(req.getEmployee().getName());
    mealStatus.setText(req.getStatus());
    mealDescription.setText(req.getDescription());
    mealType.setText(req.getType());
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
        mealStatus.setVisible(true);
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
