package edu.wpi.agileAngels.Controllers;

import edu.wpi.agileAngels.Database.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.input.MouseEvent;

public class EquipmentNodeManager {
  private MapsController mapsController;
  private MedEquipImpl equipDAO = MedEquipImpl.getInstance();
  private HashMap<String, EquipmentNode> nodes = new HashMap<>();
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  HashMap<String, Location> locationsHash = locDAO.getAllLocations();

  public EquipmentNodeManager(MapsController mapsController) throws SQLException {
    this.mapsController = mapsController;
  }

  // gets all equipment from the DB and creates nodes from them
  void createNodesFromDB() throws SQLException {
    ArrayList<MedicalEquip> equipList = new ArrayList<>(equipDAO.getAllMedicalEquipment().values());
    for (MedicalEquip equip : equipList) {
      mapsController.displayEquipmentNode(addNode(equip));
    }
  }

  public void editEquipmentLocation(EquipmentNode equipmentNode, Location newLocation) {
    equipmentNode.setLocation(newLocation);
    equipDAO.updateEquipmentLocation(equipmentNode.getMedEquip(), newLocation);
  }

  public ArrayList<Location> getLocationsList() {
    return new ArrayList<Location>(locationsHash.values());
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

  public void setCoordsOnMouseEvent(MouseEvent click) {
    mapsController.setCoordsOnMouseEvent(click);
  }

  public void setDraggedNodeCoords(MouseEvent mouseEvent) {
    mapsController.setCoordsOnMouseEvent(mouseEvent);
  }

  EquipmentNode addNode(MedicalEquip medEquip) throws SQLException {
    EquipmentNode equipNode = new EquipmentNode(medEquip, this);
    nodes.put(equipNode.getMedEquip().getID(), equipNode);
    return equipNode;
  }

  // gets called on button press and gets the node data
  void loadNode(EquipmentNode equipNode) {
    mapsController.populateEquipmentNodeData(equipNode);
  }

  void makeClean(EquipmentNode node) {
    MedicalEquip medEquip = node.getMedEquip();
    if (!medEquip.isClean()) {
      equipDAO.updateMedicalCleanliness(medEquip, true);
      if (medEquip.getType().equals("XRayMachine") || medEquip.getType().equals("InfusionPump")) {
        if (medEquip.getLocation().getFloor().equals("3")) {
          equipDAO.updateEquipmentLocation(medEquip, locationsHash.get("ASTOR00103"));
          node.setLocation(locationsHash.get("ASTOR00103"));
        } else if (medEquip.getLocation().getFloor().equals("4")) {
          equipDAO.updateEquipmentLocation(medEquip, locationsHash.get("ASTOR00104"));
          node.setLocation(locationsHash.get("ASTOR00104"));
        } else if (medEquip.getLocation().getFloor().equals("5")) {
          equipDAO.updateEquipmentLocation(medEquip, locationsHash.get("ASTOR00105"));
          node.setLocation(locationsHash.get("ASTOR00105"));
        }
      } else {
        if (medEquip.getLocation().getFloor().equals("3")) {
          equipDAO.updateEquipmentLocation(medEquip, locationsHash.get("ASTOR00303"));
          node.setLocation(locationsHash.get("ASTOR00303"));
        } else if (medEquip.getLocation().getFloor().equals("4")) {
          equipDAO.updateEquipmentLocation(medEquip, locationsHash.get("ASTOR00304"));
          node.setLocation(locationsHash.get("ASTOR00304"));
        } else if (medEquip.getLocation().getFloor().equals("5")) {
          equipDAO.updateEquipmentLocation(medEquip, locationsHash.get("ASTOR00305"));
          node.setLocation(locationsHash.get("ASTOR00305"));
        }
      }
    }
  }

  public void updateEquipmentNode(String ID) {
    nodes.get(ID).updateLocation();
  }

  public void updateEquipObject(Request request, String status) {
    // set the status and location of the medicalEquipment object
    // corresponding to the request
    if (request.getAttribute2().equals("Clean")) {
      // if it's a request to clean equipment
      if (request.getMedicalEquip() != null) {
        if (status.equals("Not Started")) {
          equipDAO.updateStatus(request.getMedicalEquip(), "inUse");
        } else if (status.equals("In Progress")) {
          equipDAO.updateStatus(request.getMedicalEquip(), "inUse");
          if (request.getLocation().getFloor().equals("3")) {
            equipDAO.updateEquipmentLocation(
                request.getMedicalEquip(), locationsHash.get("ADIRT00103"));
          } else if (request.getLocation().getFloor().equals("4")) {
            equipDAO.updateEquipmentLocation(
                request.getMedicalEquip(), locationsHash.get("ADIRT00104"));
          } else if (request.getLocation().getFloor().equals("5")) {
            equipDAO.updateEquipmentLocation(
                request.getMedicalEquip(), locationsHash.get("ADIRT00105"));
          }
        } else if (status.equals("Complete")) {
          equipDAO.updateMedicalCleanliness(request.getMedicalEquip(), true);
          equipDAO.updateStatus(request.getMedicalEquip(), "available");
          if (request.getType().equals("InfusionPump")) {
            if (request.getLocation().getFloor().equals("3")) {
              equipDAO.updateEquipmentLocation(
                  request.getMedicalEquip(), locationsHash.get("ASTOR00103"));
            } else if (request.getLocation().getFloor().equals("4")) {
              equipDAO.updateEquipmentLocation(
                  request.getMedicalEquip(), locationsHash.get("ASTOR00104"));
            } else if (request.getLocation().getFloor().equals("5")) {
              equipDAO.updateEquipmentLocation(
                  request.getMedicalEquip(), locationsHash.get("ASTOR00105"));
            }
          } else {
            if (request.getLocation().getFloor().equals("3")) {
              equipDAO.updateEquipmentLocation(
                  request.getMedicalEquip(), locationsHash.get("ASTOR00303"));
            } else if (request.getLocation().getFloor().equals("4")) {
              equipDAO.updateEquipmentLocation(
                  request.getMedicalEquip(), locationsHash.get("ASTOR00304"));
            } else if (request.getLocation().getFloor().equals("5")) {
              equipDAO.updateEquipmentLocation(
                  request.getMedicalEquip(), locationsHash.get("ASTOR00305"));
            }
          }
        }
      }
    } else {
      // if it's not a request to clean equipment
      if (request.getMedicalEquip() != null) {
        if (status.equals("Not Started")) {
          equipDAO.updateStatus(request.getMedicalEquip(), "inUse");
        } else if (status.equals("In Progress")) {
          equipDAO.updateStatus(request.getMedicalEquip(), "inUse");
          equipDAO.updateEquipmentLocation(request.getMedicalEquip(), request.getLocation());
        } else if (status.equals("Complete")) {
          equipDAO.updateMedicalCleanliness(request.getMedicalEquip(), false);
          equipDAO.updateStatus(request.getMedicalEquip(), "available");
          if (request.getLocation().getFloor().equals("3")) {
            equipDAO.updateEquipmentLocation(
                request.getMedicalEquip(), locationsHash.get("ADIRT00103"));
          } else if (request.getLocation().getFloor().equals("4")) {
            equipDAO.updateEquipmentLocation(
                request.getMedicalEquip(), locationsHash.get("ADIRT00104"));
          } else if (request.getLocation().getFloor().equals("5")) {
            equipDAO.updateEquipmentLocation(
                request.getMedicalEquip(), locationsHash.get("ADIRT00105"));
          }
        }
      }
    }
    updateEquipmentNode(request.getMedicalEquip().getID());
  }

  public void setVisibilityOfType(String type, boolean b) {
    for (EquipmentNode node : nodes.values()) {
      if (node.getMedEquip().getType().equals(type)) {
        node.getButton().setVisible(b);
      }
    }
  }

  public void setVisibilityAll(boolean b) {
    for (EquipmentNode node : nodes.values()) {
      node.getButton().setVisible(b);
    }
  }
}
