package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;

public class MapsController implements Initializable, PropertyChangeListener {

  @FXML public MenuButton LocationFilter;
  @FXML
  public CheckMenuItem patiCheck,
      storCheck,
      dirtCheck,
      hallCheck,
      elevCheck,
      restCheck,
      staiCheck,
      deptCheck,
      labsCheck,
      infoCheck,
      confCheck,
      exitCheck,
      retlCheck,
      servCheck,
      equipCheck,
      labCheck,
      mealCheck,
      giftCheck,
      sanCheck,
      maintCheck,
      transCheck,
      morgCheck,
      laundryCheck,
      xrayCheck,
      pumpCheck,
      bedCheck,
      reclinerCheck;

  HashMap<CheckMenuItem, String> locationFilterHash = new HashMap<CheckMenuItem, String>();
  HashMap<CheckMenuItem, String> requestFilterHash = new HashMap<CheckMenuItem, String>();
  HashMap<CheckMenuItem, String> equipFilterHash = new HashMap<CheckMenuItem, String>();

  @FXML
  private ImageView floorTwoMap,
      floorThreeMap,
      floorFourMap,
      floorFiveMap,
      lowerLevelOneMap,
      lowerLevelTwoMap;

  @FXML private ScrollPane mapScroll;
  @FXML HBox addButtonBox;

  @FXML CheckBox locationToggle, requestToggle, equipmentToggle;
  @FXML
  private Button locationEdit,
      locationDelete,
      requestEdit,
      requestDelete,
      switchModeButton,
      zoomIn,
      zoomOut,
      floorUp,
      floorDown;
  @FXML private MenuItem floorTwo, floorThree, floorFour, floorFive, lowerLevelOne, lowerLevelTwo;
  @FXML private TextField locationName, addLocationName;
  @FXML
  Pane mapPane, locationEditPane, requestEditPane, locationAddPane, equipmentViewPane, clickPane;
  @FXML AnchorPane anchor;
  @FXML
  Label requestName,
      floorLabel,
      nodeIDField,
      addNodeIDField,
      sameLocationName,
      sameLocationNameEdit,
      equipIDLabel,
      equipTypeLabel,
      equipStatusLabel,
      equipCleanLabel;
  @FXML
  MenuButton locationTypeDropdown,
      requestTypeDropdown,
      requestStatusDropdown,
      requestEmployeeDropdown,
      addLocationTypeDropdown,
      locationFilterButton,
      requestFilterButton,
      equipmentFilterButton;

  public ContextMenu contextMenu = new ContextMenu();

  public ContextMenu getContextMenu() {
    return contextMenu;
  }

  ContextMenu menu;
  MenuItem addNode = new MenuItem("Add Location");

  LocationNode currentLocationNode = null;
  RequestNode currentRequestNode = null;
  EquipmentNode currentEquipmentNode = null;

  Pane pane2 = new Pane();
  Pane pane3 = new Pane();
  Pane pane4 = new Pane();
  Pane pane5 = new Pane();
  Pane paneL1 = new Pane();
  Pane paneL2 = new Pane();

  private HashMap<String, Pane> floorPanes = new HashMap<>();

  ArrayList<String> floors = new ArrayList<>();

  LocationNodeManager locationNodeManager = new LocationNodeManager(this);
  RequestNodeManager requestNodeManager = new RequestNodeManager(this);
  EquipmentNodeManager equipmentNodeManager = new EquipmentNodeManager(this);

  AppController appController = AppController.getInstance();

  private MedEquipImpl equipDAO = MedEquipImpl.getInstance();
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  HashMap<String, Location> locationsHash = locDAO.getAllLocations();

  public MapsController() throws SQLException {}

  private double scale = 1;

  public double panX = 0;
  public double panY = 0;

  MouseEvent rightClick;

  private double croppedMapXOffset = 1054;
  private double croppedMapYOffset = 544;

  private double croppedMapWidth = 2000;

  private double imagePaneWidth = 1400;

