package edu.wpi.agileAngels;

import edu.wpi.agileAngels.Controllers.EmployeeManager;
import edu.wpi.agileAngels.Database.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

// This class is the backend of the DAO method.
// The objects communicate with the DB here
// Basically, front end shouldn't directly interact adb, it should interact with DAO classes
public class Adb {
  private static LocationsTable locationsTable = null;
  private static MedicalEquipmentTable medicalEquipmentTable = null;
  private static ServiceRequestTable serviceRequestTable = null;
  private static EmployeeTable employeeTable = null;
  private static EmployeeManager employeeManager = null;
  private static LocationDAOImpl locationDAO = null;
  private static RequestDAOImpl medRequestDAO = null;
  private static RequestDAOImpl labRequestDAO = null;
  private static MedEquipImpl equipmentDAO = null;
  private static RequestDAOImpl mainRequestImpl = null;
  private static RequestDAOImpl transportRequestImpl = null;
  private static RequestDAOImpl morgueRequestImpl = null;
  private static RequestDAOImpl mealRequestImpl = null;
  private static RequestDAOImpl giftRequestImpl = null;
  private static RequestDAOImpl sanitationRequestImpl = null;
  private static RequestDAOImpl laundryRequestImpl = null;

  /**
   * Creates database tables if they do not exist already.
   *
   * @throws SQLException
   */
  public void initialize() throws SQLException, IOException {

    // Apache Derby and table creation
    System.out.println("-------Embedded Apache Derby Connection Testing --------");
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      Class.forName("org.apache.derby.jdbc.ClientDriver");
    } catch (ClassNotFoundException e) {
      System.out.println("Apache Derby Driver not found. Add the classpath to your module.");
      System.out.println("For IntelliJ do the following:");
      System.out.println("File | Project Structure, Modules, Dependency tab");
      System.out.println("Add by clicking on the green plus icon on the right of the window");
      System.out.println(
          "Select JARs or directories. Go to the folder where the database JAR is located");
      System.out.println("Click OK, now you can compile your program and run it.");
      e.printStackTrace();
      return;
    }

    System.out.println("Apache Derby driver registered!");
    locationsTable = getLocationsTableInstance();
    medicalEquipmentTable = getMedicalEquipmentTableInstance();
    serviceRequestTable = getServiceRequestTableInstance();
    employeeTable = getEmployeeTableInstance();

    initializeHelper();
    DBconnection.switchConnection();
    initializeHelper();
    // After: should be embedded connection

    // Tries to get a connection

    if (DBconnection.getConnection() == null) {
      System.out.println("Connection has failed.");
      return;
    }

