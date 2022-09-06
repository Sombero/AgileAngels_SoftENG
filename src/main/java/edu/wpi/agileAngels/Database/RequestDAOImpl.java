package edu.wpi.agileAngels.Database;

import edu.wpi.agileAngels.Adb;
import edu.wpi.agileAngels.Controllers.EmployeeManager;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

// Implementation of RequestDAO
public class RequestDAOImpl implements RequestDAO {
  private String CSV_FILE_PATH;
  private HashMap<String, Request> reqData = new HashMap();
  private int count;
  private String DAOtype;

  // Implementations for each type of request, employees, and locations
  private static EmployeeManager empManager = null;
  private LocationDAOImpl locDAO = LocationDAOImpl.getInstance();
  private static RequestDAOImpl AllRequestsDAO = null;
  private static RequestDAOImpl MedrequestDAO = null;
  private static RequestDAOImpl LabrequestDAO = null;
  private static RequestDAOImpl SanDAO = null;
  private static RequestDAOImpl MealDAO = null;
  private static RequestDAOImpl GiftDAO = null;
  private static RequestDAOImpl LaundryDAO = null;
  private static RequestDAOImpl MaintenanceDAO = null;
  private static RequestDAOImpl TransportDAO = null;
  private static RequestDAOImpl MorgueDAO = null;

  HashMap<String, ArrayList> requestTypes = new HashMap<>();

  // Arraylists of each request's types
  ArrayList labTypes = new ArrayList<String>();
  ArrayList equipTypes = new ArrayList<String>();
  ArrayList sanitationTypes = new ArrayList<String>();
  ArrayList mealTypes = new ArrayList<String>();
  ArrayList giftTypes = new ArrayList<String>();
  ArrayList transportTypes = new ArrayList<String>();

  public ArrayList getLabTypes() {
    labTypes.add("Blood Test");
    labTypes.add("Urine Test");
    labTypes.add("Tumor Marker");
    labTypes.add("COVID-19 Test");
    return labTypes;
  }

  public ArrayList getEquipTypes() {
    equipTypes.add("XRayMachine");
    equipTypes.add("InfusionPump");
    equipTypes.add("Recliner");
    equipTypes.add("Bed");
    return equipTypes;
  }

  public ArrayList getSanitationTypes() {
    sanitationTypes.add("Clean Spill");
    sanitationTypes.add("Clean Room");
    sanitationTypes.add("Clean Exam");
    sanitationTypes.add("Janitorial");
    sanitationTypes.add("Other");
    return sanitationTypes;
  }

  public ArrayList getMealTypes() {
    mealTypes.add("Salad");
    mealTypes.add("Steak");
    mealTypes.add("Pasta");
    mealTypes.add("Beans");
    mealTypes.add("Fish");
    return mealTypes;
  }

  public ArrayList getGiftTypes() {
    giftTypes.add("Balloons");
    giftTypes.add("Flowers");
    giftTypes.add("Card");
    return giftTypes;
  }

  public ArrayList getTransportTypes() {
    transportTypes.add("Move Room");
    transportTypes.add("Move Hospital");
    transportTypes.add("Release");
    return transportTypes;
  }

  public RequestDAOImpl(HashMap<String, Request> reqData, int count, String type)
      throws SQLException {

    this.CSV_FILE_PATH = "./Requests.csv";
    this.reqData = reqData;
    this.count = count;
    this.DAOtype = type;
    requestTypes.put("Mea", getMealTypes());
    requestTypes.put("Med", getEquipTypes());
    requestTypes.put("Lab", getLabTypes());
    requestTypes.put("San", getSanitationTypes());
    requestTypes.put("Gif", getGiftTypes());
    requestTypes.put("Tra", getTransportTypes());
  }

  public HashMap<String, ArrayList> getRequestTypes() {
    return requestTypes;
  }