  /**
   * Called on page load, creates panes for each map, adds the images for each map to its pane, and
   * sets their initial visibility
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    appController.addPropertyChangeListener(this);

    mapPane.getChildren().add(pane2);
    pane2.getChildren().add((floorTwoMap));
    pane2.setVisible(false);
    mapPane.getChildren().add(pane3);
    pane3.getChildren().add((floorThreeMap));
    pane3.setVisible(false);
    mapPane.getChildren().add(pane4);
    pane4.getChildren().add((floorFourMap));
    pane4.setVisible(false);
    mapPane.getChildren().add(pane5);
    pane5.getChildren().add((floorFiveMap));
    pane5.setVisible(false);
    mapPane.getChildren().add(paneL1);
    paneL1.getChildren().add((lowerLevelOneMap));
    paneL1.setVisible(false);
    paneL2.getChildren().add((lowerLevelTwoMap));
    mapPane.getChildren().add(paneL2);
    paneL2.setVisible(false);

    floors.add("L2");
    floors.add("L1");
    floors.add("2");
    floors.add("3");
    floors.add("4");
    floors.add("5");

    floorPanes.put(floors.get(0), paneL2);
    floorPanes.put(floors.get(1), paneL1);
    floorPanes.put(floors.get(2), pane2);
    floorPanes.put(floors.get(3), pane3);
    floorPanes.put(floors.get(4), pane4);
    floorPanes.put(floors.get(5), pane5);

    floorPanes.get(appController.getCurrentFloor()).setVisible(true);

    floorLabel.setText(appController.getCurrentFloor());

    locationAddPane.setVisible(false);
    locationEditPane.setVisible(false);
    requestEditPane.setVisible(false);
    equipmentViewPane.setVisible(false);

    contextMenu.getItems().addAll(addNode);
    addNode.setOnAction((ActionEvent event) -> addNode());

    locationNodeManager.createNodesFromDB();
    try {
      requestNodeManager.createNodesFromDB();
      equipmentNodeManager.createNodesFromDB();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    for (Employee e : requestNodeManager.employeeHash.values()) {
      MenuItem menuItem = new MenuItem(e.getName());
      menuItem.setOnAction(
          (ActionEvent event) -> {
            MenuItem button = (MenuItem) event.getSource();
            requestEmployeeDropdown.setText(button.getText());
          });
      try {
        requestEmployeeDropdown.getItems().add(menuItem);
      } catch (NullPointerException e2) {

      }
    }

    locationFilterHash.put(patiCheck, "PATI");
    locationFilterHash.put(storCheck, "STOR");
    locationFilterHash.put(dirtCheck, "DIRT");
    locationFilterHash.put(hallCheck, "HALL");
    locationFilterHash.put(elevCheck, "ELEV");
    locationFilterHash.put(restCheck, "REST");
    locationFilterHash.put(staiCheck, "STAI");
    locationFilterHash.put(deptCheck, "DEPT");
    locationFilterHash.put(labsCheck, "LABS");
    locationFilterHash.put(infoCheck, "INFO");
    locationFilterHash.put(confCheck, "CONF");
    locationFilterHash.put(exitCheck, "EXIT");
    locationFilterHash.put(retlCheck, "RETL");
    locationFilterHash.put(servCheck, "SERV");

    requestFilterHash.put(equipCheck, "Med");
    requestFilterHash.put(labCheck, "Lab");
    requestFilterHash.put(mealCheck, "Meal");
    requestFilterHash.put(giftCheck, "Gif");
    requestFilterHash.put(sanCheck, "San");
    requestFilterHash.put(maintCheck, "Mai");
    requestFilterHash.put(transCheck, "Tra");
    requestFilterHash.put(morgCheck, "Mor");
    requestFilterHash.put(laundryCheck, "Lau");

    equipFilterHash.put(xrayCheck, "XRayMachine");
    equipFilterHash.put(pumpCheck, "InfusionPump");
    equipFilterHash.put(bedCheck, "Bed");
    equipFilterHash.put(reclinerCheck, "Recliner");

    if (appController.getCurrentUser().getPermissionLevel() >= 4) {
      mapScroll.setOnMousePressed(
          (MouseEvent event) -> {
            if (event.isSecondaryButtonDown()) {
              menu = getContextMenu();
              menu.show(mapScroll, event.getScreenX(), event.getScreenY());
              rightClick = event;
            }
          });

    } else if (appController.getCurrentUser().getPermissionLevel() == 1) {
      labCheck.setSelected(false);
      equipCheck.setSelected(false);
      morgCheck.setSelected(false);
      transCheck.setSelected(false);
      requestFilter();
      labCheck.setDisable(true);
      equipCheck.setDisable(true);
      morgCheck.setDisable(true);
      transCheck.setDisable(true);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String changeType = evt.getPropertyName();
    int newValue = (int) evt.getNewValue();
    appController.displayAlert();
  }

  /**
   * Populates the text fields on the page with data of a node
   *
   * @param locationNode the node whose data is populated
   */
  public void populateLocationNodeData(LocationNode locationNode) {
    deselect();
    hideFilter();
    clickPane.setVisible(true);
    locationEditPane.setVisible(true);
    nodeIDField.setText(locationNode.getNodeID());
    locationName.setText(locationNode.getName());
    locationTypeDropdown.setText(locationNode.getNodeType());
    currentLocationNode = locationNode;
  }