    System.out.println("Apache Derby connection established!");
  }

  /**
   * Helper method for the initialize function.
   *
   * @throws SQLException
   */
  private static void initializeHelper() throws SQLException {
    locationsTable.createTable();
    boolean medEquipEmpty = medicalEquipmentTable.createTable();
    serviceRequestTable.createTable();
    employeeTable.createTable();
    employeeManager = EmployeeManager.getInstance();
    employeeManager.readCSV();
    locationDAO = LocationDAOImpl.getInstance();
    locationDAO.csvRead();
    equipmentDAO = MedEquipImpl.getInstance();
    if (medEquipEmpty) {
      equipmentDAO.readCSV();
    } else {
      medicalEquipmentTable.getData();
    }
    labRequestDAO = RequestDAOImpl.getInstance("LabRequest");
    mainRequestImpl = RequestDAOImpl.getInstance("MaintenanceRequest");
    transportRequestImpl = RequestDAOImpl.getInstance("TransportRequest");
    morgueRequestImpl = RequestDAOImpl.getInstance("MorgueRequest");
    mealRequestImpl = RequestDAOImpl.getInstance("MealRequest");
    medRequestDAO = RequestDAOImpl.getInstance("MedRequest");
    giftRequestImpl = RequestDAOImpl.getInstance("GiftRequest");
    sanitationRequestImpl = RequestDAOImpl.getInstance("SanitationRequest");
    laundryRequestImpl = RequestDAOImpl.getInstance("LaundryRequest");
    resetServiceRequests();

    serviceRequestTable.getData();
  }

  /**
   * Get instance of location Table
   *
   * @return a singleton of a Locations Table
   */
  public static LocationsTable getLocationsTableInstance() {
    if (locationsTable == null) {

      locationsTable = new LocationsTable();
      return locationsTable;
    }
    return locationsTable;
  }

  /**
   * Get instance of Medical Equipment Table
   *
   * @return a singleton of a MedicalEquipment Table
   */
  public static MedicalEquipmentTable getMedicalEquipmentTableInstance() {
    if (medicalEquipmentTable == null) {

      medicalEquipmentTable = new MedicalEquipmentTable();
      return medicalEquipmentTable;
    }
    return medicalEquipmentTable;
  }

  /**
   * Get instance of Service Request Table
   *
   * @return a singleton of a Service Request Table
   */
  public static ServiceRequestTable getServiceRequestTableInstance() {
    if (serviceRequestTable == null) {

      serviceRequestTable = new ServiceRequestTable();
    }
    return serviceRequestTable;
  }

  /**
   * Get instance of Employee Table
   *
   * @return a singleton of an Employee Table
   */
  public static EmployeeTable getEmployeeTableInstance() {
    if (employeeTable == null) {

      employeeTable = new EmployeeTable();
    }
    return employeeTable;
  }

  /**
   * Adds a request to the request database table.
   *
   * @param request new Request
   * @return True if successful, false if not.
   */
  public static boolean addRequest(Request request) {
    return serviceRequestTable.add(request);
  }
  //  return serviceRequestTable.add(request)

  /**
   * Removes a request from the request database table.
   *
   * @param name Request name
   * @return True if successful, false if not.
   */
  public static boolean removeRequest(String name) {
    return serviceRequestTable.delete(name);
  }

  /**
   * Updates different attributes for a request in the table.
   *
   * @param request updated Request
   * @return True if successful, false if not.
   */
  public static boolean updateRequest(Request request) {
    return serviceRequestTable.update(request);
  }

  /**
   * Adds a new location to the locations table.
   *
   * @param location Location
   * @return True if successful, false if not
   */
  public static boolean addLocation(Location location) {
    return locationsTable.add(location);
  }

  /**
   * Deletes a location from the locations table.
   *
   * @param nodeID Location id
   * @return True if successful, false if not
   */
  public static boolean removeLocation(String nodeID) {
    return locationsTable.delete(nodeID);
  }

  /**
   * Updates a location on the table with updated attributes.
   *
   * @param location updated Location
   * @return True if successful, false if not
   */
  public static boolean updateLocation(Location location) {
    return locationsTable.update(location);
  }

  /**
   * Adds one medical equipment to the medical equipment table.
   *
   * @param medicalEquip new MedicalEquip
   * @return True if successful, false if not
   */
  public static boolean addMedicalEquipment(MedicalEquip medicalEquip) {
    return medicalEquipmentTable.add(medicalEquip);
  }

  /**
   * Deletes one medical equipment from the medical equipment table
   *
   * @param ID MedicalEquip id
   * @return True if successful, false if not
   */
  public static boolean removeMedicalEquipment(String ID) {
    return medicalEquipmentTable.delete(ID);
  }

  /**
   * Updates one medical equipment in the medical equipment table
   *
   * @param medicalEquip updated MedicalEquip
   * @return True if successful, false if not
   */
  public static boolean updateMedicalEquipment(MedicalEquip medicalEquip) {
    return medicalEquipmentTable.update(medicalEquip);
  }

  /**
   * Adds a medical equipment request to the HashMap
   *
   * @param request Medical equipment request
   */
  public static void addMedRequest(Request request) {
    medRequestDAO.uploadRequest(request);
  }

  /**
   * Adds a meal request to the HashMap
   *
   * @param request Meal request
   */
  public static void addMealRequest(Request request) {
    mealRequestImpl.uploadRequest(request);
  }

  /**
   * Adds a lab request to the HashMap
   *
   * @param request Lab request
   */
  public static void addLabRequest(Request request) {
    labRequestDAO.uploadRequest(request);
  }

  /**
   * Adds a maintenance request to the HashMap
   *
   * @param request Maintenance request
   */
  public static void addMainRequest(Request request) {
    mainRequestImpl.uploadRequest(request);
  }

  /**
   * Adds a patient transport request to the HashMap
   *
   * @param request Patient transport request
   */
  public static void addTransportRequest(Request request) {
    transportRequestImpl.uploadRequest(request);
  }

  /**
   * Adds a morgue request to the HashMap
   *
   * @param request Morgue request
   */
  public static void addMorgueRequest(Request request) {
    morgueRequestImpl.uploadRequest(request);
  }

  /**
   * Adds a gift request to the HashMap
   *
   * @param request Gift request
   */
  public static void addGiftRequest(Request request) {
    giftRequestImpl.uploadRequest(request);
  }

  /**
   * Adds a sanitation request to the HashMap
   *
   * @param request Sanitation request
   */
  public static void addSanitationRequest(Request request) {
    sanitationRequestImpl.uploadRequest(request);
  }

  /**
   * Adds a laundry request to the HashMap
   *
   * @param request Laundry request
   */
  public static void addLaundryRequest(Request request) {
    laundryRequestImpl.uploadRequest(request);
  }

  /**
   * Adds a new employee to the employee table
   *
   * @param employee Employee name
   * @return True if successful, false if not
   */
  public static boolean addEmployee(Employee employee) {
    return employeeTable.add(employee);
  }

  /**
   * Reads employee csv file
   *
   * @return True if successful
   */
  public static boolean readCSVEmployees() {
    employeeManager.resetAllEmployees();
    employeeManager.readCSV();
    return true;
  }

  /** Resets all service request HashMaps */
  public static void resetServiceRequests() {
    medRequestDAO.setCount(0);
    medRequestDAO.resetData();

    labRequestDAO.setCount(0);
    labRequestDAO.resetData();

    mealRequestImpl.setCount(0);
    mealRequestImpl.resetData();

    mainRequestImpl.setCount(0);
    mainRequestImpl.resetData();

    transportRequestImpl.setCount(0);
    transportRequestImpl.resetData();

    morgueRequestImpl.setCount(0);
    morgueRequestImpl.resetData();

    giftRequestImpl.setCount(0);
    giftRequestImpl.resetData();

    sanitationRequestImpl.setCount(0);
    sanitationRequestImpl.resetData();

    laundryRequestImpl.setCount(0);
    laundryRequestImpl.resetData();

    try {
      RequestDAOImpl.getInstance("AllRequests").setCount(0);
      RequestDAOImpl.getInstance("AllRequests").resetData();
    } catch (SQLException sqlException) {
    }
  }

  /**
   * Gets the HashMap of all the employees.
   *
   * @return HashMap of employees
   */
  public static HashMap getEmployees() {
    return employeeManager.getAllEmployees();
  }

  /**
   * Resets DAO objects of equipment on hashmap and reads them from database table
   *
   * @return HashMap of medical equipment requests
   * @throws SQLException
   */
  public static HashMap getMedEquipment() throws SQLException {
    equipmentDAO.resetAllEquips();
    medicalEquipmentTable.getData();
    return equipmentDAO.getAllMedicalEquipment();
  }

  /**
   * Adds a medical equipment object to the HashMap
   *
   * @param equip Medical equipment
   */
  public static void addMedEquip(MedicalEquip equip) {
    equipmentDAO.addEquipment(equip);
  }

  /**
   * Reads locations csv file
   *
   * @return True if successful
   */
  public static boolean readCSVLocations() {
    locationDAO.resetAllLocations();
    locationDAO.csvRead();
    return true;
  }

  /**
   * Gets the HashMap of locations
   *
   * @return Locations HashMap
   */
  public static HashMap getLocations() {
    return locationDAO.getAllLocations();
  }

  /**
   * Removes an employee from the employee table
   *
   * @param name Employee name
   * @return True if successful, false if not
   */
  public static boolean removeEmployee(String name) {
    return employeeTable.delete(name);
  }

  public static ArrayList<String> getFreeEmployees() throws SQLException {
    return serviceRequestTable.freeEmployees();
  }

  /**
   * Updates an employee's information in the employee table
   *
   * @param employee updated Employee
   * @return True if successful, false if not
   */
  public static boolean updateEmployee(Employee employee) {
    return employeeTable.update(employee);
  }

  public static void populateServiceRequests() {
    serviceRequestTable.getData();
  }

  public static void resetMedEquipment() {
    equipmentDAO.resetAllEquips();
  }

  public static void populateMedicalEquipment() {
    try {
      medicalEquipmentTable.getData();
    } catch (SQLException sqlException) {
    }
  }
}
