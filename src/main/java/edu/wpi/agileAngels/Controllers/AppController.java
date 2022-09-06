package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.DBconnection;
import edu.wpi.agileAngels.Database.Employee;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppController {

  public static Stack<String> pageHistory = new Stack<>();
  private static AppController appController = null;
  private MenuController menuController;
  private PropertyChangeSupport support;
  private Stage primaryStage;
  private int[] dirtyBeds = new int[4];
  private int[] dirtyInfusionPumps = new int[4];
  private int[] dirtyRecliners = new int[4];
  private int[] dirtyXRays = new int[4];
  public boolean embeddedON = false;

  // for color modes, this is defualt color
  public String color = "blue";

  HashMap<String, String> pages = new HashMap<>();

  private String currentFloor = "2";
  private Employee currentUser;

  public AppController() {
    support = new PropertyChangeSupport(this);

    for (int i = 0; i < 4; i++) {
      dirtyBeds[i] = 0;
      dirtyInfusionPumps[i] = 0;
      dirtyRecliners[i] = 0;
      dirtyXRays[i] = 0;
    }
  }

  public static AppController getInstance() {
    if (appController == null) {
      appController = new AppController();
    }
    return appController;
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    support.removePropertyChangeListener(pcl);
  }

  public void incrementDirty(String type, String floor, int i) {
    if (type.equals("XRayMachine")) {
      incrementDirtyXRays(floor, i);
    } else if (type.equals("InfusionPump")) {
      incrementDirtyInfusionPumps(floor, i);
    } else if (type.equals("Bed")) {
      incrementDirtyBeds(floor, i);
    } else if (type.equals("Recliner")) {
      incrementDirtyRecliners(floor, i);
    }
  }

  public void incrementDirtyBeds(String floor, int increment) {
    int floorInt = getFloorInt(floor);
    try {
      this.dirtyBeds[floorInt] = this.dirtyBeds[floorInt] + increment;
      this.dirtyBeds[0] = this.dirtyBeds[0] + increment;
      support.firePropertyChange(
          "dirtyBeds" + floor, this.dirtyBeds[floorInt] - increment, this.dirtyBeds[floorInt]);
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  public void incrementDirtyInfusionPumps(String floor, int increment) {
    int floorInt = getFloorInt(floor);
    try {
      this.dirtyInfusionPumps[floorInt] = this.dirtyInfusionPumps[floorInt] + increment;
      this.dirtyInfusionPumps[0] = this.dirtyInfusionPumps[0] + increment;
      support.firePropertyChange(
          "dirtyPumps" + floor,
          this.dirtyInfusionPumps[floorInt] - increment,
          this.dirtyInfusionPumps[floorInt]);

    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  public void setUser(Employee employee) {
    this.currentUser = employee;
  }

  public Employee getCurrentUser() {
    return currentUser;
  }

  public void incrementDirtyRecliners(String floor, int increment) {
    int floorInt = getFloorInt(floor);
    try {
      this.dirtyRecliners[floorInt] = this.dirtyRecliners[floorInt] + increment;
      this.dirtyRecliners[0] = this.dirtyRecliners[0] + increment;
      support.firePropertyChange(
          "dirtyRecliners" + floor,
          this.dirtyRecliners[floorInt] - increment,
          this.dirtyRecliners[floorInt]);
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  public void incrementDirtyXRays(String floor, int increment) {
    int floorInt = getFloorInt(floor);
    try {
      this.dirtyXRays[floorInt] = this.dirtyXRays[floorInt] + increment;
      this.dirtyXRays[0] = this.dirtyXRays[0] + increment;
      support.firePropertyChange(
          "dirtyXRays" + floor, this.dirtyXRays[floorInt] - increment, this.dirtyXRays[floorInt]);
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
  }

  public boolean displayAlert() {
    boolean alertDisplayed = false;
    String view = "";
    if (dirtyBeds[1] > 6) {
      view = "/edu/wpi/agileAngels/views/bed-alert-view.fxml";
    } else if (dirtyBeds[2] > 6) {
      view = "/edu/wpi/agileAngels/views/bed-alert-view.fxml";
    } else if (dirtyBeds[3] > 6) {
      view = "/edu/wpi/agileAngels/views/bed-alert-view.fxml";
    } else if (dirtyInfusionPumps[1] > 10) {
      view = "/edu/wpi/agileAngels/views/pump-alert-view.fxml";
    } else if (dirtyInfusionPumps[2] > 10) {
      view = "/edu/wpi/agileAngels/views/pump-alert-view.fxml";
    } else if (dirtyInfusionPumps[3] > 10) {
      view = "/edu/wpi/agileAngels/views/pump-alert-view.fxml";
    }
    if (!view.equals("")) {
      alertDisplayed = true;
      FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
      try {
        Scene secondScene = new Scene(loader.load());

        secondScene
            .getStylesheets()
            .add(
                "https://fonts.googleapis.com/css2?family=Roboto:wght@100;300;400;500;700;900&display=swap");

        Stage newWindow = new Stage();
        newWindow.setTitle("Alert");
        newWindow.setScene(secondScene);

        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);

        newWindow.show();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return alertDisplayed;
  }

  public String getPumpFloor() {
    String floor = "";
    if (dirtyInfusionPumps[1] > 10) {
      floor = "3";
    } else if (dirtyInfusionPumps[2] > 10) {
      floor = "4";
    } else if (dirtyInfusionPumps[3] > 10) {
      floor = "5";
    }
    return floor;
  }

  public String getBedFloor() {
    String floor = "";
    if (dirtyBeds[1] > 6) {
      floor = "3";
    } else if (dirtyBeds[2] > 6) {
      floor = "4";
    } else if (dirtyBeds[3] > 6) {
      floor = "5";
    }
    return floor;
  }

  private int getFloorInt(String floor) {
    int floorInt = -1;
    if (floor.equals("3")) {
      floorInt = 1;
    } else if (floor.equals("4")) {
      floorInt = 2;
    } else if (floor.equals("5")) {
      floorInt = 3;
    }
    return floorInt;
  }

  public void init(Stage primaryStage) {
    this.primaryStage = primaryStage;
    loadPage("/edu/wpi/agileAngels/views/login.fxml");
    pages.put("/edu/wpi/agileAngels/views/login.fxml", "Login");
    pages.put("/edu/wpi/agileAngels/views/lab-view.fxml", "Lab Request");
    pages.put("/edu/wpi/agileAngels/views/emergency-view.fxml", "Emergency");
    pages.put("/edu/wpi/agileAngels/views/equipment-view.fxml", "Equipment Request");
    pages.put("/edu/wpi/agileAngels/views/gifts-view.fxml", "Gift Request");
    pages.put("/edu/wpi/agileAngels/views/maintenance-view.fxml", "Maintenance Request");
    pages.put("/edu/wpi/agileAngels/views/map-view.fxml", "Map");
    pages.put("/edu/wpi/agileAngels/views/morgue-view.fxml", "Morgue Request");
    pages.put("/edu/wpi/agileAngels/views/NEWdashboard.fxml", "Dashboard");
    pages.put("/edu/wpi/agileAngels/views/patientTransport-view.fxml", "Patient Transport");
    pages.put("/edu/wpi/agileAngels/views/sanitation-view.fxml", "Sanitation Request");
    pages.put("/edu/wpi/agileAngels/views/serviceRequest-view.fxml", "Service Request");
    pages.put("/edu/wpi/agileAngels/views/aboutUs-view.fxml", "About Us");
    pages.put("/edu/wpi/agileAngels/views/profile-view.fxml", "Profile");
    pages.put("/edu/wpi/agileAngels/views/laundryRequest-view.fxml", "Laundry Request");
    pages.put("edu/wpi/agileAngels/views/medAid-view.fxml", "Medical Aid");
  }

  public void closeApp() {
    DBconnection.shutdown();
    Platform.exit();
  }

  public boolean isEmbeddedON() {
    return embeddedON;
  }

  public void setEmbeddedON(boolean embeddedON) {
    this.embeddedON = embeddedON;
  }

  public void loadPage(String view) {
    if (pageHistory.isEmpty()) {
      pageHistory.push(view);
    } else if (!Objects.equals(view, pageHistory.peek())) {
      pageHistory.push(view);
    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
    Parent root = null;
    try {
      root = loader.load();
      Scene defaultScene = new Scene(root);

      defaultScene
          .getStylesheets()
          .add(
              "https://fonts.googleapis.com/css2?family=Roboto:wght@100;300;400;500;700;900&display=swap");

      primaryStage.setScene(defaultScene);
      primaryStage.setResizable(true);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
    setMenuBarTitle(view);
    try {
      setMenuBarUser();
    } catch (NullPointerException e) {

    }
  }

  void setMenuBarTitle(String view) {
    try {
      if (view.equals("/edu/wpi/agileAngels/views/NEWdashboard.fxml")) {
        menuController.hideButtons();
      }
      menuController.changeTitle(pages.get(view));
    } catch (NullPointerException e) {

    }
  }

  private void setMenuBarUser() {
    menuController.setUserInitials(currentUser.initialsMaker());
  }

  public void back() {
    pageHistory.pop();
    loadPage(pageHistory.peek());
  }

  public void clearPage() {
    loadPage(pageHistory.peek());
  }

  private void profile() throws IOException {
    loadPage("/edu/wpi/agileAngels/views/login.fxml");
  }

  public String getCurrentFloor() {
    return currentFloor;
  }

  public void setCurrentFloor(String currentFloor) {
    this.currentFloor = currentFloor;
  }

  private void goHome(ActionEvent event) {
    loadPage("/edu/wpi/agileAngels/views/NEWdashboard.fxml");
  }

  public void setCurrentMenuController(MenuController menuController) {
    this.menuController = menuController;
  }

  public MenuController getMenuController() {
    return menuController;
  }

  public void updateMenuColor() {
    menuController.setColor(this.color);
  }
}