  /**
   * Ensures only one instance of each type of RequestDAOImpl is made for each type of request
   *
   * @param type Request type
   * @return RequestDAOImpl of specified request type
   * @throws SQLException
   */
  public static RequestDAOImpl getInstance(String type) throws SQLException {
    HashMap<String, Request> data = new HashMap();

    if (0 == type.compareTo("MedRequest")) {
      if (MedrequestDAO == null) {
        MedrequestDAO = new RequestDAOImpl(data, 1, "MedRequest");
      }
      return MedrequestDAO;
    } else if (0 == type.compareTo("LabRequest")) {
      if (LabrequestDAO == null) {
        LabrequestDAO = new RequestDAOImpl(data, 1, "LabRequest");
      }
      return LabrequestDAO;
    } else if (0 == type.compareTo("MealRequest")) {
      if (MealDAO == null) {
        MealDAO = new RequestDAOImpl(data, 1, "MealRequest");
      }
      return MealDAO;
    } else if (0 == type.compareTo("SanitationRequest")) {
      if (SanDAO == null) {
        SanDAO = new RequestDAOImpl(data, 1, "SanitationRequest");
      }
      return SanDAO;
    } else if (0 == type.compareTo("LaundryRequest")) {
      if (LaundryDAO == null) {
        LaundryDAO = new RequestDAOImpl(data, 1, "LaundryRequest");
      }
      return LaundryDAO;
    } else if (0 == type.compareTo("MaintenanceRequest")) {
      if (MaintenanceDAO == null) {
        MaintenanceDAO = new RequestDAOImpl(data, 1, "MaintenanceRequest");
      }
      return MaintenanceDAO;
    } else if (0 == type.compareTo("TransportRequest")) {
      if (TransportDAO == null) {
        TransportDAO = new RequestDAOImpl(data, 1, "TransportRequest");
      }
      return TransportDAO;
    } else if (0 == type.compareTo("GiftRequest")) {
      if (GiftDAO == null) {
        GiftDAO = new RequestDAOImpl(data, 1, "GiftRequest");
      }
      return GiftDAO;
    } else if (0 == type.compareTo("MorgueRequest")) {
      if (MorgueDAO == null) {
        MorgueDAO = new RequestDAOImpl(data, 1, "MorgueRequest");
      }
      return MorgueDAO;
    } else if (0 == type.compareTo("AllRequests")) {
      if (AllRequestsDAO == null) {
        AllRequestsDAO = new RequestDAOImpl(data, 1, "AllRequests");
      }
      return AllRequestsDAO;
    }
    return null;
  }

  /**
   * Gets the HashMap of all requests
   *
   * @return HashMap of all requests
   */
  public HashMap<String, Request> getAllRequests() {
    return this.reqData;
  }

  public void updateRequest(Request request) {
    Adb.updateRequest(request);
  }

  public void updateEmployeeName(Request request, String newName) {
    this.reqData
        .get(request.getName())
        .setEmployee((Employee) EmployeeManager.getInstance().getAllEmployees().get(newName));

    Adb.updateRequest(request);
  }

  public void updateRequestType(Request request, int requestType) {
    request.setRequestType(requestType);
  }

  public void updateType(Request request, String newType) {
    request.setType(newType);
    Adb.updateRequest(request);
  }

  public void updateLocation(Request request, Location newLocation) {
    request.setLocation(newLocation);
    Adb.updateRequest(request);
  }

  public void updateDescription(Request request, String description) {
    request.setDescription(description);
    Adb.updateRequest(request);
  }

  public void updateStatus(Request request, String newStatus) {
    request.setStatus(newStatus);
    Adb.updateRequest(request);
  }

  public void updateAttribute2(Request req, String dest) {
    req.setAttribute2(dest);
    Adb.updateRequest(req);
  }

  public void updateAttribute1(Request req, String dest) {
    req.setAttribute1(dest);
    Adb.updateRequest(req);
  }

  public void deleteRequest(Request request) {
    this.reqData.remove(request.getName());
    try {
      RequestDAOImpl.getInstance("AllRequests").reqData.remove(request.getName());
    } catch (SQLException sqlException) {
    }

    String name = request.getName();
    Adb.removeRequest(name);
  }

  /**
   * Adds a new request to the HashMap and database table
   *
   * @param request Service request
   */
  public void addRequest(Request request) {
    ++this.count;
    String letter = "";
    if (0 == DAOtype.compareTo("MedRequest")) {
      letter = "Med";
    } else if (0 == DAOtype.compareTo("LabRequest")) {
      letter = "Lab";
    } else if (0 == DAOtype.compareTo("SanitationRequest")) {
      letter = "San";
    } else if (0 == DAOtype.compareTo("MealRequest")) {
      letter = "Meal";
    } else if (0 == DAOtype.compareTo("TransportRequest")) {
      letter = "Tran";
    } else if (0 == DAOtype.compareTo("GiftRequest")) {
      letter = "Gift";
    } else if (0 == DAOtype.compareTo("LaundryRequest")) {
      letter = "Laundry";
    } else if (0 == DAOtype.compareTo("MaintenanceRequest")) {
      letter = "Main";
    } else if (0 == DAOtype.compareTo("MorgueRequest")) {
      letter = "Morgue";
    }

    letter = letter + Integer.toString(this.count);
    request.setName(letter);
    this.reqData.put(letter, request);
    // System.out.println("ADDS A REQUEST: " + letter);
    try {
      RequestDAOImpl.getInstance("AllRequests").reqData.put(letter, request);
    } catch (SQLException sqlException) {
    }

    Adb.addRequest(request);
  }

