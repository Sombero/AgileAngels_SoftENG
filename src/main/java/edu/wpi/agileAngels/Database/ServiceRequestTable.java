package edu.wpi.agileAngels.Database;

import edu.wpi.agileAngels.Adb;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceRequestTable implements TableI {

  /**
   * Adds a new Request to the service request table.
   *
   * @param obj new Request
   * @return True if successful, false otherwise.
   */
  @Override
  public boolean add(Object obj) {
    try {
      if (!(obj instanceof Request)) {
        return false;
      }
      Request request = (Request) obj;
      String add =
          "INSERT INTO ServiceRequests(Name, EmployeeName, Location, Type, Status, Description, Attribute1, Attribute2, MedEquipID) VALUES(?,?,?,?,?,?,?,?,?)";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(add);
      preparedStatement.setString(1, request.getName());
      preparedStatement.setString(2, request.getEmployee().getName());
      preparedStatement.setString(3, request.getLocation().getNodeID());
      preparedStatement.setString(4, request.getType());
      preparedStatement.setString(5, request.getStatus());
      preparedStatement.setString(6, request.getDescription());
      preparedStatement.setString(7, request.getAttribute1());
      preparedStatement.setString(8, request.getAttribute2());
      if (request.getMedicalEquip() == null) {
        preparedStatement.setString(9, "N/A");
      } else {
        preparedStatement.setString(9, request.getMedicalEquip().getID());
      }
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Deletes a service request by name.
   *
   * @param str Request name
   * @return True if successful, false otherwise.
   */
  @Override
  public boolean delete(String str) {
    try {
      String delete = "DELETE FROM ServiceRequests WHERE Name = ?";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(delete);
      preparedStatement.setString(1, str);
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Updates Request specified by the name.
   *
   * @param obj updated Request
   * @return True if successful, false otherwise.
   */
  @Override
  public boolean update(Object obj) {
    try {
      if (!(obj instanceof Request)) {
        return false;
      }
      Request request = (Request) obj;
      String update =
          "UPDATE ServiceRequests SET EmployeeName = ?, Location = ?, Type = ?, Status = ?, Description = ?, Attribute1 = ?, Attribute2 = ?, MedEquipID = ? WHERE Name = ?";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(update);
      preparedStatement.setString(1, request.getEmployee().getName());
      preparedStatement.setString(2, request.getLocation().getNodeID());
      preparedStatement.setString(3, request.getType());
      preparedStatement.setString(4, request.getStatus());
      preparedStatement.setString(5, request.getDescription());
      preparedStatement.setString(6, request.getAttribute1());
      preparedStatement.setString(7, request.getAttribute2());
      if (request.getMedicalEquip() == null) {
        preparedStatement.setString(8, "N/A");
      } else {
        preparedStatement.setString(8, request.getMedicalEquip().getID());
      }
      preparedStatement.setString(9, request.getName());
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Creates a new service request table.
   *
   * @return True if successful, false otherwise.
   */
  @Override
  public boolean createTable() {
    try {
      Statement query = DBconnection.getConnection().createStatement();
      String queryServiceRequests =
          "CREATE TABLE ServiceRequests("
              + "Name VARCHAR(50),"
              + "EmployeeName VARCHAR(50),"
              + "Location VARCHAR(50),"
              + "Type VARCHAR(50),"
              + "Status VARCHAR(50),"
              + "Description VARCHAR(50),"
              + "Attribute1 VARCHAR(50),"
              + "Attribute2 VARCHAR(50),"
              + "MedEquipID VARCHAR(50),"
              + "PRIMARY KEY (Name))";
      query.execute(queryServiceRequests);
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Drops the service request table.
   *
   * @return True if successful, false otherwise.
   */
  @Override
  public boolean dropTable() {
    try {
      Statement dropTable = DBconnection.getConnection().createStatement();
      String queryDropMed = "DROP TABLE ServiceRequests";
      dropTable.execute(queryDropMed);
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  @Override
  public HashMap<String, Object> getData() {
    int maxMed = 0,
        maxLab = 0,
        maxMorgue = 0,
        maxMeal = 0,
        maxMain = 0,
        maxTran = 0,
        maxGift = 0,
        maxSan = 0,
        maxLaund = 0;
    try {
      DBconnection.getConnection().setAutoCommit(false);
      String sql = "SELECT * FROM ServiceRequests";
      HashMap<String, Employee> employeeHashmap = Adb.getEmployees();
      HashMap<String, Location> locationHashMap = Adb.getLocations();
      Adb.resetServiceRequests();

      Statement statement = DBconnection.getConnection().createStatement();
      ResultSet result = statement.executeQuery(sql);

      while (result.next()) {
        String name = result.getString("Name");
        Employee employee = employeeHashmap.get(result.getString("employeename"));
        Location location = locationHashMap.get(result.getString("location"));
        String type = result.getString("type");
        String status = result.getString("status");
        String description = result.getString("description");
        String attribute1 = result.getString("attribute1");
        String attribute2 = result.getString("attribute2");
        String medEquipID = result.getString("MedEquipID");

        Request request;
        if (medEquipID.equals("N/A")) {
          request =
              new Request(
                  name, employee, location, type, status, description, attribute1, attribute2);
        } else {
          MedicalEquip equip = MedEquipImpl.getInstance().getAllMedicalEquipment().get(medEquipID);
          request =
              new Request(
                  name,
                  employee,
                  location,
                  type,
                  status,
                  description,
                  attribute1,
                  attribute2,
                  equip);
          maxMed = Integer.parseInt(request.getName().substring(3));
          Adb.addMedRequest(request);
        }
        if (name.substring(0, 3).compareTo("San") == 0) {
          maxSan = Integer.parseInt(request.getName().substring(3));
          Adb.addSanitationRequest(request);
        } else if (name.substring(0, 3).compareTo("Lab") == 0) {
          maxLab = Integer.parseInt(request.getName().substring(3));
          Adb.addLabRequest(request);
        } else if (name.substring(0, 3).compareTo("Mor") == 0) {
          maxMorgue = Integer.parseInt(request.getName().substring(6));
          Adb.addMorgueRequest(request);
        } else if (name.substring(0, 4).compareTo("Meal") == 0) {
          maxMeal = Integer.parseInt(request.getName().substring(4));
          Adb.addMealRequest(request);
        } else if (name.substring(0, 4).compareTo("Main") == 0) {
          maxMain = Integer.parseInt(request.getName().substring(4));
          Adb.addMainRequest(request);
        } else if (name.substring(0, 4).compareTo("Tran") == 0) {
          maxTran = Integer.parseInt(request.getName().substring(4));
          Adb.addTransportRequest(request);
        } else if (name.substring(0, 4).compareTo("Gift") == 0) {
          maxGift = Integer.parseInt(request.getName().substring(4));
          Adb.addGiftRequest(request);
        } else if (name.substring(0, 4).compareTo("Laun") == 0) {
          maxLaund = Integer.parseInt(request.getName().substring(7));
          Adb.addLaundryRequest(request);
        }
      }
      DBconnection.getConnection().setAutoCommit(true);
      resetCounts(maxMed, maxLab, maxMorgue, maxMeal, maxMain, maxTran, maxGift, maxSan, maxLaund);
      return null;
    } catch (SQLException sqlException) {
      try {
        DBconnection.getConnection().setAutoCommit(true);
      } catch (SQLException sqlException1) {
        return null;
      }
      return null;
    }
  }

  public static ArrayList<String> freeEmployees() throws SQLException {
    Statement statement = DBconnection.getConnection().createStatement();
    ArrayList<String> employees = new ArrayList<>();
    String freeEmployee =
        "(SELECT Employees.Name FROM Employees left outer join ServiceRequests on ServiceRequests.EmployeeName = Employees.Name WHERE ServiceRequests.EmployeeName IS NULL OR ServiceRequests.Status in ('complete') GROUP BY Employees.Name) EXCEPT (SELECT Employees.Name FROM ServiceRequests join Employees on ServiceRequests.EmployeeName = Employees.Name WHERE Status in ('notStarted','inProgress','Not Complete','In Progress','Not Started'))";
    ResultSet rs = statement.executeQuery(freeEmployee);
    while (rs.next()) {
      employees.add(rs.getString("Name"));
    }
    return employees;
  }

  private void resetCounts(
      int medCount,
      int labCount,
      int morgueCount,
      int mealCount,
      int mainCount,
      int tranCount,
      int giftCount,
      int sanCount,
      int laundCount) {
    try {
      RequestDAOImpl.getInstance("MedRequest").setCount(medCount);
      RequestDAOImpl.getInstance("LabRequest").setCount(labCount);
      RequestDAOImpl.getInstance("MorgueRequest").setCount(morgueCount);
      RequestDAOImpl.getInstance("MealRequest").setCount(mealCount);
      RequestDAOImpl.getInstance("MaintenanceRequest").setCount(mainCount);
      RequestDAOImpl.getInstance("TransportRequest").setCount(tranCount);
      RequestDAOImpl.getInstance("GiftRequest").setCount(giftCount);
      RequestDAOImpl.getInstance("SanitationRequest").setCount(sanCount);
      RequestDAOImpl.getInstance("LaundryRequest").setCount(laundCount);
    } catch (SQLException sqlException) {
    }
  }
}
