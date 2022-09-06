package edu.wpi.agileAngels.Database;

import edu.wpi.agileAngels.Adb;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class EmployeeTable implements TableI {
  /**
   * Adds a new Employee to the employee table.
   *
   * @param obj Employee
   * @return True if successful, false if not
   */
  @Override
  public boolean add(Object obj) {
    try {
      if (!(obj instanceof Employee)) {
        return false;
      }
      Employee emp = (Employee) obj;
      String supervisees = "";
      if (!emp.getSupervisees().isEmpty()) {
        supervisees += emp.getSupervisees().get(0).getName();
        for (int i = 1; i < emp.getSupervisees().size(); i++) {
          supervisees += ", " + emp.getSupervisees().get(i).getName();
        }
      }

      String add =
          "INSERT INTO Employees(name, password, floorOnDuty, permission, department, startTime, endTime, supervisor, supervisees)VALUES(?,?,?,?,?,?,?,?,?)";
      PreparedStatement preparedStatement = DBconnection.getConnection().prepareStatement(add);
      preparedStatement.setString(1, emp.getName());
      preparedStatement.setString(2, emp.getPassword());
      preparedStatement.setString(3, emp.getFloorOnDuty());
      preparedStatement.setString(4, Integer.toString(emp.getPermissionLevel()));
      preparedStatement.setString(5, emp.getDepartment());
      preparedStatement.setString(6, emp.getStartTime().toString());
      preparedStatement.setString(7, emp.getEndTime().toString());
      if (emp.getSupervisor() == null) {
        preparedStatement.setString(8, "None");
      } else {
        preparedStatement.setString(8, emp.getSupervisor().getName());
      }
      preparedStatement.setString(9, supervisees);
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Deletes an employee from the employee table
   *
   * @param str Employee name
   * @return True if successful, false if not
   */
  @Override
  public boolean delete(String str) {
    try {
      PreparedStatement preparedStatement =
          DBconnection.getConnection().prepareStatement("DELETE FROM Employees WHERE name = ?");
      preparedStatement.setString(1, str);
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Updates an employee by name
   *
   * @param obj updated Employee
   * @return True if successful, false if not
   */
  @Override
  public boolean update(Object obj) {
    try {
      if (!(obj instanceof Employee)) {
        return false;
      }
      Employee emp = (Employee) obj;
      String supervisees = "";
      if (!emp.getSupervisees().isEmpty()) {
        supervisees += emp.getSupervisees().get(0).getName();
        for (int i = 1; i < emp.getSupervisees().size(); i++) {
          supervisees += ", " + emp.getSupervisees().get(i).getName();
        }
      }
      PreparedStatement preparedStatement =
          DBconnection.getConnection()
              .prepareStatement(
                  "UPDATE Employees SET password = ?, floorOnDuty = ?, permission = ?, department = ?, startTime = ?, endTime = ?, supervisor = ?, supervisees = ? WHERE name = ?");
      preparedStatement.setString(1, emp.getPassword());
      preparedStatement.setString(2, emp.getFloorOnDuty());
      preparedStatement.setString(3, Integer.toString(emp.getPermissionLevel()));
      preparedStatement.setString(4, emp.getDepartment());
      preparedStatement.setString(5, emp.getStartTime().toString());
      preparedStatement.setString(6, emp.getEndTime().toString());
      if (emp.getSupervisor() == null) {
        preparedStatement.setString(7, "None");
      } else {
        preparedStatement.setString(7, emp.getSupervisor().getName());
      }
      preparedStatement.setString(8, supervisees);
      preparedStatement.setString(9, emp.getName());
      preparedStatement.execute();
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Creates a new employee table
   *
   * @return True if successful, false if not
   */
  @Override
  public boolean createTable() {
    try {
      Statement query = DBconnection.getConnection().createStatement();
      String queryEmployees =
          "CREATE TABLE Employees( "
              + "Name VARCHAR(50),"
              + "Password VARCHAR(50),"
              + "FloorOnDuty VARCHAR(50),"
              + "Permission VARCHAR(50),"
              + "Department VARCHAR(50),"
              + "StartTime VARCHAR(50),"
              + "EndTime VARCHAR(50),"
              + "Supervisor VARCHAR(50),"
              + "Supervisees VARCHAR(50),"
              + "PRIMARY KEY (Name))";
      query.execute(queryEmployees);
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  /**
   * Drops the employee table
   *
   * @return True if successful, false if not
   */
  @Override
  public boolean dropTable() {
    try {
      Statement droptable = DBconnection.getConnection().createStatement();
      String dropLoc = "DROP TABLE Employees";
      droptable.execute(dropLoc);
      return true;
    } catch (SQLException sqlException) {
      return false;
    }
  }

  @Override
  public HashMap<String, Object> getData() throws SQLException {
    Adb.readCSVEmployees();
    return Adb.getEmployees();
  }
}