  /** Reads from the service request csv file. */
  public void csvRead() {
    String line = "";
    String splitBy = ",";

    try {
      BufferedReader br = new BufferedReader(new FileReader(this.CSV_FILE_PATH));
      boolean OnHeader = false;
      line.split(splitBy);

      while ((line = br.readLine()) != null) {
        if (OnHeader) {
          String[] values = line.split(splitBy);
          typeofDAO(values);
        } else {
          OnHeader = true;
        }
      }
    } catch (IOException var7) {
      var7.printStackTrace();
    } catch (SQLException var8) {
      var8.printStackTrace();
    }
  }

  // UHHHH fix this
  private void typeofDAO(String[] values) throws SQLException {
    if (values[0].substring(0, 3).compareTo("Med") == 0 && DAOtype.compareTo("MedRequest") == 0) {
      makeRequest(values);
    } else if (values[0].substring(0, 4).compareTo("Meal") == 0
        && DAOtype.compareTo("MealRequest") == 0) {
      makeRequest(values);
    } else if (values[0].substring(0, 3).compareTo("San") == 0
        && DAOtype.compareTo("SanitationRequest") == 0) {
      makeRequest(values);
    } else if (values[0].substring(0, 1).compareTo("L") == 0
        && DAOtype.compareTo("LabRequest") == 0) {
      makeRequest(values);
    } else if (values[0].substring(0, 4).compareTo("Main") == 0
        && DAOtype.compareTo("MaintenanceRequest") == 0) {
      makeRequest(values);
    } else if (values[0].substring(0, 4).compareTo("Tran") == 0
        && DAOtype.compareTo("TransportRequest") == 0) { // Now this is an issue!
      makeRequest(values);
    } else if (values[0].substring(0, 4).compareTo("Gift") == 0
        && DAOtype.compareTo("GiftRequest") == 0) {
      makeRequest(values);
    } else if (values[0].substring(0, 4).compareTo("Morg") == 0
        && DAOtype.compareTo("MorgueRequest") == 0) {
      makeRequest(values);
    }
    return;
  }

  /**
   * Adds a service request to the HashMap and database table depending on a string of values
   *
   * @param values
   * @throws SQLException
   */
  private void makeRequest(String[] values) throws SQLException {
    Request request =
        new Request(
            values[0],
            findEmployee(values[1]),
            findLocation(values[2]),
            values[3],
            values[4],
            values[5],
            values[6],
            values[7]);
    this.reqData.put(values[0], request);
    this.count++;
    try {
      RequestDAOImpl.getInstance("AllRequests").reqData.put(values[0], request);
      RequestDAOImpl.getInstance("AllRequests").incrementCount();
    } catch (SQLException sqlException) {
    }
    Adb.addRequest(request);
  }

  private Employee findEmployee(String value) throws SQLException {
    Employee employee;
    HashMap<String, Employee> employeeData = EmployeeManager.getInstance().getAllEmployees();

    employee = employeeData.get(value);
    return employee;
  }

  private Location findLocation(String value) {
    Location location;
    HashMap<String, Location> locationData = locDAO.getAllLocations();
    location = locationData.get(value);
    return location;
  }

  public void outputCSVFile() {
    String csvFilePath = "./RequestsOUT.csv";

    try {

      String sql = "SELECT * FROM ServiceRequests";

      Connection connection = DBconnection.getConnection();

      Statement statement = connection.createStatement();

      ResultSet result = statement.executeQuery(sql);

      BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath));

      // write header line containing column names
      fileWriter.write("name,employee,location,type,status,description, attribute1, attribute2");

      while (result.next()) {
        String name = result.getString("Name");
        String employee = result.getString("employeename");
        String location = result.getString("location");
        String type = result.getString("type");
        String status = result.getString("status");
        String description = result.getString("description");
        String attribute1 = result.getString("attribute1");
        String attribute2 = "NONE";
        String[] att =
            new String[] {
              name, employee, location, type, status, description, attribute1, attribute2
            };
        for (int i = 0; i < att.length; i++) {
          if (att[i].compareTo(" ") == 0) {
            att[i] = "None";
          }
        }
        String line =
            String.format(
                "%s,%s,%s, %s, %s, %s, %s,%s",
                att[0], att[1], att[2], att[3], att[4], att[5], att[6], att[7]);

        fileWriter.newLine();
        fileWriter.write(line);
      }

      statement.close();
      fileWriter.close();

    } catch (SQLException | IOException e) {
      System.out.println("Datababse error:");
      e.printStackTrace();
    }
  }

  public ArrayList<String> getFreeEmployees() throws SQLException {
    return Adb.getFreeEmployees();
  }

  public void resetData() {
    this.reqData = new HashMap<String, Request>();
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void incrementCount() {
    this.count++;
  }

  public void uploadRequest(Request request) {
    this.reqData.put(request.getName(), request);
    try {
      RequestDAOImpl.getInstance("AllRequests").reqData.put(request.getName(), request);
    } catch (SQLException sqlException) {
    }
  }
}