  public double getScale() {
    return scale;
  }

  /**
   * Populates the text fields on the page with data of a node
   *
   * @param requestNode the node whose data is populated
   */
  public void populateRequestNodeData(RequestNode requestNode) {
    requestTypeDropdown.getItems().clear();

    try {
      for (String s : requestNodeManager.getTypes(requestNode)) {
        requestTypeDropdown.getItems().add(new MenuItem(s));
      }
    } catch (NullPointerException e) {
      requestTypeDropdown.setText("N/A");
      requestTypeDropdown.setDisable(true);
    }

    if (requestNode.getName().substring(0, 3).equals("Med")) {
      requestTypeDropdown.setDisable(true);
    }
    deselect();
    hideFilter();
    clickPane.setVisible(true);
    requestEditPane.setVisible(true);
    requestTypeDropdown.setText(requestNode.getRequest().getType());
    requestName.setText(requestNode.getName());
    requestEmployeeDropdown.setText(requestNode.getEmployee());
    requestStatusDropdown.setText(requestNode.getStatus());
    currentRequestNode = requestNode;
  }

  /**
   * Populates the text fields on the page with data of a node
   *
   * @param equipmentNode the node whose data is populated
   */
  public void populateEquipmentNodeData(EquipmentNode equipmentNode) {
    equipIDLabel.setText(equipmentNode.getMedEquip().getID());
    equipTypeLabel.setText("Type: " + equipmentNode.getMedEquip().getType());
    equipStatusLabel.setText("Status: " + equipmentNode.getStatus());
    equipCleanLabel.setText("Cleanliness: " + equipmentNode.getClean());
    currentEquipmentNode = equipmentNode;

    deselect();
    hideFilter();
    clickPane.setVisible(true);
    equipmentViewPane.setVisible(true);
  }

  @FXML
  private void addNode() {
    deselect();
    clickPane.setVisible(true);
    locationAddPane.setVisible(true);
    hideFilter();
  }

  /**
   * Edits the parameters of currentNode
   *
   * @throws IOException NumberFormatException from parseDouble
   */
  //  @FXML
  //  private void editNode() throws IOException {
  //    String name = locationName.getText();
  //    String type = locationTypeDropdown.getText();
  //    currentLocationNode.changeLocationName(name);
  //    currentLocationNode.changeLocationType(type);
  //    locationNodeManager.editNode(currentLocationNode, name, type);
  //    currentLocationNode.resetLocation();
  //    currentLocationNode = null;
  //  }
  @FXML
  public void locationEdit() {
    String name = locationName.getText();
    String type = locationTypeDropdown.getText();
    boolean flag = true;

    for (Location e : locationsHash.values()) {
      if (e.getLongName().equals(name)) {
        flag = false;
      }
    }

    if (flag) {
      currentLocationNode.changeLocationName(name);
      currentLocationNode.changeLocationType(type);
      locationNodeManager.editNode(currentLocationNode, name, type);
      currentLocationNode.resetLocation();
      currentLocationNode = null;
      deselect();
    } else if (!type.equals(currentLocationNode.getNodeType())
        && name.equals(currentLocationNode.getName())) {
      currentLocationNode.changeLocationType(type);
      locationNodeManager.editNode(currentLocationNode, currentLocationNode.getName(), type);
      currentLocationNode.resetLocation();
      currentLocationNode = null;
      deselect();
    } else {
      sameLocationNameEdit.setVisible(true);
    }
  }

