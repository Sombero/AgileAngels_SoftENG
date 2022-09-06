package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.input.MouseEvent;

public class RequestNodeManager {

  private MapsController mapsController;
  private RequestDAOImpl medRequestDAO = RequestDAOImpl.getInstance("MedRequest");
  private RequestDAOImpl labRequestDAO = RequestDAOImpl.getInstance("LabRequest");
  private RequestDAOImpl MealRequestDAO = RequestDAOImpl.getInstance("MealRequest");
  private RequestDAOImpl SanitationRequestDAO = RequestDAOImpl.getInstance("SanitationRequest");
  private RequestDAOImpl LaundryRequestDAO = RequestDAOImpl.getInstance("LaundryRequest");
  private RequestDAOImpl MaintenanceRequestDAO = RequestDAOImpl.getInstance("MaintenanceRequest");
  private RequestDAOImpl TransportRequestDAO = RequestDAOImpl.getInstance("TransportRequest");
  private RequestDAOImpl GiftRequestDAO = RequestDAOImpl.getInstance("GiftRequest");
  private RequestDAOImpl MorgueRequestDAO = RequestDAOImpl.getInstance("MorgueRequest");
  private RequestDAOImpl AllRequestDAO = RequestDAOImpl.getInstance("AllRequests");
  private EmployeeManager empDAO = EmployeeManager.getInstance();
  private HashMap<String, RequestNode> nodes = new HashMap<>();
  private LocationDAOImpl locationDAO = LocationDAOImpl.getInstance();
  HashMap<String, Location> locationsHash = locationDAO.getAllLocations();
  ArrayList<Location> locationsList = new ArrayList<Location>(locationsHash.values());

  public HashMap<String, Employee> employeeHash = empDAO.getAllEmployees();

  public RequestNodeManager(MapsController mapsController) throws SQLException {
    this.mapsController = mapsController;
  }

  public ArrayList<Location> getLocationsList() {
    return locationsList;
  }

  public ArrayList<String> getTypes(RequestNode node) {
    return labRequestDAO.getRequestTypes().get(node.getName().substring(0, 3));
  }

  // gets all locations from the DB and creates nodes from them
  void createNodesFromDB() throws SQLException {
    ArrayList<Request> requestsList = new ArrayList<>(medRequestDAO.getAllRequests().values());
    requestsList.addAll(labRequestDAO.getAllRequests().values());
    requestsList.addAll(MealRequestDAO.getAllRequests().values());
    requestsList.addAll(SanitationRequestDAO.getAllRequests().values());
    requestsList.addAll(LaundryRequestDAO.getAllRequests().values());
    requestsList.addAll(MaintenanceRequestDAO.getAllRequests().values());
    requestsList.addAll(GiftRequestDAO.getAllRequests().values());
    requestsList.addAll(MorgueRequestDAO.getAllRequests().values());
    requestsList.addAll(TransportRequestDAO.getAllRequests().values());
    for (Request request : requestsList) {
      mapsController.displayRequestNode(addNode(request));
    }
  }

  public void editRequestLocation(RequestNode request, Location newLocation) {

    if (request.getName().substring(0, 3).equals("Lab")) {
      request.setLocation(newLocation);
      labRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("Med")) {
      request.setLocation(newLocation);
      medRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("Tra")) {
      request.setLocation(newLocation);
      TransportRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("Mea")) {
      request.setLocation(newLocation);
      MealRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("Gif")) {
      request.setLocation(newLocation);
      GiftRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("San")) {
      request.setLocation(newLocation);
      SanitationRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("Mai")) {
      request.setLocation(newLocation);
      MaintenanceRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("Mor")) {
      request.setLocation(newLocation);
      MorgueRequestDAO.updateLocation(request.getRequest(), newLocation);
    } else if (request.getName().substring(0, 3).equals("Lau")) {
      request.setLocation(newLocation);
      LaundryRequestDAO.updateLocation(request.getRequest(), newLocation);
    }

    if (request.getRequest().getName().substring(0, 3).equals("Med")
        && request.getRequest().getMedicalEquip() != null) {
      request.getRequest().getMedicalEquip().setLocation(newLocation);
      mapsController.updateEquipNode(request.getRequest().getMedicalEquip().getID());
    }
  }

  RequestNode addNode(Request request) throws SQLException {
    RequestNode requestNode = new RequestNode(request, this);
    nodes.put(requestNode.getName(), requestNode);
    return requestNode;
  }

  // gets called on button press and gets the node data
  void loadNode(RequestNode requestNode) {
    mapsController.populateRequestNodeData(requestNode);
  }

  public double getCroppedMapXOffset() {
    return mapsController.getCroppedMapXOffset();
  }

  public double getCroppedMapYOffset() {
    return mapsController.getCroppedMapYOffset();
  }

  public double getCroppedMapWidth() {
    return mapsController.getCroppedMapWidth();
  }

  public double getImagePaneWidth() {
    return mapsController.getImagePaneWidth();
  }

  public double getMapXCoordFromClick(MouseEvent click) {
    return mapsController.getMapXCoordFromClick(click);
  }

  public double getMapYCoordFromClick(MouseEvent click) {
    return mapsController.getMapYCoordFromClick(click);
  }

  public void setDraggedNodeCoords(MouseEvent mouseEvent) {
    mapsController.setCoordsOnMouseEvent(mouseEvent);
  }

  public void updateRequest(RequestNode request) {
    if (request.getName().startsWith("Lab")) {
      labRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      labRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("Med")) {
      medRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      medRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("Gif")) {
      GiftRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      GiftRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("Lau")) {
      LaundryRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      LaundryRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("Mai")) {
      MaintenanceRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      MaintenanceRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("Mea")) {
      MealRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      MealRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("Mor")) {
      MorgueRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      MorgueRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("Tra")) {
      TransportRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      TransportRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    } else if (request.getName().startsWith("San")) {
      SanitationRequestDAO.updateEmployeeName(request.getRequest(), request.getEmployee());
      SanitationRequestDAO.updateStatus(request.getRequest(), request.getStatus());
    }
  }

  public void deleteRequest(RequestNode request) {
    nodes.remove(request);
    Request req = request.getRequest();
    if (req.getName().startsWith("Med")) {
      medRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("Lab")) {
      labRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("Gif")) {
      GiftRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("Lau")) {
      LaundryRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("Mai")) {
      MaintenanceRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("Mea")) {
      MealRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("Mor")) {
      MorgueRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("Tra")) {
      TransportRequestDAO.deleteRequest(req);
    } else if (req.getName().startsWith("San")) {
      SanitationRequestDAO.deleteRequest(req);
    }
  }

  public void setVisibilityOfType(String type, Boolean b) {
    for (RequestNode node : nodes.values()) {
      if (node.getName().substring(0, 3).equals(type)) {
        node.getButton().setVisible(b);
      }
    }
  }

  public void setVisibilityAll(boolean selected) {
    for (RequestNode node : nodes.values()) {
      node.getButton().setVisible(selected);
    }
  }
}