  @FXML
  public void requestEdit(ActionEvent event) {
    currentRequestNode.setEmployee(requestEmployeeDropdown.getText());
    currentRequestNode.getRequest().setStatus(requestStatusDropdown.getText());
    currentRequestNode.getRequest().setType(requestTypeDropdown.getText());
    requestNodeManager.updateRequest(currentRequestNode);
    if (currentRequestNode.getRequest().getName().substring(0, 3).equals("Med")) {
      equipmentNodeManager.updateEquipObject(
          currentRequestNode.getRequest(), requestStatusDropdown.getText());
    }
    currentRequestNode = null;
    deselect();
  }

  public void updateEquipNode(String ID) {
    equipmentNodeManager.updateEquipmentNode(ID);
  }

  /**
   * Removes currentNode from its pane and calls NodeManager.deleteNode(), which removes the node
   * from the hashmap in NodeManager and the database
   */
  @FXML
  private void removeNode() {
    if (currentLocationNode.getFloor().equals("2")) {
      pane2.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("3")) {
      pane3.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("4")) {
      pane3.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("5")) {
      pane3.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("L1")) {
      paneL1.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("L2")) {
      paneL2.getChildren().remove(currentLocationNode.getButton());
    }
    locationNodeManager.deleteNode(currentLocationNode.getNodeID());
    clearFields();
  }

  /**
   * Switches between the "add a location" mode and the "edit or delete a location" mode on a button
   * press
   *
   * @param event the button that was pressed, which is either switchToAddButton or the
   *     switchToEditButton
   */

  /**
   * Adds the button for a location node to the pane corresponding to its floor
   *
   * @param node the node whose button is added to a pane
   */
  public void displayLocationNode(LocationNode node) {
    if (node.getFloor().equals("2")) {
      pane2.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("5")) {
      pane5.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("4")) {
      pane4.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("3")) {
      pane3.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("L1")) {
      paneL1.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("L2")) {
      paneL2.getChildren().add(node.getButton());
    }
  }

  /**
   * Adds the button for a request node to the pane corresponding to its floor
   *
   * @param node the node whose button is added to a pane
   */
  public void displayRequestNode(RequestNode node) {
    if (node.getFloor().equals("2")) {
      pane2.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("5")) {
      pane5.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("4")) {
      pane4.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("3")) {
      pane3.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("L1")) {
      paneL1.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("L2")) {
      paneL2.getChildren().add(node.getButton());
    }
  }

  /**
   * Adds the button for an equipment node to the pane corresponding to its floor
   *
   * @param node the node whose button is added to a pane
   */
  public void displayEquipmentNode(EquipmentNode node) {
    if (node.getFloor().equals("2")) {
      pane2.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("5")) {
      pane5.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("4")) {
      pane4.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("3")) {
      pane3.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("L1")) {
      paneL1.getChildren().add(node.getButton());
    } else if (node.getFloor().equals("L2")) {
      paneL2.getChildren().add(node.getButton());
    }
  }

  /**
   * Switches between the panes for each floor when the button for each floor is pressed
   *
   * @param event one of the floor buttons
   */
  public void changeMap(ActionEvent event) {
    pane2.setVisible(false);
    pane3.setVisible(false);
    pane4.setVisible(false);
    pane5.setVisible(false);
    paneL1.setVisible(false);
    paneL2.setVisible(false);

    if (event.getSource() == floorTwo) {
      pane2.setVisible(true);
      appController.setCurrentFloor("2");
      floorLabel.setText("2");
    } else if (event.getSource() == floorThree) {
      pane3.setVisible(true);
      appController.setCurrentFloor("3");
      floorLabel.setText("3");
    } else if (event.getSource() == floorFour) {
      pane4.setVisible(true);
      appController.setCurrentFloor("4");
      floorLabel.setText("4");
    } else if (event.getSource() == floorFive) {
      pane5.setVisible(true);
      appController.setCurrentFloor("5");
      floorLabel.setText("5");
    } else if (event.getSource() == lowerLevelOne) {
      paneL1.setVisible(true);
      appController.setCurrentFloor("L1");
      floorLabel.setText("L1");
    } else if (event.getSource() == lowerLevelTwo) {
      paneL2.setVisible(true);
      appController.setCurrentFloor("L2");
      floorLabel.setText("L2");
    } else if (event.getSource() == floorUp) {
      if (appController.getCurrentFloor().equals("L2")) {
        paneL1.setVisible(true);
        appController.setCurrentFloor("L1");
        floorLabel.setText("L1");
      } else if (appController.getCurrentFloor().equals("L1")) {
        pane2.setVisible(true);
        appController.setCurrentFloor("2");
        floorLabel.setText("2");
      } else if (appController.getCurrentFloor().equals("2")) {
        pane3.setVisible(true);
        appController.setCurrentFloor("3");
        floorLabel.setText("3");
      } else if (appController.getCurrentFloor().equals("3")) {
        pane4.setVisible(true);
        appController.setCurrentFloor("4");
        floorLabel.setText("4");
      } else if (appController.getCurrentFloor().equals("4")) {
        pane5.setVisible(true);
        appController.setCurrentFloor("5");
        floorLabel.setText("5");
      } else if (appController.getCurrentFloor().equals("5")) {
        pane5.setVisible(true);
        appController.setCurrentFloor("5");
        floorLabel.setText("5");
      }
    } else if (event.getSource() == floorDown) {
      if (appController.getCurrentFloor().equals("2")) {
        paneL1.setVisible(true);
        appController.setCurrentFloor("L1");
        floorLabel.setText("L1");
      } else if (appController.getCurrentFloor().equals("3")) {
        pane2.setVisible(true);
        appController.setCurrentFloor("2");
        floorLabel.setText("2");
      } else if (appController.getCurrentFloor().equals("4")) {
        pane3.setVisible(true);
        appController.setCurrentFloor("3");
        floorLabel.setText("3");
      } else if (appController.getCurrentFloor().equals("5")) {
        pane4.setVisible(true);
        appController.setCurrentFloor("4");
        floorLabel.setText("4");
      } else if (appController.getCurrentFloor().equals("L1")) {
        paneL2.setVisible(true);
        appController.setCurrentFloor("L2");
        floorLabel.setText("L2");
      } else if (appController.getCurrentFloor().equals("L2")) {
        paneL2.setVisible(true);
        appController.setCurrentFloor("L2");
        floorLabel.setText("L2");
      }
    }
  }

  void clearFields() {
    // nameField.clear();
    locationTypeDropdown.setText("Node Type");
    // requestName.setText("Node ID:");
  }

  public void typeMenu(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    locationTypeDropdown.setText(button.getText());
    addLocationTypeDropdown.setText(button.getText());
  }

  public void zoomableMap(ActionEvent event) {

    Node content = mapScroll.getContent();
    Group contentGroup = new Group();
    contentGroup.getChildren().add(content);
    mapScroll.setContent(contentGroup);
    if (event.getSource() == zoomIn) {
      Scale scaleTransform = new Scale(1.05, 1.05, 0, 0);
      scale *= 1.05;
      contentGroup.getTransforms().add(scaleTransform);

    } else if (event.getSource() == zoomOut && scale >= 1.05) {
      Scale scaleTransform = new Scale(.95, .95, 0, 0);
      scale *= 0.95;
      contentGroup.getTransforms().add(scaleTransform);
      if (mapScroll.getHvalue() > 0.8 || mapScroll.getVvalue() > 0.8) {
        mapScroll.setHvalue(0.6);
        mapScroll.setVvalue(0.6);
      }
    }
  }

  public double getMapXCoordFromClick(MouseEvent click) {
    panX = mapScroll.getHvalue() * (mapPane.getWidth() - mapScroll.getWidth() / scale);
    double panX2 =
        mapScroll.getHvalue() * 50; // no idea what these numbers are i guessed and checked
    if (scale == 1) {
      return (((click.getSceneX() - mapScroll.getLayoutX()) / scale) + panX - 8)
              * (croppedMapWidth / imagePaneWidth)
          + croppedMapXOffset;
    } else {
      return (((click.getSceneX() - mapScroll.getLayoutX()) / scale) + panX + panX2 - 8)
              * (croppedMapWidth / imagePaneWidth)
          + croppedMapXOffset;
    }
  }

  public double getMapYCoordFromClick(MouseEvent click) {
    panY = mapScroll.getVvalue() * (mapPane.getHeight() - mapScroll.getHeight() / scale);
    double panY2 =
        mapScroll.getVvalue() * 10; // no idea what these numbers are i guessed and checked
    if (scale == 1) {
      return (((click.getSceneY() - mapScroll.getLayoutY()) / scale) + panY - 8)
              * (croppedMapWidth / imagePaneWidth)
          + croppedMapYOffset;
    } else {
      return (((click.getSceneY() - mapScroll.getLayoutY()) / scale) + panY + panY2 - 8)
              * (croppedMapWidth / imagePaneWidth)
          + croppedMapYOffset;
    }
  }

  public void setCoordsOnMouseEvent(MouseEvent click) {
    try {
      locationEdit();
    } catch (NullPointerException e) {
    }
  }

  @FXML
  public void cleanEquip() {
    equipmentNodeManager.makeClean(currentEquipmentNode);
    currentEquipmentNode.resetLocation();
    populateEquipmentNodeData(currentEquipmentNode);
  }

  public void clearPage(ActionEvent actionEvent) {
    appController.clearPage();
  }

  public void changeStatus(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    requestStatusDropdown.setText(button.getText());
  }

  public void deselect() {
    requestEditPane.setVisible(false);
    locationEditPane.setVisible(false);
    locationAddPane.setVisible(false);
    equipmentViewPane.setVisible(false);
    sameLocationName.setVisible(false);
    sameLocationNameEdit.setVisible(false);

    clickPane.setVisible(false);
    locationFilterButton.setVisible(true);
    requestFilterButton.setVisible(true);
    equipmentFilterButton.setVisible(true);
  }

  public void hideFilter() {
    locationFilterButton.setVisible(false);
    requestFilterButton.setVisible(false);
    equipmentFilterButton.setVisible(false);
  }

  public void locationDelete(ActionEvent event) {
    if (currentLocationNode.getFloor().equals("5")) {
      pane5.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("4")) {
      pane4.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("3")) {
      pane3.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("2")) {
      pane2.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("L1")) {
      paneL1.getChildren().remove(currentLocationNode.getButton());
    } else if (currentLocationNode.getFloor().equals("l2")) {
      paneL2.getChildren().remove(currentLocationNode.getButton());
    }

    locationNodeManager.deleteNode(currentLocationNode.getNodeID());
    locationEditPane.setVisible(false);
  }

  public void requestDelete(ActionEvent event) {
    if (currentRequestNode.getFloor().equals("5")) {
      pane5.getChildren().remove(currentRequestNode.getButton());
    } else if (currentRequestNode.getFloor().equals("4")) {
      pane4.getChildren().remove(currentRequestNode.getButton());
    } else if (currentRequestNode.getFloor().equals("3")) {
      pane3.getChildren().remove(currentRequestNode.getButton());
    } else if (currentRequestNode.getFloor().equals("2")) {
      pane2.getChildren().remove(currentRequestNode.getButton());
    } else if (currentRequestNode.getFloor().equals("L1")) {
      paneL1.getChildren().remove(currentRequestNode.getButton());
    } else if (currentRequestNode.getFloor().equals("l2")) {
      paneL2.getChildren().remove(currentRequestNode.getButton());
    }

    requestNodeManager.deleteRequest(currentRequestNode);
    requestEditPane.setVisible(false);
    deselect();
  }

  public void locationAdd(ActionEvent event) {
    String name = addLocationName.getText();
    boolean flag = true;

    for (Location e : locationsHash.values()) {
      if (e.getLongName().equals(name)) {
        flag = false;
      }
    }

    if (addLocationTypeDropdown.getText().equals("Node Type")
        || addLocationName.getText().isEmpty()) {

    } else {
      if (flag) {
        int typeCount =
            (locationNodeManager.getTypeCount(
                addLocationTypeDropdown.getText(), appController.getCurrentFloor()));

        String nodeID =
            "A"
                + addLocationTypeDropdown.getText()
                + String.format("%03d", typeCount)
                + ((appController.getCurrentFloor().length() == 1)
                    ? ("0" + appController.getCurrentFloor())
                    : (appController.getCurrentFloor()));

        // check if long name is same

        Location newLocation =
            new Location(
                nodeID,
                (getMapXCoordFromClick(rightClick)),
                (getMapYCoordFromClick(rightClick)),
                appController.getCurrentFloor(),
                "TOWER",
                addLocationTypeDropdown.getText(),
                addLocationName.getText(),
                nodeID);

        displayLocationNode(
            locationNodeManager.addNode(
                newLocation, appController.getCurrentUser().getPermissionLevel()));
        deselect();
      } else {
        sameLocationName.setVisible(true);
      }
    }
  }

  public void requestTypeDropdown(ActionEvent event) {
    MenuItem button = (MenuItem) event.getSource();
    requestTypeDropdown.setText(button.getText());
  }

  public void toggleLocation(ActionEvent event) {
    if (locationToggle.isSelected()) {
      locationNodeManager.setVisibilityAll(true);
      for (CheckMenuItem e : locationFilterHash.keySet()) {
        e.setSelected(true);
      }
    }

    if (!locationToggle.isSelected()) {
      locationNodeManager.setVisibilityAll(false);
      for (CheckMenuItem e : locationFilterHash.keySet()) {
        e.setSelected(false);
      }
    } else {
      for (CheckMenuItem e : locationFilterHash.keySet()) {
        if (e.isSelected()) {
          locationNodeManager.setVisibilityOfType(locationFilterHash.get(e), true);
        }
      }
    }
    locationFilterButton.hide();
  }

  public void toggleRequest(ActionEvent event) {
    if (requestToggle.isSelected()) {
      requestNodeManager.setVisibilityAll(true);
      for (CheckMenuItem e : requestFilterHash.keySet()) {
        e.setSelected(true);
      }
    }

    if (!requestToggle.isSelected()) {
      requestNodeManager.setVisibilityAll(false);
      for (CheckMenuItem e : requestFilterHash.keySet()) {
        e.setSelected(false);
      }
    } else {
      for (CheckMenuItem e : requestFilterHash.keySet()) {
        if (e.isSelected()) {
          requestNodeManager.setVisibilityOfType(requestFilterHash.get(e), true);
        }
      }
    }
    requestFilterButton.hide();
  }

  public void toggleEquipment(ActionEvent event) {
    if (equipmentToggle.isSelected()) {
      equipmentNodeManager.setVisibilityAll(true);
      for (CheckMenuItem e : equipFilterHash.keySet()) {
        e.setSelected(true);
      }
    }

    if (!equipmentToggle.isSelected()) {
      equipmentNodeManager.setVisibilityAll(false);
      for (CheckMenuItem e : equipFilterHash.keySet()) {
        e.setSelected(false);
      }
    } else {
      for (CheckMenuItem e : equipFilterHash.keySet()) {
        if (e.isSelected()) {
          equipmentNodeManager.setVisibilityOfType(equipFilterHash.get(e), true);
        }
      }
    }
    equipmentFilterButton.hide();
  }

  public void locationFilter() {
    for (CheckMenuItem e : locationFilterHash.keySet()) {
      locationNodeManager.setVisibilityOfType(locationFilterHash.get(e), e.isSelected());
    }
    locationFilterButton.show();
  }

  public void requestFilter() {
    for (CheckMenuItem e : requestFilterHash.keySet()) {
      requestNodeManager.setVisibilityOfType(requestFilterHash.get(e), e.isSelected());
    }
    requestFilterButton.show();
  }

  public void equipmentFilter() {
    for (CheckMenuItem e : equipFilterHash.keySet()) {
      equipmentNodeManager.setVisibilityOfType(equipFilterHash.get(e), e.isSelected());
    }
    equipmentFilterButton.show();
  }

  public double getCroppedMapXOffset() {
    return croppedMapXOffset;
  }

  public double getCroppedMapYOffset() {
    return croppedMapYOffset;
  }

  public double getCroppedMapWidth() {
    return croppedMapWidth;
  }

  public double getImagePaneWidth() {
    return imagePaneWidth;
  }
}
